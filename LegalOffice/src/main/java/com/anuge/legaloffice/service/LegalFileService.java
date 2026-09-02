package com.anuge.legaloffice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anuge.legaloffice.dto.LegalFileRequest;
import com.anuge.legaloffice.entity.DocumentFormat;
import com.anuge.legaloffice.entity.DocumentType;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Office;
import com.anuge.legaloffice.entity.SpmsType;
import com.anuge.legaloffice.entity.Status;
import com.anuge.legaloffice.repository.DocumentFormatRepository;
import com.anuge.legaloffice.repository.DocumentTypeRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.OfficeRepository;
import com.anuge.legaloffice.repository.SpmsTypeRepository;
import com.anuge.legaloffice.repository.StatusRepository;

@Service
public class LegalFileService {

    private final LegalFileRepository legalFileRepository;
    private final StatusRepository statusRepository;
    private final SpmsTypeRepository spmsTypeRepository;
    private final OfficeRepository officeRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentFormatRepository documentFormatRepository;


    public LegalFileService(
            LegalFileRepository legalFileRepository,
            StatusRepository statusRepository,
            SpmsTypeRepository spmsTypeRepository,
            OfficeRepository officeRepository,
            DocumentTypeRepository documentTypeRepository,
            DocumentFormatRepository documentFormatRepository) {

        this.legalFileRepository = legalFileRepository;
        this.statusRepository = statusRepository;
        this.spmsTypeRepository = spmsTypeRepository;
        this.officeRepository = officeRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentFormatRepository = documentFormatRepository;
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
                        "Legal file not found with case number: " + caseNo
                    )
                );
    }


    // =========================================
    // CREATE LEGAL FILE
    // =========================================

    public LegalFile createLegalFile(LegalFileRequest request) {

        System.out.println(
            "STATUS ID FROM REQUEST = " + request.getStatusId()
        );


        // CHECK DUPLICATE CASE NUMBER

        if (legalFileRepository.existsByCaseNo(request.getCaseNo())) {

            throw new RuntimeException(
                "Case number already exists: " + request.getCaseNo()
            );
        }


        LegalFile legalFile = new LegalFile();


        // =========================================
        // BASIC DATA
        // =========================================

        legalFile.setCaseNo(request.getCaseNo());
        legalFile.setDateReceived(request.getDateReceived());
        legalFile.setTimeReceived(request.getTimeReceived());
        legalFile.setDateCompleted(request.getDateCompleted());
        legalFile.setContactDetails(request.getContactDetails());
        legalFile.setCurrentStage(request.getCurrentStage());


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

            SpmsType spmsType = spmsTypeRepository
                    .findById(request.getSpmsTypeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "SPMS Type not found with id: "
                            + request.getSpmsTypeId()
                        )
                    );

            legalFile.setSpmsType(spmsType);
        }


        // =========================================
        // REQUESTING OFFICE
        // =========================================

        if (request.getRequestingOfficeId() != null) {

            Office office = officeRepository
                    .findById(request.getRequestingOfficeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Office not found with id: "
                            + request.getRequestingOfficeId()
                        )
                    );

            legalFile.setRequestingOffice(office);
        }


        // =========================================
        // DOCUMENT TYPE
        // =========================================

        if (request.getDocumentTypeId() != null) {

            DocumentType documentType = documentTypeRepository
                    .findById(request.getDocumentTypeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Document Type not found with id: "
                            + request.getDocumentTypeId()
                        )
                    );

            legalFile.setDocumentType(documentType);
        }


        // =========================================
        // DOCUMENT FORMAT
        // =========================================

        if (request.getDocumentFormatId() != null) {

            DocumentFormat documentFormat = documentFormatRepository
                    .findById(request.getDocumentFormatId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Document Format not found with id: "
                            + request.getDocumentFormatId()
                        )
                    );

            legalFile.setDocumentFormat(documentFormat);
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


        return legalFileRepository.save(legalFile);
    }


    // =========================================
    // UPDATE COMPLETE LEGAL FILE
    // =========================================

    public LegalFile updateLegalFile(
            Long id,
            LegalFileRequest request) {

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

            Status status = statusRepository
                    .findById(request.getStatusId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Status not found with id: "
                            + request.getStatusId()
                        )
                    );

            existingLegalFile.setStatus(status);
        }


        // =========================================
        // SPMS TYPE
        // =========================================

        if (request.getSpmsTypeId() != null) {

            SpmsType spmsType = spmsTypeRepository
                    .findById(request.getSpmsTypeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "SPMS Type not found with id: "
                            + request.getSpmsTypeId()
                        )
                    );

            existingLegalFile.setSpmsType(spmsType);
        }


        // =========================================
        // REQUESTING OFFICE
        // =========================================

        if (request.getRequestingOfficeId() != null) {

            Office office = officeRepository
                    .findById(request.getRequestingOfficeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Office not found with id: "
                            + request.getRequestingOfficeId()
                        )
                    );

            existingLegalFile.setRequestingOffice(office);
        }


        // =========================================
        // DOCUMENT TYPE
        // =========================================

        if (request.getDocumentTypeId() != null) {

            DocumentType documentType = documentTypeRepository
                    .findById(request.getDocumentTypeId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Document Type not found with id: "
                            + request.getDocumentTypeId()
                        )
                    );

            existingLegalFile.setDocumentType(documentType);
        }


        // =========================================
        // DOCUMENT FORMAT
        // =========================================

        if (request.getDocumentFormatId() != null) {

            DocumentFormat documentFormat = documentFormatRepository
                    .findById(request.getDocumentFormatId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Document Format not found with id: "
                            + request.getDocumentFormatId()
                        )
                    );

            existingLegalFile.setDocumentFormat(documentFormat);
        }


        return legalFileRepository.save(existingLegalFile);
    }


    // =========================================
    // UPDATE STATUS ONLY
    // =========================================

    public LegalFile updateStatus(
            Long id,
            Long statusId) {

        // FIND LEGAL FILE

        LegalFile legalFile =
                getLegalFileById(id);


        // VALIDATE STATUS ID

        if (statusId == null) {

            throw new RuntimeException(
                "Status ID cannot be null"
            );
        }


        // FIND STATUS

        Status status = statusRepository
                .findById(statusId)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Status not found with id: "
                        + statusId
                    )
                );


        // SET STATUS

        legalFile.setStatus(status);


        // DEBUG

        System.out.println(
            "UPDATING LEGAL FILE ID = " + id
        );

        System.out.println(
            "NEW STATUS ID = " + status.getId()
        );

        System.out.println(
            "NEW STATUS NAME = " + status.getStatusName()
        );


        // SAVE

        return legalFileRepository.save(legalFile);
    }


    // =========================================
    // DELETE LEGAL FILE
    // =========================================

    public void deleteLegalFile(Long id) {

        if (!legalFileRepository.existsById(id)) {

            throw new RuntimeException(
                "Legal file not found with id: " + id
            );
        }

        legalFileRepository.deleteById(id);
    }
}