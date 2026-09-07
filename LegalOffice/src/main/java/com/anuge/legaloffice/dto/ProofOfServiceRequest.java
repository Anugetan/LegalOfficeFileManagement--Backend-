package com.anuge.legaloffice.dto;

public class ProofOfServiceRequest {

    private Long fileId;

    private String status;

    private String proofFilePath;

    private String remarks;


    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}