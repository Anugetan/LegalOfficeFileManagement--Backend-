package com.anuge.wallet.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.wallet.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{
	
	List<TransactionEntity>
    findByUserIdOrderByCreatedAtDesc(Long userId);//Repository, please find me the user whose userId is this.

    Optional<TransactionEntity>
    findByReferenceNumber(String referenceNumber); //Repository, please find me the user whose referenceNumber is this.

    List<TransactionEntity>
    findByWalletAccountIdOrderByCreatedAtDesc(Long walletAccountId); //Repository, please find me the user whose walletAccountId is this.
    
}
 