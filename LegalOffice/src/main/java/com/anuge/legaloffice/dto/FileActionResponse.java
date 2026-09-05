package com.anuge.legaloffice.dto;

import java.time.LocalDateTime;

import com.anuge.legaloffice.entity.FileAction;

public class FileActionResponse {

    private Long id;
    private Long fileId;

    private String action;
    private String fromStage;
    private String toStage;
    private String remarks;

    private Long performedBy;
    private String performedByUsername;
    private String performedByName;

    private LocalDateTime performedAt;

    public FileActionResponse(FileAction fileAction) {

        this.id = fileAction.getId();

        if (fileAction.getLegalFile() != null) {
            this.fileId = fileAction.getLegalFile().getId();
        }

        this.action = fileAction.getAction();
        this.fromStage = fileAction.getFromStage();
        this.toStage = fileAction.getToStage();
        this.remarks = fileAction.getRemarks();

        if (fileAction.getPerformedBy() != null) {

            this.performedBy = fileAction.getPerformedBy().getId();

            this.performedByUsername =
                    fileAction.getPerformedBy().getUsername();

            this.performedByName =
                    fileAction.getPerformedBy().getFullName();
        }

        this.performedAt = fileAction.getPerformedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getAction() {
        return action;
    }

    public String getFromStage() {
        return fromStage;
    }

    public String getToStage() {
        return toStage;
    }

    public String getRemarks() {
        return remarks;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public String getPerformedByUsername() {
        return performedByUsername;
    }

    public String getPerformedByName() {
        return performedByName;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }
}