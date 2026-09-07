package com.anuge.legaloffice.dto;

import java.time.LocalDateTime;

public class FinalDocumentResponse {

    private Long id;
    private Long fileId;
    private String caseNo;
    private String documentName;
    private String filePath;
    private Long finalizedBy;
    private String finalizedByName;
    private LocalDateTime finalizedAt;
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
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

    public Long getFinalizedBy() {
        return finalizedBy;
    }

    public void setFinalizedBy(Long finalizedBy) {
        this.finalizedBy = finalizedBy;
    }

    public String getFinalizedByName() {
        return finalizedByName;
    }

    public void setFinalizedByName(String finalizedByName) {
        this.finalizedByName = finalizedByName;
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