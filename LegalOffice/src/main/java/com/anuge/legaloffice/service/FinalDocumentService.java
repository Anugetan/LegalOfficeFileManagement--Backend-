package com.anuge.legaloffice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.legaloffice.dto.FinalDocumentRequest;
import com.anuge.legaloffice.dto.FinalDocumentResponse;
import com.anuge.legaloffice.entity.FileAction;
import com.anuge.legaloffice.entity.FinalDocument;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.FileActionRepository;
import com.anuge.legaloffice.repository.FinalDocumentRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;
import com.anuge.legaloffice.repository.UserRepository;



@Service
public class FinalDocumentService {

    private final FinalDocumentRepository finalDocumentRepository;
    private final LegalFileRepository legalFileRepository;
    private final UserRepository userRepository;
    private final FileActionRepository fileActionRepository;

    public FinalDocumentService(
            FinalDocumentRepository finalDocumentRepository,
            LegalFileRepository legalFileRepository,
            UserRepository userRepository,
            FileActionRepository fileActionRepository) {

        this.finalDocumentRepository = finalDocumentRepository;
        this.legalFileRepository = legalFileRepository;
        this.userRepository = userRepository;
        this.fileActionRepository = fileActionRepository;
    }

    // ============================================================
    // CREATE FINAL DOCUMENT
    // ============================================================

    @Transactional
    public FinalDocumentResponse createFinalDocument(
            FinalDocumentRequest request) {

        if (request.getFileId() == null) {
            throw new RuntimeException("File ID is required");
        }

        if (request.getDocumentName() == null ||
                request.getDocumentName().isBlank()) {

            throw new RuntimeException("Document name is required");
        }

        LegalFile legalFile = legalFileRepository
                .findById(request.getFileId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Legal file not found: "
                                        + request.getFileId()));

        // ========================================================
        // FINAL DOCUMENT SHOULD ONLY BE CREATED AFTER RESOLVED
        // ========================================================

        if (!"RESOLVED".equalsIgnoreCase(
                legalFile.getCurrentStage())) {

            throw new RuntimeException(
                    "Final document can only be created when "
                    + "the legal file is RESOLVED.");
        }

        // ========================================================
        // GET CURRENT LOGGED-IN USER
        // ========================================================

        Users currentUser = getCurrentUser();

        FinalDocument document = new FinalDocument();

        document.setLegalFile(legalFile);
        document.setDocumentName(request.getDocumentName());
        document.setFilePath(request.getFilePath());
        document.setFinalizedBy(currentUser);
        document.setFinalizedAt(LocalDateTime.now());
        document.setRemarks(request.getRemarks());

        FinalDocument saved =
                finalDocumentRepository.save(document);

        // ========================================================
        // ACTIVITY HISTORY
        // ========================================================

        FileAction action = new FileAction();

        action.setLegalFile(legalFile);
        action.setAction("FINAL DOCUMENT CREATED");
        action.setFromStage(legalFile.getCurrentStage());
        action.setToStage(legalFile.getCurrentStage());
        action.setRemarks(request.getRemarks());
        action.setPerformedBy(currentUser);
        action.setPerformedAt(LocalDateTime.now());

        fileActionRepository.save(action);

        return toResponse(saved);
    }

    // ============================================================
    // GET FINAL DOCUMENTS BY FILE
    // ============================================================

    public List<FinalDocumentResponse> getByFileId(Long fileId) {

        return finalDocumentRepository
                .findByLegalFileIdOrderByFinalizedAtDesc(fileId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET CURRENT USER
    // ============================================================

    private Users getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated");
        }

        return userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"));
    }

    // ============================================================
    // RESPONSE MAPPER
    // ============================================================

    private FinalDocumentResponse toResponse(
            FinalDocument document) {

        FinalDocumentResponse response =
                new FinalDocumentResponse();

        response.setId(document.getId());

        response.setFileId(
                document.getLegalFile().getId());

        response.setCaseNo(
                document.getLegalFile().getCaseNo());

        response.setDocumentName(
                document.getDocumentName());

        response.setFilePath(
                document.getFilePath());

        if (document.getFinalizedBy() != null) {

            response.setFinalizedBy(
                    document.getFinalizedBy().getId());

            response.setFinalizedByName(
                    document.getFinalizedBy().getFullName());
        }

        response.setFinalizedAt(
                document.getFinalizedAt());

        response.setRemarks(
                document.getRemarks());

        return response;
    }
}