package com.anuge.legaloffice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.enumerate.RegistrationStatus;
import com.anuge.legaloffice.repository.UserRepository;

@Service
public class AdminUserService {

    private final UserRepository userRepository;


    public AdminUserService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    // =====================================================
    // GET PENDING REGISTRATION REQUESTS
    // =====================================================

    public List<Users> getPendingUsers() {

        return userRepository.findByRegistrationStatus(
                RegistrationStatus.PENDING
        );
    }


    // =====================================================
    // APPROVE USER
    // =====================================================

    public Users approveUser(Long userId) {

        Users user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        user.setRegistrationStatus(
                RegistrationStatus.APPROVED
        );

        user.setActive(true);


        return userRepository.save(user);
    }


    // =====================================================
    // REJECT USER
    // =====================================================

    public Users rejectUser(Long userId) {

        Users user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        user.setRegistrationStatus(
                RegistrationStatus.REJECTED
        );

        user.setActive(false);


        return userRepository.save(user);
    }
}