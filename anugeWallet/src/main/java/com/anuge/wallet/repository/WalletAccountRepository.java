package com.anuge.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.wallet.entity.WalletAccountEntity;

public interface WalletAccountRepository extends JpaRepository<WalletAccountEntity, Long> {
	
	Optional<WalletAccountEntity>findByUserId(Long userId);

}
