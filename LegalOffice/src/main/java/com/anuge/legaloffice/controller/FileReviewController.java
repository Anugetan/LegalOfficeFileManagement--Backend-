package com.anuge.legaloffice.controller;


import com.anuge.legaloffice.dto.CreateFileReviewRequest;
import com.anuge.legaloffice.dto.FileReviewResponse;
import com.anuge.legaloffice.service.FileReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/file-reviews")
public class FileReviewController {

    private final FileReviewService fileReviewService;


    public FileReviewController(
            FileReviewService fileReviewService
    ) {
        this.fileReviewService = fileReviewService;
    }


    // =====================================================
    // CREATE REVIEW
    // =====================================================

    @PostMapping
    public ResponseEntity<FileReviewResponse> createReview(
            @RequestBody CreateFileReviewRequest request
    ) {

        FileReviewResponse response =
                fileReviewService.createReview(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET ALL REVIEWS FOR FILE
    // =====================================================

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileReviewResponse>>
    getReviewsByFile(
            @PathVariable Long fileId
    ) {

        return ResponseEntity.ok(
                fileReviewService.getReviewsByFile(fileId)
        );
    }


    // =====================================================
    // GET REVIEWS BY TYPE
    // =====================================================

    @GetMapping("/file/{fileId}/type/{reviewType}")
    public ResponseEntity<List<FileReviewResponse>>
    getReviewsByType(
            @PathVariable Long fileId,
            @PathVariable String reviewType
    ) {

        return ResponseEntity.ok(
                fileReviewService.getReviewsByType(
                        fileId,
                        reviewType
                )
        );
    }
}