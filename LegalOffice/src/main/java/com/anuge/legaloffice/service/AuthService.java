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
import com.anuge.legaloffice.enumerate.RegistrationStatus;
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


 // =====================================================
 // REGISTER
 // =====================================================

 public AuthResponse register(RegisterRequest request) {

     // Check username
     if (userRepository.existsByUsername(
             request.getUsername())) {

         throw new RuntimeException(
                 "Username already exists"
         );
     }


     // Check email
     if (request.getEmail() != null
             && !request.getEmail().isBlank()
             && userRepository.existsByEmail(
                     request.getEmail())) {

         throw new RuntimeException(
                 "Email already exists"
         );
     }


     // =================================================
     // CREATE USER
     // =================================================

     Users user = new Users();

     user.setUsername(
             request.getUsername()
     );

     user.setFullName(
             request.getFullName()
     );

     user.setEmail(
             request.getEmail()
     );


     // =================================================
     // PASSWORD
     // =================================================

     user.setPasswordHash(
             passwordEncoder.encode(
                     request.getPassword()
             )
     );


     // =================================================
     // DEFAULT USER SETTINGS
     // =================================================

     user.setRole("USER");

     // User cannot login yet
     user.setActive(false);

     // Waiting for administrator approval
     user.setRegistrationStatus(
             RegistrationStatus.PENDING
     );


     // =================================================
     // SAVE USER
     // =================================================

     Users savedUser =
             userRepository.save(user);


     // =================================================
     // DO NOT GENERATE JWT
     // =================================================

     /*
      * The user is still waiting for administrator
      * approval, so we do NOT generate a JWT here.
      */


     // =================================================
     // RETURN RESPONSE
     // =================================================

     return new AuthResponse(
             null,
             savedUser.getId(),
             savedUser.getUsername(),
             savedUser.getFullName(),
             savedUser.getRole()
     );
 }


    // =====================================================
    // LOGIN
    // =====================================================

//=====================================================
//LOGIN
//=====================================================

public AuthResponse login(LoginRequest request) {

  // =================================================
  // FIND USER
  // =================================================

  Users user =
          userRepository
                  .findByUsername(
                          request.getUsername()
                  )
                  .orElseThrow(() ->
                          new RuntimeException(
                                  "User not found"
                          )
                  );


  System.out.println("USER ID: " + user.getId());
  System.out.println("USERNAME: " + user.getUsername());
  System.out.println("FULL NAME: " + user.getFullName());
  System.out.println(
          "REGISTRATION STATUS: "
                  + user.getRegistrationStatus()
  );


  // =================================================
  // CHECK REGISTRATION STATUS
  // =================================================

  if (user.getRegistrationStatus()
          == RegistrationStatus.PENDING) {

      throw new RuntimeException(
              "Your registration is still pending administrator approval."
      );
  }


  if (user.getRegistrationStatus()
          == RegistrationStatus.REJECTED) {

      throw new RuntimeException(
              "You don't have permission to access the Legal Office system. Your registration request was rejected by the administrator."
      );
  }


  // =================================================
  // CHECK ACTIVE
  // =================================================

  if (!Boolean.TRUE.equals(user.getActive())) {

      throw new RuntimeException(
              "Your account is currently inactive."
      );
  }


  // =================================================
  // AUTHENTICATE USER
  // =================================================

  Authentication authentication =
          authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                          request.getUsername(),
                          request.getPassword()
                  )
          );


  // =================================================
  // GET AUTHENTICATED USERNAME
  // =================================================

  String username =
          authentication.getName();


  // =================================================
  // GENERATE JWT
  // =================================================

  String token =
          jwtService.generateToken(
                  username
          );


  // =================================================
  // RETURN RESPONSE
  // =================================================

  return new AuthResponse(
          token,
          user.getId(),
          user.getUsername(),
          user.getFullName(),
          user.getRole()
  );
}

}
