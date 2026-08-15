package com.anuge.wallet.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.wallet.dto.LoginRequest;
import com.anuge.wallet.entity.LoginEntity;
import com.anuge.wallet.repository.LoginRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/loginAuth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final LoginRepository loginRepository;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();


    public LoginController(
            AuthenticationManager authenticationManager,
            LoginRepository loginRepository) {

        this.authenticationManager = authenticationManager;
        this.loginRepository = loginRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        try {

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    );


            Authentication authentication =
                    authenticationManager.authenticate(token);


            
            SecurityContext context = SecurityContextHolder.createEmptyContext(); // Create SecurityContext

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);


            
            securityContextRepository.saveContext( context, httpRequest, null );// Save authentication into HTTP session


            return ResponseEntity.ok("Login Successful");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Not authenticated");
        }


        String username =
                authentication.getName();


        LoginEntity user =
                loginRepository
                        .findByUsername(username)
                        .orElseThrow();


        return ResponseEntity.ok(
                Map.of(
                        "username", user.getUsername(),
                        "name", user.getName(),
                        "lastname", user.getLastname()
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request) {

        SecurityContextHolder.clearContext();


        if (request.getSession(false) != null) {

            request.getSession(false).invalidate();
        }


        return ResponseEntity.ok(
                "Logout Successful"
        );
    }
}