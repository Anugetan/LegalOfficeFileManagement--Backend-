package com.anuge.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LoginConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {

        // DEVELOPMENT ONLY
        // Your current database stores plain passwords.
        return NoOpPasswordEncoder.getInstance();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})

            .authorizeHttpRequests(auth -> auth

                .requestMatchers( "/api/loginAuth/login").permitAll()
                .requestMatchers("/api/loginAuth/logout").permitAll()
                .anyRequest().authenticated()
            )

            .httpBasic(httpBasic -> httpBasic.disable());


        return http.build();
    }
}