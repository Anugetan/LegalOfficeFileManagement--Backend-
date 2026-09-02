package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.DocumentFormat;

public interface DocumentFormatRepository extends JpaRepository<DocumentFormat, Long> {

	List<DocumentFormat>findByActiveTrue();
}
