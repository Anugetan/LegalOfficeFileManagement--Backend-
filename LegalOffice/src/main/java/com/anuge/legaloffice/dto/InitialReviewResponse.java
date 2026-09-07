package com.anuge.legaloffice.dto;

import java.time.LocalDateTime;

import com.anuge.legaloffice.entity.InitialReview;

public class InitialReviewResponse {

    private Long id;

    private Long fileId;

    private String caseNo;

    private Long reviewedBy;

    private String reviewerName;

    private String reviewStatus;

    private LocalDateTime reviewDate;

    private String remarks;

    public InitialReviewResponse(InitialReview review) {

        this.id = review.getId();

        this.fileId =
                review.getLegalFile() != null
                        ? review.getLegalFile().getId()
                        : null;

        this.caseNo =
                review.getLegalFile() != null
                        ? review.getLegalFile().getCaseNo()
                        : null;

        this.reviewedBy =
                review.getReviewedBy() != null
                        ? review.getReviewedBy().getId()
                        : null;

        this.reviewerName =
                review.getReviewedBy() != null
                        ? review.getReviewedBy().getFullName()
                        : null;

        this.reviewStatus =
                review.getReviewStatus();

        this.reviewDate =
                review.getReviewDate();

        this.remarks =
                review.getRemarks();
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public String getRemarks() {
        return remarks;
    }
}