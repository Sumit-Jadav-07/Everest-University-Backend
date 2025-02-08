package com.everestuniversity.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everestuniversity.entity.StudentEntity;
import com.everestuniversity.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepo; 
	
	public boolean validateStudent(String studentId) {
		String sanitizedId = studentId.startsWith("0x") ? studentId.substring(2) : studentId;
        UUID uuid = UUIDService.formatUuid(sanitizedId);
        Optional<StudentEntity> student = studentRepo.findById(uuid);
        if (student.isEmpty()) {
            return false;
        }
        return true;
	}
	
	public StudentEntity getStudentById(String studentId){
		String sanitizedId = studentId.startsWith("0x") ? studentId.substring(2) : studentId;
        UUID uuid = UUIDService.formatUuid(sanitizedId);
        Optional<StudentEntity> student = studentRepo.findById(uuid);
        if (student.isEmpty()) {
            return null;
        }
        return student.get();
	}

}
