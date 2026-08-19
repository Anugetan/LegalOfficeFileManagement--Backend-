package com.anuge.wallet.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.anuge.wallet.entity.WalletAccountEntity;
import com.anuge.wallet.repository.WalletAccountRepository;

@Service
public class WalletAccountService {

	private final WalletAccountRepository walletAccountRepository;
	
	public WalletAccountService(WalletAccountRepository walletAccountRepository) {
		this.walletAccountRepository = walletAccountRepository;
	}
	
	  // GET EXISTING WALLET
	public WalletAccountEntity getWallet(Long userId) {
		return walletAccountRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Wallet not found"
                    )
                );
	}
	
	 // CREATE NEW WALLET
	public WalletAccountEntity createWallet(Long userId) {

	    WalletAccountEntity wallet = new WalletAccountEntity();
	    
	    wallet.setUserId(userId);
	    
	    wallet.setAccountNumber(
	            generateAccountNumber()
	    );

	    wallet.setBalance( BigDecimal.ZERO );

	    wallet.setCurrency("PHP");
	    wallet.setStatus("ACTIVE");

	    return walletAccountRepository.save(wallet);
	}
	
	// GENERATE WALLET ACCOUNT NUMBER
	private String generateAccountNumber() {
	    long number = System.currentTimeMillis();
	    return "WLT" + number;
	}

}
