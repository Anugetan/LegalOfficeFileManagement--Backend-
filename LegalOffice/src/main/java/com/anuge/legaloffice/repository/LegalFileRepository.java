package com.anuge.legaloffice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.LegalFile;

public interface LegalFileRepository extends JpaRepository<LegalFile, Long> {

    Optional<LegalFile> findByCaseNo(String caseNo);

    boolean existsByCaseNo(String caseNo);
    
}
