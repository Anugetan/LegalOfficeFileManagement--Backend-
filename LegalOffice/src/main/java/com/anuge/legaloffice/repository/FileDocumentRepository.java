package com.anuge.legaloffice.repository;

import com.anuge.legaloffice.entity.FileDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileDocumentRepository
        extends JpaRepository<FileDocument, Long> {

    List<FileDocument> findByLegalFileId(Long fileId);
}
