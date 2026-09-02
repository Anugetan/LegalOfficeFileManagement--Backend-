package com.anuge.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.wallet.dto.TransactionRequest;
import com.anuge.wallet.entity.TransactionEntity;
import com.anuge.wallet.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }


    // =========================================================
    // CREATE TRANSACTION
    // =========================================================

    @PostMapping
    public ResponseEntity<TransactionEntity> createTransaction(
            Authentication authentication,
            @RequestBody TransactionRequest request) {

        String username = authentication.getName();

        TransactionEntity transaction =
                transactionService.createTransaction(
                        username,
                        request
                );

        return ResponseEntity.ok(transaction);
    }
}