package com.everestuniversity.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everestuniversity.dto.UserDto;

@RestController("/api/private/admin")
public class AdminController {

	
//	@GetMapping("/getalladmin")
//	public ResponseEntity<?> getAllAdmin() {
////		UserDto user = webBuilder.build().get().uri("http://API_GATEWAY/api/private/profile/getalluser").retrieve()
////				.bodyToMono(UserDto.class).block();
//		if (user.getRole().equalsIgnoreCase("admin")) {
//
//			return ResponseEntity.ok(user);
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found...");
//	}
//
//	@GetMapping("/getuserbyid")
//	public ResponseEntity<?> getUserById(@RequestParam UUID userId) {
////		UserDto user = webBuilder.build().get().uri("http://API_GATEWAY/api/private/profile/getuser").retrieve()
////				.bodyToMono(UserDto.class).block();
//		if (user.getUserId().equals(userId)) {
//			return ResponseEntity.ok(user);
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found...");
//	}
}
