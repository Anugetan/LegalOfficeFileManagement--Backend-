package com.anuge.legaloffice.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "spms_types")
public class SpmsType {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "spms_name", nullable = false, unique = true)
	    private String spmsName;

	    @Column(nullable = false)
	    private Boolean active = true;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getSpmsName() {
	        return spmsName;
	    }

	    public void setSpmsName(String spmsName) {
	        this.spmsName = spmsName;
	    }

	    public Boolean getActive() {
	        return active;
	    }

	    public void setActive(Boolean active) {
	        this.active = active;
	    }

}
