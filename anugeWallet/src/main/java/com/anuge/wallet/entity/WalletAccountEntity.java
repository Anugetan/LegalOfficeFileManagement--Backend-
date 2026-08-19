package com.anuge.wallet.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_accounts")
public class WalletAccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;
	
	 @Column(
		        nullable = false,
		        precision = 15,
		        scale = 2
		    )
		    private BigDecimal balance = BigDecimal.ZERO;

		    @Column(nullable = false)
		    private String currency = "PHP";

		    @Column(nullable = false)
		    private String status = "ACTIVE";

		    @Column(name = "created_at")
		    private LocalDateTime createdAt;

		    @Column(name = "updated_at")
		    private LocalDateTime updatedAt;
		    
			@PrePersist
		    protected void onCreate() {
		        createdAt = LocalDateTime.now();
		        updatedAt = LocalDateTime.now();
		    }

		    @PreUpdate
		    protected void onUpdate() {
		        updatedAt = LocalDateTime.now();
		    }
		    


		    public Long getId() {
				return Id;
			}

			public void setId(Long id) {
				Id = id;
			}

			public Long getUserId() {
				return userId;
			}

			public void setUserId(Long userId) {
				this.userId = userId;
			}

			public String getAccountNumber() {
				return accountNumber;
			}

			public void setAccountNumber(String accountNumber) {
				this.accountNumber = accountNumber;
			}

			public BigDecimal getBalance() {
				return balance;
			}

			public void setBalance(BigDecimal balance) {
				this.balance = balance;
			}

			public String getCurrency() {
				return currency;
			}

			public void setCurrency(String currency) {
				this.currency = currency;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public LocalDateTime getCreatedAt() {
				return createdAt;
			}

			public void setCreatedAt(LocalDateTime createdAt) {
				this.createdAt = createdAt;
			}

			public LocalDateTime getUpdatedAt() {
				return updatedAt;
			}

			public void setUpdatedAt(LocalDateTime updatedAt) {
				this.updatedAt = updatedAt;
			}
	
}
