package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.dto.LegalFileRequest;
import com.anuge.legaloffice.dto.LegalFileResponse;
import com.anuge.legaloffice.dto.StatusUpdateRequest;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.service.LegalFileService;

@RestController
@RequestMapping("/api/legal-files")
public class LegalFileController {

    private final LegalFileService legalFileService;


    public LegalFileController(
            LegalFileService legalFileService) {

        this.legalFileService = legalFileService;
    }


    // =========================================
    // GET ALL LEGAL FILES
    // =========================================

    @GetMapping
    public ResponseEntity<List<LegalFileResponse>>
            getAllLegalFiles() {

        List<LegalFileResponse> response =
                legalFileService
                        .getAllLegalFiles()
                        .stream()
                        .map(LegalFileResponse::new)
                        .toList();

        return ResponseEntity.ok(response);
    }


    // =========================================
    // GET LEGAL FILE BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<LegalFileResponse>
            getLegalFileById(
                    @PathVariable Long id) {

        LegalFile legalFile =
                legalFileService.getLegalFileById(id);

        return ResponseEntity.ok(
            new LegalFileResponse(legalFile)
        );
    }


    // =========================================
    // GET LEGAL FILE BY CASE NUMBER
    // =========================================

    @GetMapping("/case/{caseNo}")
    public ResponseEntity<LegalFileResponse>
            getLegalFileByCaseNo(
                    @PathVariable String caseNo) {

        LegalFile legalFile =
                legalFileService
                    .getLegalFileByCaseNo(caseNo);

        return ResponseEntity.ok(
            new LegalFileResponse(legalFile)
        );
    }


    // =========================================
    // CREATE LEGAL FILE
    // =========================================

    @PostMapping
    public ResponseEntity<LegalFileResponse>
            createLegalFile(
                    @RequestBody LegalFileRequest request) {

        LegalFile legalFile =
                legalFileService
                    .createLegalFile(request);

        return ResponseEntity.ok(
            new LegalFileResponse(legalFile)
        );
    }


    // =========================================
    // UPDATE COMPLETE LEGAL FILE
    // =========================================

    @PutMapping("/{id}")
    public ResponseEntity<LegalFileResponse>
            updateLegalFile(
                    @PathVariable Long id,
                    @RequestBody LegalFileRequest request) {

        LegalFile legalFile =
                legalFileService
                    .updateLegalFile(id, request);

        return ResponseEntity.ok(
            new LegalFileResponse(legalFile)
        );
    }


    // =========================================
    // UPDATE STATUS ONLY
    // =========================================

    @PutMapping("/{id}/status")
    public ResponseEntity<LegalFileResponse>
            updateStatus(
                    @PathVariable Long id,
                    @RequestBody StatusUpdateRequest request) {

        LegalFile legalFile =
                legalFileService.updateStatus(
                    id,
                    request.getStatusId()
                );

        return ResponseEntity.ok(
            new LegalFileResponse(legalFile)
        );
    }


    // =========================================
    // DELETE LEGAL FILE
    // =========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteLegalFile(
                    @PathVariable Long id) {

        legalFileService.deleteLegalFile(id);

        return ResponseEntity.noContent().build();
    }
}