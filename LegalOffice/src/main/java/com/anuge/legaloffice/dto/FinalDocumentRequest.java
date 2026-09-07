package com.anuge.legaloffice.dto;

public class FinalDocumentRequest {

    private Long fileId;
    private String documentName;
    private String filePath;
    private Long finalizedBy;
    private String remarks;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}