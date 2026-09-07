package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.dto.ProofOfServiceRequest;
import com.anuge.legaloffice.dto.ProofOfServiceResponse;
import com.anuge.legaloffice.service.ProofOfServiceService;

@RestController
@RequestMapping("/api/proof-of-service")
public class ProofOfServiceController {


    private final ProofOfServiceService proofOfServiceService;


    public ProofOfServiceController(
            ProofOfServiceService proofOfServiceService) {

        this.proofOfServiceService =
            proofOfServiceService;
    }


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<ProofOfServiceResponse> create(
            @RequestBody ProofOfServiceRequest request) {

        return ResponseEntity.ok(
            proofOfServiceService.create(request)
        );
    }


    // =========================================================
    // GET BY LEGAL FILE
    // =========================================================

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<ProofOfServiceResponse>>
        getByFileId(
            @PathVariable Long fileId) {

        return ResponseEntity.ok(
            proofOfServiceService.getByFileId(fileId)
        );
    }
}