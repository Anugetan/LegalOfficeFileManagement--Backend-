package com.anuge.legaloffice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anuge.legaloffice.entity.Users;
import com.anuge.legaloffice.service.AdminUserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminUserService adminUserService;


    public AdminController(
            AdminUserService adminUserService) {

        this.adminUserService = adminUserService;
    }


    // =====================================================
    // GET PENDING REGISTRATION REQUESTS
    // =====================================================

    @GetMapping("/registration-requests")
    public ResponseEntity<List<Users>> getPendingUsers() {

        return ResponseEntity.ok(
                adminUserService.getPendingUsers()
        );
    }


    // =====================================================
    // APPROVE USER
    // =====================================================

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<Users> approveUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminUserService.approveUser(id)
        );
    }


    // =====================================================
    // REJECT USER
    // =====================================================

    @PutMapping("/users/{id}/reject")
    public ResponseEntity<Users> rejectUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminUserService.rejectUser(id)
        );
    }
}