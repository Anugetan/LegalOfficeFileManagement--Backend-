package com.anuge.legaloffice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "proof_of_service")
public class ProofOfService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================================
    // LEGAL FILE
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private LegalFile legalFile;


    // =========================================================
    // STATUS
    // =========================================================

    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    private String status = "PENDING";


    // =========================================================
    // PROOF FILE
    // =========================================================

    @Column(name = "proof_file_path")
    private String proofFilePath;


    // =========================================================
    // SUBMITTED BY
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Users submittedBy;


    // =========================================================
    // SUBMITTED AT
    // =========================================================

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;


    // =========================================================
    // REMARKS
    // =========================================================

    @Column(columnDefinition = "TEXT")
    private String remarks;


    // =========================================================
    // PRE PERSIST
    // =========================================================

    @PrePersist
    protected void onCreate() {

        if (status == null || status.isBlank()) {
            status = "PENDING";
        }

        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }


    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getProofFilePath() {
        return proofFilePath;
    }

    public void setProofFilePath(String proofFilePath) {
        this.proofFilePath = proofFilePath;
    }


    public Users getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Users submittedBy) {
        this.submittedBy = submittedBy;
    }


    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}