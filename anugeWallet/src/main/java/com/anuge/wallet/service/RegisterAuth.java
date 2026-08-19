package com.anuge.wallet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.wallet.dto.RegisterRequest;
import com.anuge.wallet.entity.UsersEntity;
import com.anuge.wallet.repository.RegisterRepository;

@Service
public class RegisterAuth {

	private final RegisterRepository registerRepository;
	private final WalletAccountService walletAccountService;
	
	public RegisterAuth(
			RegisterRepository registerRepository,
			WalletAccountService walletAccountService) {
		 this.registerRepository = registerRepository;
		 this.walletAccountService = walletAccountService;
	}
	
	@Transactional
	public String register(RegisterRequest request) {
		 // CHECK USERNAME
		if(registerRepository.findByUsername(request.getUsername()).isPresent()) {
			
			return "Username already exist";
		}
		// CHECK EMAIL
		if(registerRepository.findByEmail(request.getEmail()).isPresent()) {
			return "Email already exist";
		}
		
		//CREATE USER --query create new user if not exist   
		UsersEntity user =
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
		// CREATE WALLET
		walletAccountService.createWallet(user.getId());
		
		return "Registration Successful";
	}
		
}
