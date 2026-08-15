package com.anuge.wallet.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anuge.wallet.entity.LoginEntity;
import com.anuge.wallet.repository.LoginRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final LoginRepository loginRepository;


    public CustomUserDetailsService(LoginRepository loginRepository) {

        this.loginRepository = loginRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        LoginEntity user =  loginRepository.findByUsername(username).orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"
                                )
                        );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}