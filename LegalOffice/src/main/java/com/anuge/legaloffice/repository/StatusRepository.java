package com.anuge.legaloffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findByActiveTrue();

    Optional<Status> findByStatusName(String statusName);
}