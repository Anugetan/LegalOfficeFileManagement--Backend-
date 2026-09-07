package com.anuge.legaloffice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "initial_reviews")
public class InitialReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private LegalFile legalFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Users reviewedBy;

    @Column(name = "review_status", nullable = false, length = 30)
    private String reviewStatus = "PENDING";

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    public InitialReview() {
    }

    @PrePersist
    protected void onCreate() {
        if (reviewDate == null) {
            reviewDate = LocalDateTime.now();
        }

        if (reviewStatus == null || reviewStatus.isBlank()) {
            reviewStatus = "PENDING";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LegalFile getLegalFile() {
        return legalFile;
    }

    public void setLegalFile(LegalFile legalFile) {
        this.legalFile = legalFile;
    }

    public Users getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Users reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}