package com.salam.dms.config.auth;

import com.salam.dms.db.entity.Dealer;
import com.salam.dms.db.entity.Role;
import com.salam.dms.db.entity.User;
import com.salam.dms.db.entity.UserRole;
import com.salam.dms.model.request.DealerEmailLogin;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.repos.RoleRepository;
import com.salam.dms.repos.UserRoleRepository;
import com.salam.dms.services.DealerService;
import com.salam.dms.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userService.checkLogin(username);
        return result
                .map(this::buildJwtUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private JwtUser buildJwtUser(User user) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(user.getEmail());
        jwtUser.setPassword(user.getPassword());
        // load saved otp
        UserRole roleId = userRoleRepository.findByUserId(user.getId());
        Role role = roleRepository.findByRole(roleId.getRoleId());
        jwtUser.setTotpSecret(user.getTotp());
        jwtUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(role.getRole())));
        jwtUser.setAccountNonExpired(true);
        jwtUser.setAccountNonLocked(true);
        jwtUser.setApiAccessAllowed(true);
        jwtUser.setCredentialsNonExpired(true);
        jwtUser.setEnabled(true);
        jwtUser.setId(user.getId());


        return jwtUser;
    }


}
