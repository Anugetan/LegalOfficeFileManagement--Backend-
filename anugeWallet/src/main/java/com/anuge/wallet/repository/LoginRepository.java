package com.anuge.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuge.wallet.entity.LoginEntity;

public interface LoginRepository extends JpaRepository<LoginEntity, Integer>{ //Create a repository for LoginEntity, where the primary key is an Integer
	
	Optional<LoginEntity>findByUsername(String username); //Repository, please find me the user whose username is this.

}
 