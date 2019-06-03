package com.example.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet.entity.User;

/**
 * @author manish.doodi
 *
 */
@Repository
public interface UserAccountRepository extends JpaRepository<User, Long> {

	Optional<User> getByUserName(String name);
	 
	Optional<User> findByPhone(Long phone);	
}
