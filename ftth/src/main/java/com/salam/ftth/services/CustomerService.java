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
import com.salam.ftth.config.exception.AppErrors;
import com.salam.ftth.db.entity.Role;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.UserMetaInfo;
import com.salam.ftth.model.request.IDType;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.model.request.RegisterRequest;
import com.salam.ftth.model.response.CustomerSubscription;
import com.salam.ftth.repos.RoleRepository;
import com.salam.ftth.repos.UserRepository;
import com.salam.libs.feign.elm.model.ErrorSalamResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    final MessageSource messageSource;


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

//    public IdentityInfo verifyAndGetCustomerInfo(CustomerProfileRequest customerInfo) {
//        var nin = customerInfo.getId();
//        var dateOfBirth = customerInfo.getDob();
//
//        var locale = LocaleContextHolder.getLocale();
//        var language = locale.getLanguage();
//
//        var isCitizen = nin.startsWith("1");
//        EntityDto entityDto;
//        try {
//            var identityInfoResponse = isCitizen ?
//                    yakeenClient.getCitizenInfo(nin, dateOfBirth) :
//                    yakeenClient.getExpatInfo(nin, dateOfBirth);
//
//            entityDto = identityInfoResponse.getData();
//        } catch (FeignException e) {
//            handleVerifyErrorIfRequired(e, locale);
//            throw e;
//        }
//
//        var addressesResponse = isCitizen ?
//                yakeenClient.getCitizenAddresses(nin, dateOfBirth, language) :
//                yakeenClient.getExpatsIqamaNumberAddresses(nin, dateOfBirth, language);
//        var addresses = addressesResponse.getData();
//
//        return new IdentityInfo(entityDto, addresses);
//    }

    private void handleVerifyErrorIfRequired(FeignException e, Locale locale) {
        int status = e.status();
        var messagePrefix = "com.validation.yakeen.customer";

        if (status == HttpStatus.BAD_REQUEST.value()) {
            var errorResponse = e.responseBody().map(byteBuffer -> {
                try {
                    return new ObjectMapper().readValue(byteBuffer.array(), ErrorSalamResponse.class);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            var errorInfo = new HashMap<>();
            if (errorResponse.isPresent()) {
                var errors = errorResponse.get().getErrors();
                if (errors.containsKey("nin")) {
                    errorInfo.put("id", getMessageSourceValue(messagePrefix, "nin", locale));
                }

                if (errors.containsKey("dateOfBirth")) {
                    errorInfo.put("dob", getMessageSourceValue(messagePrefix, "dateOfBirth", locale));
                }
            }

            throw AppError.create(AppErrors.BAD_REQUEST, errorInfo, null);
        }

        if (status == HttpStatus.NOT_FOUND.value()) {
            var errorInfo = Map.of("id", getMessageSourceValue(messagePrefix, "nin", locale));
            throw AppError.create(AppErrors.BAD_REQUEST, errorInfo, null);
        }
    }

    public String getMessageSourceValue(String prefix, String value, Locale locale) {
        return messageSource.getMessage(
                String.join(".", prefix, value),
                null,
                locale
        );
    }

    public void verifyBySms(String otp, RequestContext requestContext) {
        var metaInfo = requestContext.getMeta();
        var verifyResponse = metaInfo.getVerifyInfo();

        if (!otp.equals(verifyResponse.getCode())) {
            throw AppError.create(AppErrors.INVALID_OTP);
        }
    }
}
