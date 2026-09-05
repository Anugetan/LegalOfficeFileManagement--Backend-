package com.anuge.legaloffice.repository;

import com.anuge.legaloffice.entity.FileReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileReviewRepository
        extends JpaRepository<FileReview, Long> {

    List<FileReview> findByLegalFileIdOrderByReviewedAtDesc(
            Long fileId
    );

    List<FileReview> findByLegalFileIdAndReviewTypeOrderByReviewedAtDesc(
            Long fileId,
            String reviewType
    );
}