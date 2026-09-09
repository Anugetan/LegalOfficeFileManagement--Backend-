package com.anuge.legaloffice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.anuge.legaloffice.enumerate.RegistrationStatus;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role")
    private String role;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    private RegistrationStatus registrationStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public Users() {
    }


    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (role == null) {
            role = "USER";
        }

        if (active == null) {
            active = true;
        }

        if (registrationStatus == null) {
            registrationStatus = RegistrationStatus.PENDING;
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}