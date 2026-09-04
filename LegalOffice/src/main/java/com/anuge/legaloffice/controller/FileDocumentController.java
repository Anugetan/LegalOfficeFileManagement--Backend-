package com.anuge.legaloffice.controller;

import com.anuge.legaloffice.dto.FileUploadResponse;
import com.anuge.legaloffice.service.FileDocumentService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file-documents")
public class FileDocumentController {
    

    private final FileDocumentService fileDocumentService;

    public FileDocumentController(
            FileDocumentService fileDocumentService
    ) {
        this.fileDocumentService =
                fileDocumentService;
    }

    // UPLOAD DOCUMENT

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileUploadResponse> uploadFile(

            @RequestParam Long fileId,

            @RequestParam(required = false)
            Long documentFormatId,

            @RequestParam MultipartFile file,

            Authentication authentication
    ) throws Exception {

        String username =
                authentication.getName();

        FileUploadResponse response =
                fileDocumentService.uploadFile(
                        fileId,
                        documentFormatId,
                        file,
                        username
                );

        return ResponseEntity.ok(response);
    }



    // GET DOCUMENTS BY LEGAL FILE

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileUploadResponse>>
    getDocumentsByFileId(
            @PathVariable Long fileId
    ) {

        List<FileUploadResponse> documents =
                fileDocumentService.getDocumentsByFileId(
                        fileId
                );

        return ResponseEntity.ok(documents);
    }
}
