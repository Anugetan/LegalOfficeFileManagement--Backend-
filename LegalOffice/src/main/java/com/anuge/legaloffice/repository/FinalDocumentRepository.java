package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.FinalDocument;

public interface FinalDocumentRepository
        extends JpaRepository<FinalDocument, Long> {

    List<FinalDocument> findByLegalFileIdOrderByFinalizedAtDesc(Long fileId);
}