package com.anuge.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuge.wallet.entity.UsersEntity;
import com.anuge.wallet.entity.WalletAccountEntity;
import com.anuge.wallet.repository.RegisterRepository;
import com.anuge.wallet.service.WalletAccountService;

@RestController
@RequestMapping("/api/Wallet")
public class WalletAccountController {

	private final WalletAccountService walletAccountService;
	private final RegisterRepository registerRepository;
	
	public WalletAccountController(
			WalletAccountService walletAccountService, 
			RegisterRepository registerRepository) {
		this.walletAccountService = walletAccountService;
		this.registerRepository = registerRepository;
	}
	
	@GetMapping
	public ResponseEntity<WalletAccountEntity> getWallet( 
			Authentication authentication) {
		String username = authentication.getName();
		
		UsersEntity user = registerRepository.findByUsername(username).orElseThrow(() ->
							new RuntimeException(
									"User not found"));
		WalletAccountEntity wallet = walletAccountService.getWallet(user.getId());
		
		return ResponseEntity.ok(wallet);
	}
	
}
