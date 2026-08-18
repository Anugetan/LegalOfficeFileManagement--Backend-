package com.anuge.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.wallet.dto.RegisterRequest;
import com.anuge.wallet.service.RegisterAuth;

@RestController
@RequestMapping("/api/registerAuth")
public class RegisterController {
	
	private final RegisterAuth regiterAuth;
	
	public RegisterController(RegisterAuth regiterAuth) {
		this.regiterAuth = regiterAuth;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(
			@RequestBody RegisterRequest request) {
		
		String result = regiterAuth.register(request);
		
		if(result.equals("Username already exist")
	              || result.equals("Email already exist")) {
			
			return ResponseEntity.badRequest().body(result);
		}
		
		return ResponseEntity.ok(result);
	}

}
