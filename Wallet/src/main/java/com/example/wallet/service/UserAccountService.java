package com.example.wallet.service;

import java.util.List;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.entity.User;

/**
 * @author manish.doodi
 *
 */

/** Service for Users */
public interface UserAccountService {

	User save(User t);

	User createUser(UserAccountDTO userAccountDTO) ;
	
	User getUserById(Long id);
	
	List<User> getAllUser();
	
	User updateUser(User t, Long id);

}