package com.anuge.legaloffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.legaloffice.entity.SpmsType;

public interface SpmsTypeRepository extends JpaRepository<SpmsType, Long> {
	
	List<SpmsType>findByActiveTrue();

}
