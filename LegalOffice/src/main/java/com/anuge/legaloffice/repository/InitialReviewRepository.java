package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.InitialReview;

public interface InitialReviewRepository
        extends JpaRepository<InitialReview, Long> {

    List<InitialReview>
        findByLegalFileIdOrderByReviewDateDesc(Long fileId);
}