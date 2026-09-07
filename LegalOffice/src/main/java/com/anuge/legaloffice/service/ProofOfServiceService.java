package com.anuge.legaloffice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.legaloffice.dto.ProofOfServiceRequest;
import com.anuge.legaloffice.dto.ProofOfServiceResponse;
import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.ProofOfService;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileActionRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.ProofOfServiceRepository;
import com.anuge.legaloffice.repository.UserRepository;


@Service
public class ProofOfServiceService {


    private final ProofOfServiceRepository proofOfServiceRepository;

    private final LegalFileRepository legalFileRepository;

    private final UserRepository userRepository;

    private final FileActionRepository fileActionRepository;


    public ProofOfServiceService(
            ProofOfServiceRepository proofOfServiceRepository,
            LegalFileRepository legalFileRepository,
            UserRepository userRepository,
            FileActionRepository fileActionRepository) {

        this.proofOfServiceRepository = proofOfServiceRepository;
        this.legalFileRepository = legalFileRepository;
        this.userRepository = userRepository;
        this.fileActionRepository = fileActionRepository;
    }


    // =========================================================
    // CREATE PROOF OF SERVICE
    // =========================================================

    @Transactional
    public ProofOfServiceResponse create(
            ProofOfServiceRequest request) {


        // -----------------------------------------------------
        // VALIDATE FILE ID
        // -----------------------------------------------------

        if (request.getFileId() == null) {

            throw new RuntimeException(
                "File ID is required."
            );
        }


        // -----------------------------------------------------
        // FIND LEGAL FILE
        // -----------------------------------------------------

        LegalFile legalFile =
            legalFileRepository.findById(request.getFileId())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Legal file not found: "
                        + request.getFileId()
                    )
                );


        // -----------------------------------------------------
        // CHECK CURRENT STAGE
        // -----------------------------------------------------

        if (!"RESOLVED".equalsIgnoreCase(
                legalFile.getCurrentStage())) {

            throw new RuntimeException(
                "Proof of Service can only be submitted "
                + "for resolved files."
            );
        }


        // -----------------------------------------------------
        // GET CURRENT LOGGED-IN USER
        // -----------------------------------------------------

        Authentication authentication =
            SecurityContextHolder
                .getContext()
                .getAuthentication();


        if (authentication == null ||
            !authentication.isAuthenticated()) {

            throw new RuntimeException(
                "User is not authenticated."
            );
        }


        Users currentUser =
            userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Current user not found."
                    )
                );


        // -----------------------------------------------------
        // VALIDATE STATUS
        // -----------------------------------------------------

        String status = request.getStatus();

        if (status == null || status.isBlank()) {
            status = "SUBMITTED";
        }

        status = status.toUpperCase();


        if (!status.equals("PENDING") &&
            !status.equals("SUBMITTED")) {

            throw new RuntimeException(
                "Invalid Proof of Service status: "
                + status
            );
        }


        // -----------------------------------------------------
        // CREATE PROOF OF SERVICE
        // -----------------------------------------------------

        ProofOfService proof =
            new ProofOfService();

        proof.setLegalFile(legalFile);

        proof.setStatus(status);

        proof.setProofFilePath(
            request.getProofFilePath()
        );

        proof.setSubmittedBy(currentUser);

        proof.setSubmittedAt(
            LocalDateTime.now()
        );

        proof.setRemarks(
            request.getRemarks()
        );


        ProofOfService saved =
            proofOfServiceRepository.save(proof);


        // =====================================================
        // WORKFLOW
        // =====================================================

        if ("SUBMITTED".equals(status)) {

            String oldStage =
                legalFile.getCurrentStage();

            legalFile.setCurrentStage("OUT");

            legalFileRepository.save(legalFile);


            // -------------------------------------------------
            // FILE ACTION
            // -------------------------------------------------

            FileAction action =
                new FileAction();

            action.setLegalFile(legalFile);

            action.setAction(
                "PROOF OF SERVICE SUBMITTED"
            );

            action.setFromStage(oldStage);

            action.setToStage("OUT");

            action.setRemarks(
                request.getRemarks()
            );

            action.setPerformedBy(currentUser);

            action.setPerformedAt(
                LocalDateTime.now()
            );

            fileActionRepository.save(action);
        }


        return mapToResponse(saved);
    }


    // =========================================================
    // GET BY FILE ID
    // =========================================================

    @Transactional(readOnly = true)
    public List<ProofOfServiceResponse> getByFileId(
            Long fileId) {

        return proofOfServiceRepository
            .findByLegalFileIdOrderBySubmittedAtDesc(fileId)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }


    // =========================================================
    // MAP RESPONSE
    // =========================================================

    private ProofOfServiceResponse mapToResponse(
            ProofOfService proof) {


        ProofOfServiceResponse response =
            new ProofOfServiceResponse();


        response.setId(
            proof.getId()
        );


        response.setFileId(
            proof.getLegalFile().getId()
        );


        response.setCaseNo(
            proof.getLegalFile().getCaseNo()
        );


        response.setStatus(
            proof.getStatus()
        );


        response.setProofFilePath(
            proof.getProofFilePath()
        );


        if (proof.getSubmittedBy() != null) {

            response.setSubmittedBy(
                proof.getSubmittedBy().getId()
            );

            response.setSubmittedByName(
                proof.getSubmittedBy().getFullName()
            );
        }


        response.setSubmittedAt(
            proof.getSubmittedAt()
        );


        response.setRemarks(
            proof.getRemarks()
        );


        return response;
    }
}