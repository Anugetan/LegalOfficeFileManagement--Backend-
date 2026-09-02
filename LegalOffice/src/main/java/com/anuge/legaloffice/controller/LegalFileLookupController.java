package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.legaloffice.entity.DocumentFormat;
import com.anuge.legaloffice.entity.DocumentType;
import com.anuge.legaloffice.entity.Office;
import com.anuge.legaloffice.entity.SpmsType;
import com.anuge.legaloffice.entity.Status;
import com.anuge.legaloffice.repository.DocumentFormatRepository;
import com.anuge.legaloffice.repository.DocumentTypeRepository;
import com.anuge.legaloffice.repository.OfficeRepository;
import com.anuge.legaloffice.repository.SpmsTypeRepository;
import com.anuge.legaloffice.repository.StatusRepository;

@RestController
@RequestMapping("/api/legal-file-options")
@CrossOrigin
public class LegalFileLookupController {

    private final StatusRepository statusRepository;
    private final SpmsTypeRepository spmsTypeRepository;
    private final OfficeRepository officeRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentFormatRepository documentFormatRepository;

    public LegalFileLookupController(
            StatusRepository statusRepository,
            SpmsTypeRepository spmsTypeRepository,
            OfficeRepository officeRepository,
            DocumentTypeRepository documentTypeRepository,
            DocumentFormatRepository documentFormatRepository) {

        this.statusRepository = statusRepository;
        this.spmsTypeRepository = spmsTypeRepository;
        this.officeRepository = officeRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentFormatRepository = documentFormatRepository;
    }


    @GetMapping("/statuses")
    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }


    @GetMapping("/spms-types")
    public List<SpmsType> getSpmsTypes() {
        return spmsTypeRepository.findAll();
    }


    @GetMapping("/offices")
    public List<Office> getOffices() {
        return officeRepository.findAll();
    }


    @GetMapping("/document-types")
    public List<DocumentType> getDocumentTypes() {
        return documentTypeRepository.findAll();
    }


    @GetMapping("/document-formats")
    public List<DocumentFormat> getDocumentFormats() {
        return documentFormatRepository.findAll();
    }
}