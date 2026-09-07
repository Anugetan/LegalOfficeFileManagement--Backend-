package com.anuge.legaloffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.legaloffice.dto.CreateFileReviewRequest;
import com.anuge.legaloffice.dto.FileReviewResponse;
import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.FileReview;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Status;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileActionRepository;
import com.anuge.legaloffice.repository.FileReviewRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.StatusRepository;
import com.anuge.legaloffice.repository.UserRepository;

@Service
public class FileReviewService {

    private final FileReviewRepository fileReviewRepository;
    private final LegalFileRepository legalFileRepository;
    private final UserRepository userRepository;
    private final FileActionRepository fileActionRepository;
    private final StatusRepository statusRepository;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public FileReviewService(
            FileReviewRepository fileReviewRepository,
            LegalFileRepository legalFileRepository,
            UserRepository userRepository,
            FileActionRepository fileActionRepository,
            StatusRepository statusRepository) {

        this.fileReviewRepository = fileReviewRepository;
        this.legalFileRepository = legalFileRepository;
        this.userRepository = userRepository;
        this.fileActionRepository = fileActionRepository;
        this.statusRepository = statusRepository;
    }

    // =========================================================
    // CREATE REVIEW
    // =========================================================

    @Transactional
    public FileReviewResponse createReview(
            CreateFileReviewRequest request) {

        // -----------------------------------------------------
        // VALIDATE REQUEST
        // -----------------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "Review request cannot be null"
            );
        }

        if (request.getFileId() == null) {

            throw new RuntimeException(
                    "File ID is required"
            );
        }

        if (request.getReviewedBy() == null) {

            throw new RuntimeException(
                    "Reviewer ID is required"
            );
        }

        // -----------------------------------------------------
        // FIND LEGAL FILE
        // -----------------------------------------------------

        LegalFile legalFile =
                legalFileRepository
                        .findById(request.getFileId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Legal file not found: "
                                                + request.getFileId()
                                )
                        );

        // -----------------------------------------------------
        // FIND REVIEWER
        // -----------------------------------------------------

        Users reviewer =
                userRepository
                        .findById(request.getReviewedBy())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reviewer not found: "
                                                + request.getReviewedBy()
                                )
                        );

        // -----------------------------------------------------
        // VALIDATE REVIEW TYPE
        // -----------------------------------------------------

        String reviewType =
                request.getReviewType() != null
                        ? request.getReviewType()
                                .trim()
                                .toUpperCase()
                        : "";

        if (!"INITIAL".equals(reviewType)
                && !"FINAL".equals(reviewType)) {

            throw new RuntimeException(
                    "Invalid review type. "
                            + "Use INITIAL or FINAL."
            );
        }

        // -----------------------------------------------------
        // VALIDATE REVIEW STATUS
        // -----------------------------------------------------

        String reviewStatus =
                request.getReviewStatus() != null
                        ? request.getReviewStatus()
                                .trim()
                                .toUpperCase()
                        : "";

        if (!"PENDING".equals(reviewStatus)
                && !"APPROVED".equals(reviewStatus)
                && !"RETURNED".equals(reviewStatus)
                && !"REJECTED".equals(reviewStatus)) {

            throw new RuntimeException(
                    "Invalid review status. "
                            + "Use PENDING, APPROVED, "
                            + "RETURNED or REJECTED."
            );
        }

        // =====================================================
        // SAVE ORIGINAL STAGE
        // =====================================================

        String fromStage =
                legalFile.getCurrentStage();

        // =====================================================
        // VALIDATE WORKFLOW
        // =====================================================

        validateReviewWorkflow(
                legalFile,
                reviewType,
                reviewStatus
        );

        // =====================================================
        // CREATE REVIEW
        // =====================================================

        FileReview review = new FileReview();

        review.setLegalFile(legalFile);
        review.setReviewedBy(reviewer);
        review.setReviewType(reviewType);
        review.setReviewStatus(reviewStatus);
        review.setRemarks(
                request.getRemarks() != null
                        ? request.getRemarks().trim()
                        : null
        );
        review.setReviewedAt(
                LocalDateTime.now()
        );

        FileReview savedReview =
                fileReviewRepository.save(review);

        // =====================================================
        // UPDATE LEGAL FILE WORKFLOW
        // =====================================================

        updateLegalFileWorkflow(
                legalFile,
                reviewType,
                reviewStatus
        );

        legalFileRepository.save(legalFile);

        // =====================================================
        // GET NEW STAGE
        // =====================================================

        String toStage =
                legalFile.getCurrentStage();

        // =====================================================
        // CREATE ACTION HISTORY
        // =====================================================

        createReviewAction(
                legalFile,
                reviewer,
                reviewType,
                reviewStatus,
                fromStage,
                toStage,
                request.getRemarks()
        );

        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return mapToResponse(savedReview);
    }

    // =========================================================
    // VALIDATE REVIEW WORKFLOW
    // =========================================================

    private void validateReviewWorkflow(
            LegalFile legalFile,
            String reviewType,
            String reviewStatus) {

        String currentStage =
                legalFile.getCurrentStage();

        if (currentStage == null) {
            currentStage = "";
        }

        // =====================================================
        // INITIAL REVIEW
        // =====================================================

        if ("INITIAL".equals(reviewType)) {

            /*
             * A newly created file starts at RECEIVED.
             *
             * Therefore INITIAL review is allowed from:
             *
             * RECEIVED
             * INITIAL_REVIEW
             *
             * RECEIVED is automatically moved to
             * INITIAL_REVIEW by updateLegalFileWorkflow().
             */

            if (!"RECEIVED".equalsIgnoreCase(currentStage)
                    && !"INITIAL_REVIEW"
                            .equalsIgnoreCase(currentStage)) {

                throw new RuntimeException(
                        "File is not ready for initial review. "
                                + "Current stage: "
                                + currentStage
                );
            }

            return;
        }

        // =====================================================
        // FINAL REVIEW
        // =====================================================

        if ("FINAL".equals(reviewType)) {

            /*
             * FINAL review can only happen after INITIAL
             * review has been approved.
             */

            if (!"FINAL_REVIEW"
                    .equalsIgnoreCase(currentStage)) {

                throw new RuntimeException(
                        "File is not ready for final review. "
                                + "Current stage: "
                                + currentStage
                );
            }
        }
    }

    // =========================================================
    // UPDATE LEGAL FILE WORKFLOW
    // =========================================================

    private void updateLegalFileWorkflow(
            LegalFile legalFile,
            String reviewType,
            String reviewStatus) {

        // =====================================================
        // INITIAL REVIEW
        // =====================================================

        if ("INITIAL".equals(reviewType)) {

            // -------------------------------------------------
            // INITIAL PENDING
            // -------------------------------------------------

            if ("PENDING".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "INITIAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }

            // -------------------------------------------------
            // INITIAL APPROVED
            // -------------------------------------------------

            else if ("APPROVED".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "FINAL_REVIEW"
                );

                /*
                 * The file is not yet resolved.
                 *
                 * Overall status remains PENDING.
                 */

                setOverallStatus(
                        legalFile,
                        1L
                );
            }

            // -------------------------------------------------
            // INITIAL RETURNED
            // -------------------------------------------------

            else if ("RETURNED".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "INITIAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }

            // -------------------------------------------------
            // INITIAL REJECTED
            // -------------------------------------------------

            else if ("REJECTED".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "INITIAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }
        }

        // =====================================================
        // FINAL REVIEW
        // =====================================================

        else if ("FINAL".equals(reviewType)) {

            // -------------------------------------------------
            // FINAL PENDING
            // -------------------------------------------------

            if ("PENDING".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "FINAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }

            // -------------------------------------------------
            // FINAL APPROVED
            // -------------------------------------------------

            else if ("APPROVED".equals(reviewStatus)) {

                /*
                 * Final approval completes the file.
                 */

                legalFile.setCurrentStage(
                        "RESOLVED"
                );

                // Overall status = RESOLVED
                setOverallStatus(
                        legalFile,
                        2L
                );

                // -------------------------------------------------
                // SET COMPLETION DATE
                // -------------------------------------------------

                if (legalFile.getDateCompleted() == null) {

                    legalFile.setDateCompleted(
                            LocalDate.now()
                    );
                }
            }

            // -------------------------------------------------
            // FINAL RETURNED
            // -------------------------------------------------

            else if ("RETURNED".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "FINAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }

            // -------------------------------------------------
            // FINAL REJECTED
            // -------------------------------------------------

            else if ("REJECTED".equals(reviewStatus)) {

                legalFile.setCurrentStage(
                        "FINAL_REVIEW"
                );

                setOverallStatus(
                        legalFile,
                        1L
                );
            }
        }
    }

    // =========================================================
    // SET OVERALL STATUS
    // =========================================================

    private void setOverallStatus(
            LegalFile legalFile,
            Long statusId) {

        Status status =
                statusRepository
                        .findById(statusId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Status not found with id: "
                                                + statusId
                                )
                        );

        legalFile.setStatus(status);
    }

    // =========================================================
    // CREATE REVIEW ACTION HISTORY
    // =========================================================

    private void createReviewAction(
            LegalFile legalFile,
            Users reviewer,
            String reviewType,
            String reviewStatus,
            String fromStage,
            String toStage,
            String remarks) {

        FileAction action =
                new FileAction();

        // -----------------------------------------------------
        // LEGAL FILE
        // -----------------------------------------------------

        action.setLegalFile(
                legalFile
        );

        // -----------------------------------------------------
        // ACTION
        // -----------------------------------------------------

        action.setAction(
                reviewType
                        + " REVIEW - "
                        + reviewStatus
        );

        // -----------------------------------------------------
        // FROM STAGE
        // -----------------------------------------------------

        action.setFromStage(
                fromStage
        );

        // -----------------------------------------------------
        // TO STAGE
        // -----------------------------------------------------

        action.setToStage(
                toStage
        );

        // -----------------------------------------------------
        // REMARKS
        // -----------------------------------------------------

        String actionRemarks =
                "Review status: "
                        + reviewStatus;

        if (remarks != null
                && !remarks.isBlank()) {

            actionRemarks +=
                    " | Remarks: "
                            + remarks.trim();
        }

        action.setRemarks(
                actionRemarks
        );

        // -----------------------------------------------------
        // PERFORMED BY
        // -----------------------------------------------------

        action.setPerformedBy(
                reviewer
        );

        // -----------------------------------------------------
        // TIMESTAMP
        // -----------------------------------------------------

        action.setPerformedAt(
                LocalDateTime.now()
        );

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        fileActionRepository.save(
                action
        );
    }

    // =========================================================
    // GET REVIEWS BY FILE
    // =========================================================

    public List<FileReviewResponse> getReviewsByFile(
            Long fileId) {

        if (fileId == null) {

            throw new RuntimeException(
                    "File ID is required"
            );
        }

        return fileReviewRepository
                .findByLegalFileIdOrderByReviewedAtDesc(
                        fileId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET REVIEWS BY TYPE
    // =========================================================

    public List<FileReviewResponse> getReviewsByType(
            Long fileId,
            String reviewType) {

        if (fileId == null) {

            throw new RuntimeException(
                    "File ID is required"
            );
        }

        if (reviewType == null
                || reviewType.isBlank()) {

            throw new RuntimeException(
                    "Review type is required"
            );
        }

        String normalizedReviewType =
                reviewType
                        .trim()
                        .toUpperCase();

        if (!"INITIAL".equals(normalizedReviewType)
                && !"FINAL".equals(normalizedReviewType)) {

            throw new RuntimeException(
                    "Invalid review type. "
                            + "Use INITIAL or FINAL."
            );
        }

        return fileReviewRepository
                .findByLegalFileIdAndReviewTypeOrderByReviewedAtDesc(
                        fileId,
                        normalizedReviewType
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private FileReviewResponse mapToResponse(
            FileReview review) {

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
