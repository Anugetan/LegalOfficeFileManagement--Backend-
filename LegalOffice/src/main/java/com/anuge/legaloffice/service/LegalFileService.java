package com.anuge.legaloffice.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

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

    // =========================================================
    // GET CURRENT AUTHENTICATED USER
    // =========================================================

    private Users getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().equals("anonymousUser")) {

            throw new RuntimeException(
                    "No authenticated user found"
            );
        }

        String username = authentication.getName();

        return usersRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found: "
                                        + username
                        )
                );
    }

    // =========================================================
    // CREATE FILE ACTION / AUDIT LOG
    // =========================================================

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

    // =========================================================
    // GET ALL LEGAL FILES
    // =========================================================

    public List<LegalFile> getAllLegalFiles() {

        return legalFileRepository.findAll();
    }

    // =========================================================
    // GET LEGAL FILE BY ID
    // =========================================================

    public LegalFile getLegalFileById(Long id) {

        if (id == null) {

            throw new RuntimeException(
                    "Legal file ID cannot be null"
            );
        }

        return legalFileRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Legal file not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // GET LEGAL FILE BY CASE NUMBER
    // =========================================================

    public LegalFile getLegalFileByCaseNo(String caseNo) {

        if (caseNo == null || caseNo.isBlank()) {

            throw new RuntimeException(
                    "Case number is required"
            );
        }

        return legalFileRepository
                .findByCaseNo(caseNo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Legal file not found with case number: "
                                        + caseNo
                        )
                );
    }

    // =========================================================
    // CREATE LEGAL FILE
    // =========================================================

    @Transactional
    public LegalFile createLegalFile(
            LegalFileRequest request) {

        // -----------------------------------------------------
        // VALIDATE REQUEST
        // -----------------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "Legal file request cannot be null"
            );
        }

        // -----------------------------------------------------
        // GET CURRENT USER
        // -----------------------------------------------------

        Users currentUser = getCurrentUser();

        // -----------------------------------------------------
        // VALIDATE CASE NUMBER
        // -----------------------------------------------------

        if (request.getCaseNo() == null
                || request.getCaseNo().isBlank()) {

            throw new RuntimeException(
                    "Case number is required"
            );
        }

        String caseNo = request.getCaseNo().trim();

        // -----------------------------------------------------
        // CHECK DUPLICATE CASE NUMBER
        // -----------------------------------------------------

        if (legalFileRepository.existsByCaseNo(caseNo)) {

            throw new RuntimeException(
                    "Case number already exists: "
                            + caseNo
            );
        }

        // -----------------------------------------------------
        // CREATE ENTITY
        // -----------------------------------------------------

        LegalFile legalFile = new LegalFile();

        // -----------------------------------------------------
        // BASIC INFORMATION
        // -----------------------------------------------------

        legalFile.setCaseNo(caseNo);

        legalFile.setDateReceived(
                request.getDateReceived()
        );

        legalFile.setTimeReceived(
                request.getTimeReceived()
        );

        /*
         * A newly created file should not have a completion
         * date yet.
         *
         * The date will be automatically assigned when
         * FINAL REVIEW is APPROVED.
         */
        legalFile.setDateCompleted(null);

        legalFile.setContactDetails(
                request.getContactDetails()
        );

        // -----------------------------------------------------
        // CURRENT STAGE
        // -----------------------------------------------------

        /*
         * IMPORTANT:
         *
         * Stage is controlled by the backend workflow.
         *
         * A newly created file always starts at RECEIVED.
         *
         * The frontend cannot create a file directly as
         * FINAL_REVIEW or RESOLVED.
         */
        legalFile.setCurrentStage("RECEIVED");

        // -----------------------------------------------------
        // CREATED BY
        // -----------------------------------------------------

        legalFile.setCreatedBy(currentUser);

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        /*
         * New files always start as PENDING.
         *
         * The frontend cannot choose RESOLVED when creating
         * a new file.
         */
        Status pendingStatus =
                statusRepository
                        .findById(1L)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Default PENDING status not found with id: 1"
                                )
                        );

        legalFile.setStatus(pendingStatus);

        // -----------------------------------------------------
        // SPMS TYPE
        // -----------------------------------------------------

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

            legalFile.setSpmsType(spmsType);
        }

        // -----------------------------------------------------
        // REQUESTING OFFICE
        // -----------------------------------------------------

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

            legalFile.setRequestingOffice(office);
        }

        // -----------------------------------------------------
        // DOCUMENT TYPE
        // -----------------------------------------------------

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

            legalFile.setDocumentType(documentType);
        }

        // -----------------------------------------------------
        // DOCUMENT FORMAT
        // -----------------------------------------------------

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

            legalFile.setDocumentFormat(documentFormat);
        }

        // -----------------------------------------------------
        // SAVE LEGAL FILE
        // -----------------------------------------------------

        LegalFile savedLegalFile =
                legalFileRepository.save(legalFile);

        // -----------------------------------------------------
        // CREATE AUDIT ACTION
        // -----------------------------------------------------

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

    // =========================================================
    // UPDATE LEGAL FILE INFORMATION
    // =========================================================

    @Transactional
    public LegalFile updateLegalFile(
            Long id,
            LegalFileRequest request) {

        // -----------------------------------------------------
        // VALIDATE REQUEST
        // -----------------------------------------------------

        if (request == null) {

            throw new RuntimeException(
                    "Legal file request cannot be null"
            );
        }

        // -----------------------------------------------------
        // GET CURRENT USER
        // -----------------------------------------------------

        Users currentUser = getCurrentUser();

        // -----------------------------------------------------
        // GET EXISTING FILE
        // -----------------------------------------------------

        LegalFile existingLegalFile =
                getLegalFileById(id);

        // -----------------------------------------------------
        // VALIDATE CASE NUMBER
        // -----------------------------------------------------

        if (request.getCaseNo() == null
                || request.getCaseNo().isBlank()) {

            throw new RuntimeException(
                    "Case number is required"
            );
        }

        String newCaseNo =
                request.getCaseNo().trim();

        // -----------------------------------------------------
        // CHECK DUPLICATE CASE NUMBER
        // -----------------------------------------------------

        if (!existingLegalFile.getCaseNo()
                .equals(newCaseNo)
                && legalFileRepository
                        .existsByCaseNo(newCaseNo)) {

            throw new RuntimeException(
                    "Case number already exists: "
                            + newCaseNo
            );
        }

        // -----------------------------------------------------
        // REMEMBER CURRENT WORKFLOW STATE
        // -----------------------------------------------------

        String currentStage =
                existingLegalFile.getCurrentStage();

        String currentStatus =
                existingLegalFile.getStatus() != null
                        ? existingLegalFile
                                .getStatus()
                                .getStatusName()
                        : "NONE";

        // -----------------------------------------------------
        // UPDATE BASIC INFORMATION ONLY
        // -----------------------------------------------------

        existingLegalFile.setCaseNo(newCaseNo);

        existingLegalFile.setDateReceived(
                request.getDateReceived()
        );

        existingLegalFile.setTimeReceived(
                request.getTimeReceived()
        );

        existingLegalFile.setContactDetails(
                request.getContactDetails()
        );

        // -----------------------------------------------------
        // DO NOT UPDATE CURRENT STAGE
        // -----------------------------------------------------

        /*
         * currentStage is intentionally NOT taken from:
         *
         * request.getCurrentStage()
         *
         * The review workflow controls currentStage.
         */

        // -----------------------------------------------------
        // DO NOT UPDATE STATUS
        // -----------------------------------------------------

        /*
         * status is intentionally NOT taken from:
         *
         * request.getStatusId()
         *
         * FileReviewService controls the overall status.
         */

        // -----------------------------------------------------
        // SPMS TYPE
        // -----------------------------------------------------

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

            existingLegalFile.setSpmsType(spmsType);
        }

        // -----------------------------------------------------
        // REQUESTING OFFICE
        // -----------------------------------------------------

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

            existingLegalFile.setRequestingOffice(office);
        }

        // -----------------------------------------------------
        // DOCUMENT TYPE
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // DOCUMENT FORMAT
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        LegalFile savedLegalFile =
                legalFileRepository.save(
                        existingLegalFile
                );

        // -----------------------------------------------------
        // CREATE AUDIT ACTION
        // -----------------------------------------------------

        createFileAction(
                savedLegalFile,
                "FILE UPDATED",
                currentStage,
                currentStage,
                "Legal file information updated. "
                        + "Stage: "
                        + currentStage
                        + " | Status: "
                        + currentStatus,
                currentUser
        );

        return savedLegalFile;
    }

    // =========================================================
    // DELETE LEGAL FILE
    // =========================================================

    @Transactional
    public void deleteLegalFile(Long id) {

        // -----------------------------------------------------
        // GET FILE
        // -----------------------------------------------------

        LegalFile legalFile =
                getLegalFileById(id);

        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        legalFileRepository.delete(legalFile);
    }
}
