package com.anuge.legaloffice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}