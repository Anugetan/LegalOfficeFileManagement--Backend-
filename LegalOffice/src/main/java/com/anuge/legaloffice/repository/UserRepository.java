package com.anuge.legaloffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.enumerate.RegistrationStatus;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    List<Users> findByRegistrationStatus(
            RegistrationStatus registrationStatus
    );
}
