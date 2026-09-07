package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.dto.FinalDocumentRequest;
import com.anuge.legaloffice.dto.FinalDocumentResponse;
import com.anuge.legaloffice.service.FinalDocumentService;

@RestController
@RequestMapping("/api/final-documents")
public class FinalDocumentController {

    private final FinalDocumentService finalDocumentService;

    public FinalDocumentController(
            FinalDocumentService finalDocumentService) {

        this.finalDocumentService = finalDocumentService;
    }

    // ============================================================
    // CREATE FINAL DOCUMENT
    // ============================================================

    @PostMapping
    public ResponseEntity<FinalDocumentResponse> create(
            @RequestBody FinalDocumentRequest request) {

        return ResponseEntity.ok(
                finalDocumentService
                        .createFinalDocument(request));
    }

    // ============================================================
    // GET FINAL DOCUMENTS FOR A LEGAL FILE
    // ============================================================

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FinalDocumentResponse>> getByFileId(
            @PathVariable Long fileId) {

        return ResponseEntity.ok(
                finalDocumentService
                        .getByFileId(fileId));
    }
}