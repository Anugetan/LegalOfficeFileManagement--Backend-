package com.anuge.legaloffice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_reviews")
public class FileReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // LEGAL FILE
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private LegalFile legalFile;


    // =========================
    // REVIEWED BY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", nullable = false)
    private Users reviewedBy;


    // =========================
    // REVIEW TYPE
    // =========================

    @Column(name = "review_type", nullable = false)
    private String reviewType;


    // =========================
    // REVIEW STATUS
    // =========================

    @Column(name = "review_status", nullable = false)
    private String reviewStatus;


    // =========================
    // REMARKS
    // =========================

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;


    // =========================
    // REVIEWED AT
    // =========================

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;


    // =========================
    // PRE PERSIST
    // =========================

    @PrePersist
    protected void onCreate() {

        if (reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }


    // =========================
    // GETTERS / SETTERS
    // =========================

    public Long getId() {
        return id;
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

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}