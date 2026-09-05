package com.anuge.legaloffice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileActionRepository;

@Service
public class FileActionService {

    private final FileActionRepository fileActionRepository;

    public FileActionService(FileActionRepository fileActionRepository) {
        this.fileActionRepository = fileActionRepository;
    }

    public FileAction createAction(
            LegalFile legalFile,
            String action,
            String fromStage,
            String toStage,
            String remarks,
            Users performedBy) {

        FileAction fileAction = new FileAction();

        fileAction.setLegalFile(legalFile);
        fileAction.setAction(action);
        fileAction.setFromStage(fromStage);
        fileAction.setToStage(toStage);
        fileAction.setRemarks(remarks);
        fileAction.setPerformedBy(performedBy);

        return fileActionRepository.save(fileAction);
    }

    public List<FileAction> getActionsByFileId(Long fileId) {

        return fileActionRepository
                .findByLegalFileIdOrderByPerformedAtDesc(fileId);
    }
}