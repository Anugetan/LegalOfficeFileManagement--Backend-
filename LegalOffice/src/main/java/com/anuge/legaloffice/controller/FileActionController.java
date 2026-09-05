package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.dto.FileActionResponse;
import com.anuge.legaloffice.service.FileActionService;

@RestController
@RequestMapping("/api/file-actions")
public class FileActionController {

    private final FileActionService fileActionService;

    public FileActionController(FileActionService fileActionService) {
        this.fileActionService = fileActionService;
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileActionResponse>> getActionsByFileId(
            @PathVariable Long fileId) {

        List<FileActionResponse> response =
                fileActionService.getActionsByFileId(fileId)
                        .stream()
                        .map(FileActionResponse::new)
                        .toList();

        return ResponseEntity.ok(response);
    }
}