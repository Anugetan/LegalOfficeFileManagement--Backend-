package com.anuge.legaloffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name = "status_name", nullable = false, unique = true)
	private String statusName;
	
	@Column (nullable = true)
	private Boolean active = true;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	
}
