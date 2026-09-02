package com.anuge.legaloffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_formats")
public class DocumentFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "format_name", nullable = false, unique = true, length = 100)
    private String formatName;

    @Column(nullable = false)
    private Boolean active = true;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}