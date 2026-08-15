package com.anuge.wallet.service;

import org.springframework.stereotype.Service;

import com.anuge.wallet.dto.LoginRequest;
import com.anuge.wallet.entity.LoginEntity;
import com.anuge.wallet.repository.LoginRepository;

@Service
public class LoginAuth {

    private final LoginRepository loginRepository;

    public LoginAuth(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public String login(LoginRequest request) {

        LoginEntity user =
                loginRepository
                    .findByUsername(request.getUsername())
                    .orElse(null);

        if (user == null) {
            return "User name not found";
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return "Incorrect Password";
        }

        return "Login Successful";
    }
}
