package com.salam.ftth.config.auth;

import eu.fraho.spring.securityJwt.base.JwtAuthenticationTokenFilter;
import eu.fraho.spring.securityJwt.base.config.JwtSecurityConfig;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import eu.fraho.spring.securityJwt.hibernate.service.HibernateTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig extends JwtSecurityConfig {

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                          JwtTokenService jwtTokenService) {
        super(userDetailsService, passwordEncoder, jwtTokenService);
    }

    @Bean
    @Override
    public AuthenticationProvider authenticationProvider() {
        return super.authenticationProvider();
    }

    @Bean
    @Override
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return super.authenticationTokenFilterBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)
            throws Exception {
        return super.authenticationManager(authConfiguration);
    }

    @Bean
    @Override
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authenticationProvider(authenticationProvider())
                .exceptionHandling()
                .authenticationEntryPoint(new AppAuthenticationEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll();
                    registry.requestMatchers("/auth/**",
                            "/otp/verify",
                            "/customers/register",
                            "/plans").permitAll();
                    registry.anyRequest().authenticated();
                })
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public HibernateTokenStore refreshTokenStore() {
        return new HibernateTokenStore();
    }

}
