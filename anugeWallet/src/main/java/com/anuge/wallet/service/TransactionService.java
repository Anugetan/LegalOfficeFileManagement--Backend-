package com.anuge.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anuge.wallet.dto.TransactionRequest;
import com.anuge.wallet.entity.TransactionEntity;
import com.anuge.wallet.entity.UsersEntity;
import com.anuge.wallet.entity.WalletAccountEntity;
import com.anuge.wallet.enumtransaction.TransactionStatus;
import com.anuge.wallet.enumtransaction.TransactionType;
import com.anuge.wallet.repository.LoginRepository;
import com.anuge.wallet.repository.TransactionRepository;
import com.anuge.wallet.repository.WalletAccountRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletAccountRepository walletAccountRepository;
    private final LoginRepository loginRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletAccountRepository walletAccountRepository,
            LoginRepository loginRepository) {

        this.transactionRepository = transactionRepository;
        this.walletAccountRepository = walletAccountRepository;
        this.loginRepository = loginRepository;
    }


    // =========================================================
    // CREATE TRANSACTION
    // =========================================================

    @Transactional
    public TransactionEntity createTransaction(
            String username,
            TransactionRequest request) {

        // 1. FIND USER
        UsersEntity user = loginRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        // 2. FIND USER WALLET

        WalletAccountEntity wallet = walletAccountRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Wallet account not found"
                                )
                        );


        // 3. VALIDATE WALLET STATUS

        if (!"ACTIVE".equals(wallet.getStatus().toString())) {
            throw new RuntimeException("Wallet account is not active" );
        }

   
        // 4. VALIDATE AMOUNT
    
        if (request.getAmount() == null) {
            throw new RuntimeException( "Amount is required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException( "Amount must be greater than zero");
        }


        // 5. VALIDATE TRANSACTION TYPE
       
        if (request.getTransactionType() == null) {
            throw new RuntimeException("Transaction type is required");
        }


        // 6. CHECK BALANCE  

        BigDecimal currentBalance = wallet.getBalance();

        if (request.getTransactionType() ==
                TransactionType.WITHDRAW
                ||
                request.getTransactionType() ==
                TransactionType.TRANSFER
                ||
                request.getTransactionType() ==
                TransactionType.BILL_PAYMENT) {


            if (currentBalance.compareTo(
                    request.getAmount()) < 0) {

                throw new RuntimeException(
                        "Insufficient wallet balance"
                );
            }
        }

        
        // 7. CALCULATE NEW BALANCE     

        BigDecimal newBalance =currentBalance;

        if (request.getTransactionType() == TransactionType.DEPOSIT) {
            newBalance = currentBalance.add(
                            request.getAmount()
                         );
        }
        else if (
                request.getTransactionType() == 
                TransactionType.WITHDRAW
                ||
                request.getTransactionType() ==
                TransactionType.TRANSFER
                ||
                request.getTransactionType() ==
                TransactionType.BILL_PAYMENT) { 
        	
                 newBalance =
                    currentBalance.subtract(
                            request.getAmount()
                    );
        }


       
        // 8. CREATE TRANSACTION       

        TransactionEntity transaction = new TransactionEntity();

        transaction.setUserId(user.getId());
        transaction.setWalletAccountId(wallet.getId());
        transaction.setPaymentMethodId(request.getPaymentMethodId());
        transaction.setBankAccountId(request.getBankAccountId());
        transaction.setBeneficiaryId(request.getBeneficiaryId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());

        // 9. GENERATE REFERENCE NUMBER      

        String referenceNumber =
                "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20)
                        .toUpperCase();

        transaction.setReferenceNumber(
                referenceNumber
        );


        // 10. RECIPIENT INFORMATION   

        transaction.setRecipientName(request.getRecipientName());
        transaction.setRecipientAccountNumber(request.getRecipientAccountNumber());
        transaction.setRecipientBankCode(request.getRecipientBankCode());
        
        // 11. TRANSACTION STATUS

        transaction.setStatus(TransactionStatus.COMPLETED);

        LocalDateTime now = LocalDateTime.now();
        

        transaction.setCreatedAt(now);
        transaction.setCompletedAt(now);
        transaction.setUpdatedAt(now);
   
        // 12. SAVE TRANSACTION

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

       
        // 13. UPDATE WALLET BALANCE        

        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(now);
        walletAccountRepository.save(wallet);

        // 14. RETURN TRANSACTION

        return savedTransaction;
    }
}