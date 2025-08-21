package com.justin.finance_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for dev
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // allow H2 console frames
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // allow H2 console
                        .requestMatchers("/api/auth/**").permitAll()  // allow auth endpoints
                        .anyRequest().authenticated()                 // protect everything else
                );
        return http.build();
    }
}
