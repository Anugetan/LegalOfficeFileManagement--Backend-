package com.anuge.legaloffice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class LegalFileRequest {

    private String caseNo;
    private LocalDate dateReceived;
    private LocalTime timeReceived;
    private LocalDate dateCompleted;

    private Long statusId;
    private Long spmsTypeId;
    private Long requestingOfficeId;
    private Long documentTypeId;
    private Long documentFormatId;

    private String contactDetails;
    private String currentStage;


    // GETTERS

    public String getCaseNo() {
        return caseNo;
    }


    public LocalTime getTimeReceived() {
		return timeReceived;
	}


	public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public Long getStatusId() {
        return statusId;
    }

    public Long getSpmsTypeId() {
        return spmsTypeId;
    }

    public Long getRequestingOfficeId() {
        return requestingOfficeId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public Long getDocumentFormatId() {
        return documentFormatId;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getCurrentStage() {
        return currentStage;
    }


    // SETTERS

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    

    public LocalDate getDateReceived() {
		return dateReceived;
	}



	public void setDateReceived(LocalDate dateReceived) {
		this.dateReceived = dateReceived;
	}


	public void setTimeReceived(LocalTime timeReceived) {
		this.timeReceived = timeReceived;
	}



	public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public void setSpmsTypeId(Long spmsTypeId) {
        this.spmsTypeId = spmsTypeId;
    }

    public void setRequestingOfficeId(Long requestingOfficeId) {
        this.requestingOfficeId = requestingOfficeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public void setDocumentFormatId(Long documentFormatId) {
        this.documentFormatId = documentFormatId;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }
}