package com.anuge.legaloffice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "final_documents")
public class FinalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private LegalFile legalFile;

    @Column(name = "document_name", nullable = false, length = 255)
    private String documentName;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalized_by")
    private Users finalizedBy;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @PrePersist
    protected void onCreate() {
        if (finalizedAt == null) {
            finalizedAt = LocalDateTime.now();
        }
    }

    // =========================
    // GETTERS AND SETTERS
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

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Users getFinalizedBy() {
        return finalizedBy;
    }

    public void setFinalizedBy(Users finalizedBy) {
        this.finalizedBy = finalizedBy;
    }

    public LocalDateTime getFinalizedAt() {
        return finalizedAt;
    }

    public void setFinalizedAt(LocalDateTime finalizedAt) {
        this.finalizedAt = finalizedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}