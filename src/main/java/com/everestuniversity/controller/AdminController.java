package com.everestuniversity.controller;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everestuniversity.entity.AdminEntity;
import com.everestuniversity.repository.AdminRepository;

@RestController("/api/private/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepo;

	@GetMapping("/getalladmin")
	public ResponseEntity<?> getAllAdmin() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Admin fetched successfully");
        response.put("data", adminRepo.findAll());
        return ResponseEntity.ok(response);
	}

	@GetMapping("/getadminbyid")
	public ResponseEntity<?> getUserById(@RequestParam UUID userId) {
        HashMap<String, Object> response = new HashMap<>();
		AdminEntity admin = adminRepo.findById(userId).orElse(null);
        if (admin != null) {
            response.put("success", true);
            response.put("message", "Admin fetched successfully");
            response.put("data", admin);
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "Admin not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
