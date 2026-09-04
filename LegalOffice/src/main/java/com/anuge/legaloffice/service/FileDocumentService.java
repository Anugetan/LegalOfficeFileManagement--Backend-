package com.anuge.legaloffice.service;


import com.anuge.legaloffice.dto.FileUploadResponse;
import com.anuge.legaloffice.entity.DocumentFormat;
import com.anuge.legaloffice.entity.FileDocument;
import com.anuge.legaloffice.entity.LegalFile;
import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.repository.DocumentFormatRepository;
import com.anuge.legaloffice.repository.FileDocumentRepository;
import com.anuge.legaloffice.repository.LegalFileRepository;

import com.anuge.legaloffice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class FileDocumentService {

    private final FileDocumentRepository fileDocumentRepository;
    private final LegalFileRepository legalFileRepository;
    private final DocumentFormatRepository documentFormatRepository;
    private final UserRepository userRepository;

    private final Path uploadDirectory = Paths.get("uploads/legal-files");

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20 MB

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "image/jpeg",
            "image/png"
    );

    public FileDocumentService(
            FileDocumentRepository fileDocumentRepository,
            LegalFileRepository legalFileRepository,
            DocumentFormatRepository documentFormatRepository,
            UserRepository userRepository
    ) {
        this.fileDocumentRepository = fileDocumentRepository;
        this.legalFileRepository = legalFileRepository;
        this.documentFormatRepository = documentFormatRepository;
        this.userRepository = userRepository;
    }
    
    public FileUploadResponse uploadFile(
            Long fileId,
            Long documentFormatId,
            MultipartFile file,
            String username
    ) throws IOException {

        // 1. Validate file
        validateFile(file);

        // 2. Find legal file
        LegalFile legalFile = legalFileRepository
                .findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Legal file not found: " + fileId
                        )
                );

        // 3. Find logged-in user
        Users user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username
                        )
                );

        // 4. Find document format
        DocumentFormat documentFormat = null;

        if (documentFormatId != null) {

            documentFormat = documentFormatRepository
                    .findById(documentFormatId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Document format not found: "
                                            + documentFormatId
                            )
                    );
        }

        // 5. Create case directory
        Path caseDirectory = uploadDirectory
                .resolve(sanitizeFileName(legalFile.getCaseNo()));

        Files.createDirectories(caseDirectory);

        // 6. Get original filename
        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name"
            );
        }

        // 7. Get extension
        String extension = getExtension(originalFileName);

        // 8. Generate unique filename
        String storedFileName =
                UUID.randomUUID() + extension;

        // 9. Physical file location
        Path targetPath =
                caseDirectory.resolve(storedFileName);

        // 10. Save physical file
        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // 11. Save database information
        FileDocument document =
                new FileDocument();

        document.setLegalFile(legalFile);

        document.setDocumentName(
                originalFileName
        );

        document.setDocumentFormat(
                documentFormat
        );

        document.setFilePath(
                targetPath.toString()
        );

        document.setFileSize(
                file.getSize()
        );

        document.setMimeType(
                file.getContentType()
        );

        document.setVersion(1);

        document.setIsFinal(false);

        document.setUploadedBy(user);

        FileDocument saved =
                fileDocumentRepository.save(document);

        // 12. Create response
        FileUploadResponse response =
                new FileUploadResponse();

        response.setId(saved.getId());

        response.setFileId(
                legalFile.getId()
        );

        response.setDocumentName(
                saved.getDocumentName()
        );

        response.setFileName(
                originalFileName
        );

        response.setFileSize(
                saved.getFileSize()
        );

        response.setMimeType(
                saved.getMimeType()
        );

        response.setVersion(
                saved.getVersion()
        );

        response.setIsFinal(
                saved.getIsFinal()
        );

        response.setUploadedBy(
                user.getUsername()
        );

        response.setUploadedAt(
                saved.getUploadedAt()
        );

        return response;
    }

    public List<FileUploadResponse> getDocumentsByFileId(Long fileId) {

        // Check if legal file exists
        LegalFile legalFile = legalFileRepository
                .findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Legal file not found: " + fileId
                        )
                );

        // Get documents belonging to this legal file
        List<FileDocument> documents =
                fileDocumentRepository.findByLegalFileId(
                        legalFile.getId()
                );

        // Convert FileDocument -> FileUploadResponse
        return documents.stream()
                .map(document -> {

                    FileUploadResponse response =
                            new FileUploadResponse();

                    response.setId(document.getId());

                    response.setFileId(
                            legalFile.getId()
                    );

                    response.setDocumentName(
                            document.getDocumentName()
                    );

                    response.setFileName(
                            document.getDocumentName()
                    );

                    response.setFileSize(
                            document.getFileSize()
                    );

                    response.setMimeType(
                            document.getMimeType()
                    );

                    response.setVersion(
                            document.getVersion()
                    );

                    response.setIsFinal(
                            document.getIsFinal()
                    );

                    if (document.getUploadedBy() != null) {
                        response.setUploadedBy(
                                document.getUploadedBy().getUsername()
                        );
                    }

                    response.setUploadedAt(
                            document.getUploadedAt()
                    );

                    return response;
                })
                .toList();
    }
    
    private String getExtension(String fileName) {

        int index = fileName.lastIndexOf('.');

        if (index == -1) {
            return "";
        }

        return fileName.substring(index)
                .toLowerCase();
    }

	private String sanitizeFileName(String fileName) {

	    return fileName
	            .replaceAll("[^a-zA-Z0-9._-]", "_");
	}

	
	private void validateFile(MultipartFile file) {

	    if (file == null || file.isEmpty()) {
	        throw new RuntimeException(
	                "Please select a file"
	        );
	    }

	    if (file.getSize() > MAX_FILE_SIZE) {
	        throw new RuntimeException(
	                "File size must not exceed 20 MB"
	        );
	    }

	    String contentType =
	            file.getContentType();

	    if (contentType == null ||
	            !ALLOWED_MIME_TYPES.contains(contentType)) {

	        throw new RuntimeException(
	                "File type is not allowed"
	        );
	    }
	}

	
}
