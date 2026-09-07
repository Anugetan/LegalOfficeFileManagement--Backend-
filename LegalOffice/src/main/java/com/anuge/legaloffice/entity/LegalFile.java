package com.anuge.legaloffice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "legal_files")
public class LegalFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // BASIC INFORMATION
    // =====================================================

    @Column( name = "case_no",nullable = false, unique = true, length = 50)
    private String caseNo;

    @Column(name = "date_received", nullable = false)
    private LocalDate dateReceived;

    @Column( name = "time_received")
    private LocalTime timeReceived;

    @Column(name = "date_completed")
    private LocalDate dateCompleted;


    // =====================================================
    // STATUS
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;


    // =====================================================
    // SPMS TYPE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spms_type_id")
    private SpmsType spmsType;


    // =====================================================
    // REQUESTING OFFICE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requesting_office_id")
    private Office requestingOffice;


    // =====================================================
    // DOCUMENT TYPE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;


    // =====================================================
    // DOCUMENT FORMAT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_format_id")
    private DocumentFormat documentFormat;


    // =====================================================
    // OTHER INFORMATION
    // =====================================================

    @Column(
        name = "contact_details",
        columnDefinition = "TEXT"
    )
    private String contactDetails;


    @Column(
        name = "current_stage",
        length = 50
    )
    private String currentStage = "RECEIVED";


    // =====================================================
    // USER WHO CREATED THE FILE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Users createdBy;


    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;


    @Column(
        name = "updated_at",
        nullable = false
    )
    private LocalDateTime updatedAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LegalFile() {
    }


    // =====================================================
    // PRE-PERSIST
    // =====================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (currentStage == null || currentStage.isBlank()) {
            currentStage = "RECEIVED";
        }
    }


    // =====================================================
    // PRE-UPDATE
    // =====================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }


    // =====================================================
    // GET ID
    // =====================================================

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    // =====================================================
    // CASE NUMBER
    // =====================================================

    public String getCaseNo() {
        return caseNo;
    }


    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }


    // =====================================================
    // DATE RECEIVED
    // =====================================================

    public LocalDate getDateReceived() {
        return dateReceived;
    }


    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }


    // =====================================================
    // TIME RECEIVED
    // =====================================================

    public LocalTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(LocalTime timeReceived) {
        this.timeReceived = timeReceived;
    }


    // =====================================================
    // DATE COMPLETED
    // =====================================================

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }


    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    // =====================================================
    // STATUS
    // =====================================================

    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }


    // =====================================================
    // SPMS TYPE
    // =====================================================

    public SpmsType getSpmsType() {
        return spmsType;
    }


    public void setSpmsType(SpmsType spmsType) {
        this.spmsType = spmsType;
    }


    // =====================================================
    // REQUESTING OFFICE
    // =====================================================

    public Office getRequestingOffice() {
        return requestingOffice;
    }


    public void setRequestingOffice(Office requestingOffice) {
        this.requestingOffice = requestingOffice;
    }


    // =====================================================
    // DOCUMENT TYPE
    // =====================================================

    public DocumentType getDocumentType() {
        return documentType;
    }


    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }


    // =====================================================
    // DOCUMENT FORMAT
    // =====================================================

    public DocumentFormat getDocumentFormat() {
        return documentFormat;
    }


    public void setDocumentFormat(DocumentFormat documentFormat) {
        this.documentFormat = documentFormat;
    }


    // =====================================================
    // CONTACT DETAILS
    // =====================================================

    public String getContactDetails() {
        return contactDetails;
    }


    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }


    // =====================================================
    // CURRENT STAGE
    // =====================================================

    public String getCurrentStage() {
        return currentStage;
    }


    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }


    // =====================================================
    // CREATED BY
    // =====================================================

    public Users getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // =====================================================
    // UPDATED AT
    // =====================================================

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}