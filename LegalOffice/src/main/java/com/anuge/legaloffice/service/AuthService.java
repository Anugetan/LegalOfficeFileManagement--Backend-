package com.anuge.legaloffice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anuge.legaloffice.dto.AuthResponse;
import com.anuge.legaloffice.dto.LoginRequest;
import com.anuge.legaloffice.dto.RegisterRequest;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.UserRepository;
import com.anuge.legaloffice.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // REGISTER
    public AuthResponse register(RegisterRequest request) {

        // Check username
        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // Check email
        if (request.getEmail() != null &&
            !request.getEmail().isBlank() &&
            userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Create user
        Users user = new Users();

        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // IMPORTANT:
        // Never store the plain password
        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole("USER");
        user.setActive(true);

        // Save
        Users savedUser = userRepository.save(user);

        // Generate JWT
        String token =
                jwtService.generateToken(
                        savedUser.getUsername()
                );

        // Return
        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getRole()
        );
    }


    // LOGIN
    public AuthResponse login(LoginRequest request) {
    	
        Users user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                    request.getPassword(),
                    user.getPasswordHash()
                );

        Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                    )
                );

//        System.out.println("AUTHENTICATION SUCCESS!");
        String username = authentication.getName();

        String token = jwtService.generateToken(username);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRole()
        );
    }
}