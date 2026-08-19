package com.anuge.webWord.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.webWord.model.Document;
import com.anuge.webWord.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/open")
    public Document openDocument() throws IOException {

        System.out.println("Docs Read");

        String filePath =  "\\\\win2012-server\\ee_dev\\jonathan\\task\\Words\\EditMe.docx";

        return documentService.readDocument(filePath);
    }
}