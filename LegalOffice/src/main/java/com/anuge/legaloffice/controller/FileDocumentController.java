package com.anuge.legaloffice.controller;

import com.anuge.legaloffice.dto.FileUploadResponse;
import com.anuge.legaloffice.entity.FileDocument;
import com.anuge.legaloffice.service.FileDocumentService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
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


    // ==========================================
    // UPLOAD DOCUMENT
    // ==========================================

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


    // ==========================================
    // GET DOCUMENTS BY LEGAL FILE
    // ==========================================

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


    // ==========================================
    // DOWNLOAD DOCUMENT
    // ==========================================

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long documentId
    ) throws Exception {

        // Get document from database
        FileDocument document =
                fileDocumentService.getDocumentById(
                        documentId
                );


        // Get stored file path
        Path filePath =
                Paths.get(
                        document.getFilePath()
                );


        // Convert path to Resource
        Resource resource =
                new UrlResource(
                        filePath.toUri()
                );


        // Check if file exists
        if (
                !resource.exists() ||
                !resource.isReadable()
        ) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // Return file for download
        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                document.getDocumentName() +
                                "\""
                )

                .contentType(
                        MediaType.parseMediaType(
                                document.getMimeType()
                        )
                )

                .body(resource);
    }
    
		 // ==========================================
		 // REPLACE EXISTING DOCUMENT
		 // ==========================================

		 @PutMapping(
		         value = "/{documentId}/replace",
		         consumes = MediaType.MULTIPART_FORM_DATA_VALUE
		 )
		 public ResponseEntity<FileUploadResponse> replaceFile(
		         @PathVariable Long documentId,
		         @RequestParam MultipartFile file,
		         Authentication authentication
		 ) throws Exception {
		
		     String username =
		             authentication.getName();
		
		     FileUploadResponse response =
		             fileDocumentService.replaceFile(
		                     documentId,
		                     file,
		                     username
		             );
		
		     return ResponseEntity.ok(response);
		 }
}
