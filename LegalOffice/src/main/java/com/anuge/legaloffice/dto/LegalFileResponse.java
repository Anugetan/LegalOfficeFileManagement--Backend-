package com.anuge.legaloffice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.anuge.legaloffice.entity.LegalFile;

public class LegalFileResponse {

    private Long id;
    private String caseNo;
    private LocalDate dateReceived;

    private LocalTime timeReceived;
    private LocalDate dateCompleted;
    private Long statusId;
    private String statusName;
    private Long spmsTypeId;
    private String spmsTypeName;
    private Long requestingOfficeId;
    private String requestingOfficeName;
    private Long documentTypeId;
    private String documentTypeName;
    private Long documentFormatId;
    private String documentFormatName;
    private String contactDetails;
    private String currentStage;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LegalFileResponse(LegalFile legalFile) {

        this.id = legalFile.getId();
        this.caseNo = legalFile.getCaseNo();
        this.dateReceived = legalFile.getDateReceived();
        this.timeReceived = legalFile.getTimeReceived();
        this.dateCompleted = legalFile.getDateCompleted();
        this.contactDetails = legalFile.getContactDetails();
        this.currentStage = legalFile.getCurrentStage();
        this.createdAt = legalFile.getCreatedAt();
        this.updatedAt = legalFile.getUpdatedAt();

        // =================================================
        // STATUS
        // =================================================

        if (legalFile.getStatus() != null) {

            this.statusId =
                    legalFile.getStatus().getId();

            this.statusName =
                    legalFile.getStatus().getStatusName();
        }

        // =================================================
        // SPMS TYPE
        // =================================================

        if (legalFile.getSpmsType() != null) {

            this.spmsTypeId =
                    legalFile.getSpmsType().getId();

            this.spmsTypeName =
                    legalFile.getSpmsType().getSpmsName();
        }


        // =================================================
        // REQUESTING OFFICE
        // =================================================

        if (legalFile.getRequestingOffice() != null) {

            this.requestingOfficeId =
                    legalFile.getRequestingOffice().getId();

            this.requestingOfficeName =
                    legalFile.getRequestingOffice().getOfficeName();
        }


        // =================================================
        // DOCUMENT TYPE
        // =================================================

        if (legalFile.getDocumentType() != null) {

            this.documentTypeId =
                    legalFile.getDocumentType().getId();

            this.documentTypeName =
                    legalFile.getDocumentType().getDocumentName();
        }


        // =================================================
        // DOCUMENT FORMAT
        // =================================================

        if (legalFile.getDocumentFormat() != null) {

            this.documentFormatId =
                    legalFile.getDocumentFormat().getId();

            this.documentFormatName =
                    legalFile.getDocumentFormat().getFormatName();
        }


        // =================================================
        // CREATED BY
        // =================================================

        if (legalFile.getCreatedBy() != null) {

            this.createdById =
                    legalFile.getCreatedBy().getId();
        }
    }


    // =====================================================
    // GETTERS / SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }


    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }


    public LocalTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(LocalTime timeReceived) {
        this.timeReceived = timeReceived;
    }


    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


    public Long getSpmsTypeId() {
        return spmsTypeId;
    }

    public void setSpmsTypeId(Long spmsTypeId) {
        this.spmsTypeId = spmsTypeId;
    }


    public String getSpmsTypeName() {
        return spmsTypeName;
    }

    public void setSpmsTypeName(String spmsTypeName) {
        this.spmsTypeName = spmsTypeName;
    }


    public Long getRequestingOfficeId() {
        return requestingOfficeId;
    }

    public void setRequestingOfficeId(Long requestingOfficeId) {
        this.requestingOfficeId = requestingOfficeId;
    }


    public String getRequestingOfficeName() {
        return requestingOfficeName;
    }

    public void setRequestingOfficeName(String requestingOfficeName) {
        this.requestingOfficeName = requestingOfficeName;
    }


    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }


    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }


    public Long getDocumentFormatId() {
        return documentFormatId;
    }

    public void setDocumentFormatId(Long documentFormatId) {
        this.documentFormatId = documentFormatId;
    }


    public String getDocumentFormatName() {
        return documentFormatName;
    }

    public void setDocumentFormatName(String documentFormatName) {
        this.documentFormatName = documentFormatName;
    }


    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }


    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }


    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}