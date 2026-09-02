package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findByActiveTrue();
}