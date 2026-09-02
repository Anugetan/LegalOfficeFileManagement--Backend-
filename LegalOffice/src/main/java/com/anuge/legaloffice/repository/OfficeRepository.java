package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {
	
	List<Office>findByActiveTrue();

}
