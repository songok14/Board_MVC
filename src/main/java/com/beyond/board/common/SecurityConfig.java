package com.beyond.board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeRequests().antMatchers("/", "/author/create", "/author/login").permitAll().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/author/login")
                // 사전에 구현돼 있는 doLogin 메서드 그대로 사용
                .loginProcessingUrl("/doLogin")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/doLogout")
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
