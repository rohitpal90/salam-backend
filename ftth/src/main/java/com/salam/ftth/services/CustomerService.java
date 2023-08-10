package com.salam.ftth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.ftth.adapter.feign.client.CustomerClient;
import com.salam.ftth.adapter.feign.mock.ClientMockAdapter;
import com.salam.ftth.adapter.model.request.VerifySmsRequest;
import com.salam.ftth.adapter.model.response.VerifyBySmsResponse;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.db.entity.Role;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.UserMetaInfo;
import com.salam.ftth.model.request.IDType;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.model.request.RegisterRequest;
import com.salam.ftth.model.response.CustomerSubscription;
import com.salam.ftth.repos.RoleRepository;
import com.salam.ftth.repos.UserRepository;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static com.salam.ftth.config.exception.AppErrors.USER_EXISTS;
import static com.salam.ftth.config.exception.AppErrors.USER_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    final CustomerClient customerClient;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final ClientMockAdapter clientMockAdapter;
    final BCryptPasswordEncoder passwordEncoder;
    final OtpService otpService;


    public VerifyBySmsResponse createPhoneVerifyRequest(String mobile) {
        var verifyRequest = VerifySmsRequest.createByAccNbr(mobile);
//        return customerClient.verifyBySms(verifyRequest);
        return clientMockAdapter.getFor("verifysms", new TypeReference<>() {
        });
    }

    public void register(RegisterRequest registerRequest) {
        var user = new User();
        user.setName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getMobile());
        user.setPassword(
                passwordEncoder.encode(registerRequest.getPassword())
        );
        user.setActive(false); // activate only after otp verification

        var secret = otpService.generateSecret();
        var totp = otpService.generateCode(secret);
        user.setTotp(String.format("%s_%s", OtpOpType.REGISTER.name(), secret));

        var meta = new UserMetaInfo(
                IDType.valueOf(registerRequest.getIdType()),
                registerRequest.getDob(),
                registerRequest.getId()
        );
        user.setMeta(meta);

        var roles = roleRepository.findAllByRole(Role.RoleType.CUSTOMER);
        user.setRoles(roles);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw AppError.create(USER_EXISTS);
        }

        // send otp request
    }

    public void verifyUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    public List<CustomerSubscription> getCustomerSubscriptions(JwtUser user, Pageable pageable) {
        var metaMapper = new ObjectMapper();
        return userRepository.getCustomerSubscriptions(user.getId(), pageable)
                .stream()
                .map(row -> {
                    JsonNode meta;
                    try {
                        meta = metaMapper.readTree((String) row[2]);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    return new CustomerSubscription((String) row[0], (String) row[1],
                            meta, ((Timestamp) row[3]).toInstant().toString());
                }).toList();
    }

    public void resendOtp(OtpVerifyRequest request) {
        var username = request.getUsername();
        var userOpt = userRepository.findUserByPrincipal(username, false);
        if (userOpt.isEmpty()) {
            throw AppError.create(USER_NOT_FOUND);
        }

        var user = userOpt.get();
        var secret = otpService.generateSecret();
        var totp = otpService.generateCode(secret);

        user.setTotp(secret);
        userRepository.save(user);

        // send totp
    }
}
