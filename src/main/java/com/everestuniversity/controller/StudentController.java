package com.everestuniversity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everestuniversity.dto.LoginRequest;
import com.everestuniversity.entity.StudentEntity;
import com.everestuniversity.repository.StudentRepository;
import com.everestuniversity.service.MailService;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/public/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest loginRequest) {
        HashMap<String, Object> response = new HashMap<>();
        System.out.println("Enrollment id: " + loginRequest.getEnrollmentId());
        Optional<StudentEntity> op = studentRepo.findByEnrollmentId(loginRequest.getEnrollmentId());

        if (op.isPresent()) {
            StudentEntity student = op.get();
            if (student.getPassword().equals(loginRequest.getPassword())) {
                response.put("success", true);
                response.put("message", "Student login successfull");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Wrong password");
                return ResponseEntity.ok(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Wrong enrollment id");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/sendotp")
    public ResponseEntity<?> sendOtp(@RequestBody LoginRequest loginRequest, HttpSession session) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<StudentEntity> op = studentRepo.findByEmail(loginRequest.getEmail());
        if (op.isPresent()) {
            response.put("success", true);
            response.put("message", "Otp sent successfully");
            session.setAttribute("otp", mailService.sendOtp(loginRequest.getEmail()));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Email not registered");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody LoginRequest loginRequest, HttpSession session) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<StudentEntity> op = studentRepo.findByEmail(loginRequest.getEmail());
        if (op.isPresent()) {
            StudentEntity student = op.get();
            System.out.println("Enrollment id: " + student.getEnrollmentId());
            System.out.println("Enrollment id: " + loginRequest.getEnrollmentId());
            if (student.getEnrollmentId().equals(loginRequest.getEnrollmentId())) {
                if (loginRequest.getOtp().equals(session.getAttribute("otp"))) {
                    String enctyptedPassword = encoder.encode(loginRequest.getPassword());
                    student.setPassword(enctyptedPassword);
                    studentRepo.save(student);
                    response.put("success", true);
                    response.put("message", "Password changed successfully");
                    session.removeAttribute("otp");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "Wrong otp");
                    return ResponseEntity.ok(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Wrong enrollment id");
                return ResponseEntity.ok(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Wrong email");
            return ResponseEntity.ok(response);
        }
    }

}
