package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.ProofOfService;

public interface ProofOfServiceRepository
        extends JpaRepository<ProofOfService, Long> {

    List<ProofOfService>
    findByLegalFileIdOrderBySubmittedAtDesc(Long fileId);
}