package com.everestuniversity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.everestuniversity.dto.AdmissionRequestDTO;
import com.everestuniversity.entity.AdmissionRequest;
import com.everestuniversity.repository.AdmissionRequestRepository;
import com.everestuniversity.service.AdmissionRequestService;
import com.everestuniversity.service.AdmissionService;
import com.everestuniversity.service.MailService;
import com.everestuniversity.service.UUIDService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/public/admission")
public class AdmissionController {

    @Autowired
    private AdmissionRequestService admissionRequestService;

    @Autowired
    private AdmissionService admissionService;

    @Autowired
    private AdmissionRequestRepository admissionRequestRepo;

    @Autowired
    private MailService mailService;

    @PostMapping("/registration")
    public ResponseEntity<?> admissionRegistration(
            @RequestPart("tenthFile") MultipartFile tenthFile,
            @RequestPart("twelthFile") MultipartFile twelthFile,
            @RequestPart("registrationJson") String registrationJson) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AdmissionRequestDTO admissionRequestDTO = objectMapper.readValue(registrationJson, AdmissionRequestDTO.class);

            // Convert the dateOfBirth from String to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dateOfBirth = LocalDate.parse(admissionRequestDTO.getDateOfBirth(), formatter);

            String fullName = admissionRequestDTO.getSurName() + " " + admissionRequestDTO.getFirstName() + " "
                    + admissionRequestDTO.getMiddleName();

            String tenthFilePath = admissionRequestService.saveFilePath(tenthFile, fullName);
            String twelthFilePath = admissionRequestService.saveFilePath(twelthFile, fullName);

            AdmissionRequest request = admissionRequestService.mapDtoToEntity(admissionRequestDTO, tenthFilePath,
                    twelthFilePath, dateOfBirth);
            admissionRequestService.saveRegistration(request);
            admissionRequestService.saveAdmission(request.getRegistrationId());
            response.put("message", "Registration successful");
            response.put("success", true);
            response.put("data", request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Registration failed");
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    

}
