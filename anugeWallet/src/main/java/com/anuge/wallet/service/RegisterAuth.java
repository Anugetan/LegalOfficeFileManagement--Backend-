package com.anuge.wallet.service;

import org.springframework.stereotype.Service;

import com.anuge.wallet.dto.RegisterRequest;
import com.anuge.wallet.entity.UsersEntity;
import com.anuge.wallet.repository.RegisterRepository;

@Service
public class RegisterAuth {

	private final RegisterRepository registerRepository;
	
	public RegisterAuth(RegisterRepository registerRepository) {
		 this.registerRepository = registerRepository;
	}
	
	public String register(RegisterRequest request) {
		
		if(registerRepository.findByUsername(request.getUsername()).isPresent()) {
			
			return "Username already exist";
		}
		if(registerRepository.findByEmail(request.getEmail()).isPresent()) {
			return "Email already exist";
		}
		
		//query create new user if not exist
		registerRepository.save(
				new UsersEntity(
						request.getUsername(),
						request.getPassword(),
						request.getName(),
						request.getLastname(),
						request.getPhonenumber(),
						request.getEmail()
				)
		);
		return "Registration Successful";
	}
		
}
