package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.FileAction;

public interface FileActionRepository extends JpaRepository<FileAction, Long> {

    List<FileAction> findByLegalFileIdOrderByPerformedAtDesc(Long fileId);
}