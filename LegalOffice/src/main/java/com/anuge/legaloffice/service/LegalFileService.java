package com.anuge.legaloffice.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.anuge.legaloffice.dto.LegalFileRequest;
import com.anuge.legaloffice.entity.DocumentFormat;
import com.anuge.legaloffice.entity.DocumentType;
import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Office;
import com.anuge.legaloffice.entity.SpmsType;
import com.anuge.legaloffice.entity.Status;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.DocumentFormatRepository;
import com.anuge.legaloffice.repository.DocumentTypeRepository;
import com.anuge.legaloffice.repository.FileActionRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.OfficeRepository;
import com.anuge.legaloffice.repository.SpmsTypeRepository;
import com.anuge.legaloffice.repository.StatusRepository;
import com.anuge.legaloffice.repository.UserRepository;


@Service
public class LegalFileService {

    private final LegalFileRepository legalFileRepository;
    private final StatusRepository statusRepository;
    private final SpmsTypeRepository spmsTypeRepository;
    private final OfficeRepository officeRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentFormatRepository documentFormatRepository;

    private final FileActionRepository fileActionRepository;
    private final UserRepository usersRepository;


    public LegalFileService(
            LegalFileRepository legalFileRepository,
            StatusRepository statusRepository,
            SpmsTypeRepository spmsTypeRepository,
            OfficeRepository officeRepository,
            DocumentTypeRepository documentTypeRepository,
            DocumentFormatRepository documentFormatRepository,
            FileActionRepository fileActionRepository,
            UserRepository usersRepository) {

        this.legalFileRepository = legalFileRepository;
        this.statusRepository = statusRepository;
        this.spmsTypeRepository = spmsTypeRepository;
        this.officeRepository = officeRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentFormatRepository = documentFormatRepository;

        this.fileActionRepository = fileActionRepository;
        this.usersRepository = usersRepository;
    }


    // =========================================
    // GET CURRENT AUTHENTICATED USER
    // =========================================

    private Users getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        // CHECK AUTHENTICATION

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication.getName() == null ||
            authentication.getName().equals("anonymousUser")) {

            throw new RuntimeException(
                "No authenticated user found"
            );
        }


        // GET USERNAME FROM JWT

        String username = authentication.getName();


        System.out.println(
            "AUTHENTICATED USER = " + username
        );


        // FIND USER IN DATABASE

        return usersRepository
                .findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Authenticated user not found: "
                        + username
                    )
                );
    }


    // =========================================
    // CREATE FILE ACTION
    // =========================================

    private FileAction createFileAction(
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


    // =========================================
    // GET ALL LEGAL FILES
    // =========================================

    public List<LegalFile> getAllLegalFiles() {

        return legalFileRepository.findAll();
    }


    // =========================================
    // GET LEGAL FILE BY ID
    // =========================================

    public LegalFile getLegalFileById(Long id) {

        return legalFileRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Legal file not found with id: " + id
                    )
                );
    }


    // =========================================
    // GET LEGAL FILE BY CASE NUMBER
    // =========================================

    public LegalFile getLegalFileByCaseNo(String caseNo) {

        return legalFileRepository.findByCaseNo(caseNo)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Legal file not found with case number: "
                        + caseNo
                    )
                );
    }


    // =========================================
    // CREATE LEGAL FILE
    // =========================================

    public LegalFile createLegalFile(
            LegalFileRequest request) {


        // =========================================
        // GET CURRENT USER
        // =========================================

        Users currentUser = getCurrentUser();


        System.out.println(
            "CREATING LEGAL FILE BY USER = "
            + currentUser.getUsername()
        );


        System.out.println(
            "STATUS ID FROM REQUEST = "
            + request.getStatusId()
        );


        // =========================================
        // CHECK DUPLICATE CASE NUMBER
        // =========================================

        if (legalFileRepository.existsByCaseNo(
                request.getCaseNo())) {

            throw new RuntimeException(
                "Case number already exists: "
                + request.getCaseNo()
            );
        }


        LegalFile legalFile = new LegalFile();


        // =========================================
        // BASIC DATA
        // =========================================

        legalFile.setCaseNo(
            request.getCaseNo()
        );

        legalFile.setDateReceived(
            request.getDateReceived()
        );

        legalFile.setTimeReceived(
            request.getTimeReceived()
        );

        legalFile.setDateCompleted(
            request.getDateCompleted()
        );

        legalFile.setContactDetails(
            request.getContactDetails()
        );

        legalFile.setCurrentStage(
            request.getCurrentStage()
        );


        // =========================================
        // CREATED BY
        // =========================================

        legalFile.setCreatedBy(
            currentUser
        );


        // =========================================
        // STATUS
        // =========================================

        if (request.getStatusId() != null) {

            Status status = statusRepository
                    .findById(request.getStatusId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Status not found with id: "
                            + request.getStatusId()
                        )
                    );


            System.out.println(
                "STATUS FOUND = "
                + status.getId()
                + " / "
                + status.getStatusName()
            );


            legalFile.setStatus(status);


            System.out.println(
                "STATUS SET = "
                + legalFile.getStatus().getId()
            );
        }


        // =========================================
        // SPMS TYPE
        // =========================================

        if (request.getSpmsTypeId() != null) {

            SpmsType spmsType =
                    spmsTypeRepository
                        .findById(
                            request.getSpmsTypeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "SPMS Type not found with id: "
                                + request.getSpmsTypeId()
                            )
                        );

            legalFile.setSpmsType(
                spmsType
            );
        }


        // =========================================
        // REQUESTING OFFICE
        // =========================================

        if (request.getRequestingOfficeId() != null) {

            Office office =
                    officeRepository
                        .findById(
                            request.getRequestingOfficeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Office not found with id: "
                                + request.getRequestingOfficeId()
                            )
                        );

            legalFile.setRequestingOffice(
                office
            );
        }


        // =========================================
        // DOCUMENT TYPE
        // =========================================

        if (request.getDocumentTypeId() != null) {

            DocumentType documentType =
                    documentTypeRepository
                        .findById(
                            request.getDocumentTypeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Document Type not found with id: "
                                + request.getDocumentTypeId()
                            )
                        );

            legalFile.setDocumentType(
                documentType
            );
        }


        // =========================================
        // DOCUMENT FORMAT
        // =========================================

        if (request.getDocumentFormatId() != null) {

            DocumentFormat documentFormat =
                    documentFormatRepository
                        .findById(
                            request.getDocumentFormatId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Document Format not found with id: "
                                + request.getDocumentFormatId()
                            )
                        );

            legalFile.setDocumentFormat(
                documentFormat
            );
        }


        // =========================================
        // DEBUG BEFORE SAVE
        // =========================================

        System.out.println(
            "STATUS BEFORE SAVE = "
            + (
                legalFile.getStatus() != null
                    ? legalFile.getStatus().getId()
                    : null
            )
        );


        // =========================================
        // SAVE LEGAL FILE
        // =========================================

        LegalFile savedLegalFile =
                legalFileRepository.save(
                    legalFile
                );


        // =========================================
        // CREATE FILE ACTION
        // =========================================

        createFileAction(
            savedLegalFile,
            "FILE CREATED",
            null,
            savedLegalFile.getCurrentStage(),
            "Legal file created",
            currentUser
        );


        return savedLegalFile;
    }


    // =========================================
    // UPDATE COMPLETE LEGAL FILE
    // =========================================

    public LegalFile updateLegalFile(
            Long id,
            LegalFileRequest request) {


        // =========================================
        // FIND EXISTING FILE
        // =========================================

        LegalFile existingLegalFile =
                getLegalFileById(id);


        // =========================================
        // BASIC INFORMATION
        // =========================================

        existingLegalFile.setCaseNo(
            request.getCaseNo()
        );

        existingLegalFile.setDateReceived(
            request.getDateReceived()
        );

        existingLegalFile.setTimeReceived(
            request.getTimeReceived()
        );

        existingLegalFile.setDateCompleted(
            request.getDateCompleted()
        );


        // =========================================
        // OTHER INFORMATION
        // =========================================

        existingLegalFile.setContactDetails(
            request.getContactDetails()
        );

        existingLegalFile.setCurrentStage(
            request.getCurrentStage()
        );


        // =========================================
        // STATUS
        // =========================================

        if (request.getStatusId() != null) {

            Status status =
                    statusRepository
                        .findById(
                            request.getStatusId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Status not found with id: "
                                + request.getStatusId()
                            )
                        );

            existingLegalFile.setStatus(
                status
            );
        }


        // =========================================
        // SPMS TYPE
        // =========================================

        if (request.getSpmsTypeId() != null) {

            SpmsType spmsType =
                    spmsTypeRepository
                        .findById(
                            request.getSpmsTypeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "SPMS Type not found with id: "
                                + request.getSpmsTypeId()
                            )
                        );

            existingLegalFile.setSpmsType(
                spmsType
            );
        }


        // =========================================
        // REQUESTING OFFICE
        // =========================================

        if (request.getRequestingOfficeId() != null) {

            Office office =
                    officeRepository
                        .findById(
                            request.getRequestingOfficeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Office not found with id: "
                                + request.getRequestingOfficeId()
                            )
                        );

            existingLegalFile.setRequestingOffice(
                office
            );
        }


        // =========================================
        // DOCUMENT TYPE
        // =========================================

        if (request.getDocumentTypeId() != null) {

            DocumentType documentType =
                    documentTypeRepository
                        .findById(
                            request.getDocumentTypeId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Document Type not found with id: "
                                + request.getDocumentTypeId()
                            )
                        );

            existingLegalFile.setDocumentType(
                documentType
            );
        }


        // =========================================
        // DOCUMENT FORMAT
        // =========================================

        if (request.getDocumentFormatId() != null) {

            DocumentFormat documentFormat =
                    documentFormatRepository
                        .findById(
                            request.getDocumentFormatId()
                        )
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Document Format not found with id: "
                                + request.getDocumentFormatId()
                            )
                        );

            existingLegalFile.setDocumentFormat(
                documentFormat
            );
        }


        // =========================================
        // SAVE
        // =========================================

        return legalFileRepository.save(
            existingLegalFile
        );
    }


    // =========================================
    // UPDATE STATUS ONLY
    // =========================================

    public LegalFile updateStatus(
            Long id,
            Long statusId) {


        // =========================================
        // FIND LEGAL FILE
        // =========================================

        LegalFile legalFile =
                getLegalFileById(id);


        // =========================================
        // GET CURRENT USER
        // =========================================

        Users currentUser =
                getCurrentUser();


        // =========================================
        // VALIDATE STATUS ID
        // =========================================

        if (statusId == null) {

            throw new RuntimeException(
                "Status ID cannot be null"
            );
        }


        // =========================================
        // GET OLD STATUS
        // =========================================

        String oldStatus =
                legalFile.getStatus() != null
                    ? legalFile.getStatus().getStatusName()
                    : "NONE";


        // =========================================
        // FIND NEW STATUS
        // =========================================

        Status status =
                statusRepository
                    .findById(statusId)
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Status not found with id: "
                            + statusId
                        )
                    );


        // =========================================
        // SET NEW STATUS
        // =========================================

        legalFile.setStatus(
            status
        );


        // =========================================
        // DEBUG
        // =========================================

        System.out.println(
            "UPDATING LEGAL FILE ID = "
            + id
        );

        System.out.println(
            "OLD STATUS = "
            + oldStatus
        );

        System.out.println(
            "NEW STATUS ID = "
            + status.getId()
        );

        System.out.println(
            "NEW STATUS NAME = "
            + status.getStatusName()
        );

        System.out.println(
            "UPDATED BY = "
            + currentUser.getUsername()
        );


        // =========================================
        // SAVE LEGAL FILE
        // =========================================

        LegalFile savedLegalFile =
                legalFileRepository.save(
                    legalFile
                );


        // =========================================
        // CREATE STATUS ACTION
        // =========================================

        createFileAction(
            savedLegalFile,
            "STATUS CHANGED",
            null,
            null,
            "Status changed from "
                + oldStatus
                + " to "
                + status.getStatusName(),
            currentUser
        );


        return savedLegalFile;
    }


    // =========================================
    // DELETE LEGAL FILE
    // =========================================

    public void deleteLegalFile(Long id) {

        if (!legalFileRepository.existsById(id)) {

            throw new RuntimeException(
                "Legal file not found with id: "
                + id
            );
        }

        legalFileRepository.deleteById(id);
    }
}