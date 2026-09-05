package com.anuge.legaloffice.service;


import com.anuge.legaloffice.dto.CreateFileReviewRequest;
import com.anuge.legaloffice.dto.FileReviewResponse;
import com.anuge.legaloffice.entity.FileReview;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileReviewRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileReviewService {

    private final FileReviewRepository fileReviewRepository;
    private final LegalFileRepository legalFileRepository;
    private final UserRepository userRepository;


    public FileReviewService(
            FileReviewRepository fileReviewRepository,
            LegalFileRepository legalFileRepository,
            UserRepository userRepository
    ) {
        this.fileReviewRepository = fileReviewRepository;
        this.legalFileRepository = legalFileRepository;
        this.userRepository = userRepository;
    }


    // =====================================================
    // CREATE REVIEW
    // =====================================================

    @Transactional
    public FileReviewResponse createReview(
            CreateFileReviewRequest request
    ) {

        LegalFile legalFile =
                legalFileRepository
                        .findById(request.getFileId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Legal file not found: "
                                                + request.getFileId()
                                )
                        );


        Users reviewer =
                userRepository
                        .findById(request.getReviewedBy())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: "
                                                + request.getReviewedBy()
                                )
                        );


        FileReview review = new FileReview();

        review.setLegalFile(legalFile);

        review.setReviewedBy(reviewer);

        review.setReviewType(
                request.getReviewType()
        );

        review.setReviewStatus(
                request.getReviewStatus()
        );

        review.setRemarks(
                request.getRemarks()
        );


        FileReview saved =
                fileReviewRepository.save(review);


        return mapToResponse(saved);
    }


    // =====================================================
    // GET ALL REVIEWS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FileReviewResponse> getReviewsByFile(
            Long fileId
    ) {

        return fileReviewRepository
                .findByLegalFileIdOrderByReviewedAtDesc(fileId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET REVIEWS BY TYPE
    // =====================================================

    @Transactional(readOnly = true)
    public List<FileReviewResponse> getReviewsByType(
            Long fileId,
            String reviewType
    ) {

        return fileReviewRepository
                .findByLegalFileIdAndReviewTypeOrderByReviewedAtDesc(
                        fileId,
                        reviewType
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // MAPPER
    // =====================================================

    private FileReviewResponse mapToResponse(
            FileReview review
    ) {

        FileReviewResponse response =
                new FileReviewResponse();


        response.setId(
                review.getId()
        );

        response.setFileId(
                review.getLegalFile().getId()
        );

        response.setCaseNo(
                review.getLegalFile().getCaseNo()
        );

        response.setReviewedBy(
                review.getReviewedBy().getId()
        );

        response.setReviewerName(
                review.getReviewedBy().getFullName()
        );

        response.setReviewType(
                review.getReviewType()
        );

        response.setReviewStatus(
                review.getReviewStatus()
        );

        response.setRemarks(
                review.getRemarks()
        );

        response.setReviewedAt(
                review.getReviewedAt()
        );


        return response;
    }
}