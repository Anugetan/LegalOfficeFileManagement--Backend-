package com.anuge.legaloffice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.legaloffice.dto.InitialReviewRequest;
import com.anuge.legaloffice.dto.InitialReviewResponse;
import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.InitialReview;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileActionRepository;
import com.anuge.legaloffice.repository.InitialReviewRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.StatusRepository;
import com.anuge.legaloffice.repository.UserRepository;

@Service
public class InitialReviewService {

    private final InitialReviewRepository initialReviewRepository;
    private final LegalFileRepository legalFileRepository;
    private final UserRepository userRepository;
    private final FileActionRepository fileActionRepository;
    private final StatusRepository statusRepository;

    public InitialReviewService(
            InitialReviewRepository initialReviewRepository,
            LegalFileRepository legalFileRepository,
            UserRepository userRepository,
            FileActionRepository fileActionRepository,
            StatusRepository statusRepository) {

        this.initialReviewRepository = initialReviewRepository;
        this.legalFileRepository = legalFileRepository;
        this.userRepository = userRepository;
        this.fileActionRepository = fileActionRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional
    public InitialReview createInitialReview(
            InitialReviewRequest request) {

        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (request == null) {
            throw new RuntimeException(
                    "Initial review request is required."
            );
        }

        if (request.getFileId() == null) {
            throw new RuntimeException(
                    "File ID is required."
            );
        }

        if (request.getReviewedBy() == null) {
            throw new RuntimeException(
                    "Reviewer ID is required."
            );
        }

        // =====================================================
        // FIND LEGAL FILE
        // =====================================================

        LegalFile legalFile =
                legalFileRepository
                        .findById(request.getFileId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Legal file not found: "
                                                + request.getFileId()
                                )
                        );

        // =====================================================
        // FIND REVIEWER
        // =====================================================

        Users reviewer =
                userRepository
                        .findById(request.getReviewedBy())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reviewer not found: "
                                                + request.getReviewedBy()
                                )
                        );

        // =====================================================
        // REVIEW STATUS
        // =====================================================

        String reviewStatus =
                request.getReviewStatus();

        if (reviewStatus == null ||
                reviewStatus.isBlank()) {

            reviewStatus = "PENDING";
        }

        reviewStatus =
                reviewStatus
                        .trim()
                        .toUpperCase();

        validateReviewStatus(reviewStatus);

        // =====================================================
        // CURRENT STAGE
        // =====================================================

        String currentStage =
                legalFile.getCurrentStage();

        if (currentStage == null ||
                currentStage.isBlank()) {

            currentStage = "RECEIVED";
        }

        currentStage =
                currentStage
                        .trim()
                        .toUpperCase();

        // =====================================================
        // INITIAL REVIEW IS ONLY ALLOWED HERE
        // =====================================================

        if (!currentStage.equals("RECEIVED") &&
                !currentStage.equals("INITIAL_REVIEW")) {

            throw new RuntimeException(
                    "Initial review is not allowed when "
                            + "current stage is: "
                            + currentStage
            );
        }

        // =====================================================
        // CREATE INITIAL REVIEW
        // =====================================================

        InitialReview review =
                new InitialReview();

        review.setLegalFile(legalFile);
        review.setReviewedBy(reviewer);
        review.setReviewStatus(reviewStatus);
        review.setRemarks(request.getRemarks());
        review.setReviewDate(LocalDateTime.now());

        InitialReview savedReview =
                initialReviewRepository.save(review);

        // =====================================================
        // UPDATE LEGAL FILE WORKFLOW
        // =====================================================

        updateLegalFileWorkflow(
                legalFile,
                reviewStatus
        );

        // =====================================================
        // SAVE LEGAL FILE
        // =====================================================

        legalFileRepository.save(legalFile);

        // =====================================================
        // CREATE ACTION HISTORY
        // =====================================================

        createAction(
                legalFile,
                reviewer,
                currentStage,
                legalFile.getCurrentStage(),
                reviewStatus,
                request.getRemarks()
        );

        return savedReview;
    }

    // =========================================================
    // VALIDATE REVIEW STATUS
    // =========================================================

    private void validateReviewStatus(
            String reviewStatus) {

        if (!reviewStatus.equals("PENDING") &&
                !reviewStatus.equals("APPROVED") &&
                !reviewStatus.equals("RETURNED") &&
                !reviewStatus.equals("REJECTED")) {

            throw new RuntimeException(
                    "Invalid initial review status: "
                            + reviewStatus
            );
        }
    }

    // =========================================================
    // UPDATE LEGAL FILE WORKFLOW
    // =========================================================

    private void updateLegalFileWorkflow(
            LegalFile legalFile,
            String reviewStatus) {

        switch (reviewStatus) {

            // =================================================
            // PENDING
            // =================================================

            case "PENDING":

                legalFile.setCurrentStage(
                        "INITIAL_REVIEW"
                );

                legalFile.setStatus(
                        statusRepository
                                .findByStatusName("PENDING")
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "PENDING status not found"
                                        )
                                )
                );

                break;

            // =================================================
            // APPROVED
            // =================================================

            case "APPROVED":

                legalFile.setCurrentStage(
                        "FINAL_REVIEW"
                );

                legalFile.setStatus(
                        statusRepository
                                .findByStatusName("PENDING")
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "PENDING status not found"
                                        )
                                )
                );

                break;

            // =================================================
            // RETURNED
            // =================================================

            case "RETURNED":

                // Returned means the file needs correction
                // and remains in the initial review workflow.

                legalFile.setCurrentStage(
                        "INITIAL_REVIEW"
                );

                legalFile.setStatus(
                        statusRepository
                                .findByStatusName("PENDING")
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "PENDING status not found"
                                        )
                                )
                );

                break;

            // =================================================
            // REJECTED
            // =================================================

            case "REJECTED":

                // Rejected means the file is OUT.

                legalFile.setCurrentStage(
                        "OUT"
                );

                legalFile.setStatus(
                        statusRepository
                                .findByStatusName("OUT")
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "OUT status not found"
                                        )
                                )
                );

                break;

            // =================================================
            // DEFAULT
            // =================================================

            default:

                throw new RuntimeException(
                        "Unsupported review status: "
                                + reviewStatus
                );
        }
    }

    // =========================================================
    // CREATE FILE ACTION
    // =========================================================

    private void createAction(
            LegalFile legalFile,
            Users reviewer,
            String fromStage,
            String toStage,
            String reviewStatus,
            String remarks) {

        FileAction action =
                new FileAction();

        action.setLegalFile(
                legalFile
        );

        action.setAction(
                "INITIAL REVIEW - "
                        + reviewStatus
        );

        action.setFromStage(
                fromStage
        );

        action.setToStage(
                toStage
        );

        String actionRemarks =
                "Initial review status: "
                        + reviewStatus;

        if (remarks != null &&
                !remarks.trim().isEmpty()) {

            actionRemarks +=
                    " | Remarks: "
                            + remarks.trim();
        }

        action.setRemarks(
                actionRemarks
        );

        action.setPerformedBy(
                reviewer
        );

        action.setPerformedAt(
                LocalDateTime.now()
        );

        fileActionRepository.save(
                action
        );
    }

    // =========================================================
    // GET INITIAL REVIEWS
    // =========================================================

    @Transactional(readOnly = true)
    public List<InitialReviewResponse>
            getReviewsByFile(Long fileId) {

        if (fileId == null) {
            throw new RuntimeException(
                    "File ID is required."
            );
        }

        if (!legalFileRepository.existsById(fileId)) {
            throw new RuntimeException(
                    "Legal file not found: "
                            + fileId
            );
        }

        return initialReviewRepository
                .findByLegalFileIdOrderByReviewDateDesc(
                        fileId
                )
                .stream()
                .map(InitialReviewResponse::new)
                .toList();
    }
}