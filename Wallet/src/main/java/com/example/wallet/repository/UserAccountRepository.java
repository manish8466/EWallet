package com.example.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wallet.entity.User;
import java.lang.Long;
import java.util.List;

/**
 * @author Deepak Garg
 *
 */
public interface UserAccountRepository extends JpaRepository<User, Long> {

	/**gets user by name
	 * @param name
	 * @return user account
	 */
	Optional<User> getByUserName(String name);
	 
	Optional<User> findByPhone(Long phone);	
}
