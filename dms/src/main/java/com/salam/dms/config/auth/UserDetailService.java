package com.salam.dms.config.auth;

import com.salam.dms.db.entity.Role;
import com.salam.dms.db.entity.User;
import com.salam.dms.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userService.checkLogin(username);
        return result
                .map(this::buildJwtUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private JwtUser buildJwtUser(User user) {
        var jwtUser = new JwtUser();
        jwtUser.setUsername(firstNonNull(user.getEmail(), user.getPhone()));
        jwtUser.setPassword(user.getPassword());
        jwtUser.setTotpSecret(user.getTotp());
        jwtUser.setAuthorities(mapRolesToAuthorities(user.getRoles()));
        jwtUser.setAccountNonExpired(user.isActive());
        jwtUser.setAccountNonLocked(user.isActive());
        jwtUser.setApiAccessAllowed(user.isActive());
        jwtUser.setCredentialsNonExpired(user.isActive());
        jwtUser.setEnabled(user.isActive());
        jwtUser.setId(user.getId());

        return jwtUser;
    }

    private static List<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return Optional.ofNullable(roles)
                .map(rs -> rs.stream()
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getRole()))
                        .toList())
                .orElse(Collections.emptyList());
    }


}
