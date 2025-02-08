package com.everestuniversity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everestuniversity.dto.LoginRequest;
import com.everestuniversity.entity.StudentEntity;
import com.everestuniversity.entity.StudentProfileEntity;
import com.everestuniversity.repository.StudentProfileRepository;
import com.everestuniversity.repository.StudentRepository;
import com.everestuniversity.service.MailService;
import com.everestuniversity.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/private/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;
    
    @Autowired
    private StudentProfileRepository  studentProfileRepo;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping("/getallstudent")
    public ResponseEntity<?> getAllStudent(){
    	HashMap<String, Object> response = new HashMap<>();
    	List<StudentEntity> students = studentRepo.findAll();
    	response.put("success",true);
    	response.put("message","Students fetched successfully");
    	response.put("data", students);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/getstudentbyid")
    public ResponseEntity<?> getStudentById(@RequestParam("studentId") String studentId){
    	HashMap<String, Object> response = new HashMap<>();
		StudentEntity student = studentService.getStudentById(studentId);
		if(student != null) {
			response.put("success", true);
			response.put("message", "Student fetched successfully");
			response.put("data", student);
			return ResponseEntity.ok(response);
		}
		response.put("success", false);
		response.put("message", "Student not found");
		return ResponseEntity.ok(response);
    }
    
    @PutMapping("/updatestudent")
    public ResponseEntity<?> updateStudent(@RequestParam("studentId") String studentID, 
    		@RequestPart(value = "file", required = false) MultipartFile file,
    		@RequestPart(value = "student", required = false) String studentJson){
    	StudentProfileEntity student = new StudentProfileEntity();
    	student.getAddress();
    	return null;
    }
    

}
