package com.anuge.legaloffice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    Optional<DocumentType> findByDocumentName(String documentName);

}