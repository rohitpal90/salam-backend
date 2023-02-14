package com.salam.dms.config.auth;

import com.salam.dms.db.entity.Dealer;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.services.DealerService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
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

    private final DealerService dealerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Dealer> result = dealerService.checkLogin(new DealerLogin(username));
        return result
                .map(this::buildJwtUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private JwtUser buildJwtUser(Dealer dealer) {
        JwtUser user = new JwtUser();
        user.setUsername(dealer.getName());
        // load saved otp
        user.setTotpSecret(dealer.getTotp());
        user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setApiAccessAllowed(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setId(dealer.getId());

        return user;
    }

}
