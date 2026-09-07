package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.dto.InitialReviewRequest;
import com.anuge.legaloffice.dto.InitialReviewResponse;
import com.anuge.legaloffice.entity.InitialReview;
import com.anuge.legaloffice.service.InitialReviewService;

@RestController
@RequestMapping("/api/initial-reviews")
public class InitialReviewController {

    private final InitialReviewService initialReviewService;

    public InitialReviewController(
            InitialReviewService initialReviewService) {

        this.initialReviewService =
                initialReviewService;
    }

    @PostMapping
    public ResponseEntity<InitialReviewResponse>
            createInitialReview(
                    @RequestBody InitialReviewRequest request) {

        InitialReview review =
                initialReviewService
                        .createInitialReview(request);

        return ResponseEntity.ok(
                new InitialReviewResponse(review)
        );
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<InitialReviewResponse>>
            getReviewsByFile(
                    @PathVariable Long fileId) {

        return ResponseEntity.ok(
                initialReviewService
                        .getReviewsByFile(fileId)
        );
    }
}