package com.example.wallet.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.dto.mapper.UserAccountMapper;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.User;
import com.example.wallet.exceptions.UserNotFoundException;
import com.example.wallet.repository.UserAccountRepository;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.utility.ValidationUtils;
import com.google.common.collect.Lists;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public User userAccountByPK(Long userAccountId) throws UserNotFoundException {
		return userAccountRepository.findById(userAccountId).orElseThrow(
				() -> new UserNotFoundException(HttpStatus.BAD_REQUEST.value(),String.format("userAccount with '%d' not found ", userAccountId)));
	}

	/**
	 * this operations registers a user and creates and userAccount for him/her with
	 * minimal details
	 */
	@Override
	@Transactional
	public User save(User userAccount) throws Exception {
		if (userAccount.getUserName() != null) {
			if (userAccount.getUserName().length() < 5) {
				throw new Exception("user name is should be 5 characters of more");
			}
			return userAccountRepository.save(userAccount);
		}
		throw new Exception("user name is mandatory");
	}

	/**
	 * this operation updates a users userAccount information and checks for
	 * concurrent user modification
	 */
	@Override
	@Transactional
	public User update(User userAccount, Long userAccountId) throws Exception {
		if (userAccount.getUserName() != null) {
			userAccount.setId(userAccountId);
			try {
				return userAccountRepository.save(userAccount);
			} catch (Exception e) {
				throw new Exception("Try again");
			}
		}
		throw new Exception("user name is mandatory");

	}

	/**
	 * this operation gets all userAccount lists and their respective transaction
	 * transactions
	 */
	@Override
	public List<User> getList() {
		return Lists.newArrayList(userAccountRepository.findAll());
	}

	@Transactional(rollbackFor = UserNotFoundException.class)
	@Override
	public User createUser(UserAccountDTO userAccountDto) throws UserNotFoundException {

		if (userAccountDto.getPhone() == null) {
			throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(), "Provide mobile number to the user");
		}
		Optional<User> existingUser = userAccountRepository.findByPhone(userAccountDto.getPhone());
		if (existingUser.isPresent())
			throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(),
					"User already exists with this mobile Number");

		if (userAccountDto.getPhone() != null) {
			if (!ValidationUtils.isValidPhoneNo(userAccountDto.getPhone()))
				throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(), "This phone no. is invalid");
		}
		User userAccount = UserAccountMapper.dtoToDO(userAccountDto);
		Account account = new Account();
		account.setAmount(0);
		userAccount.setAccount(account);
		return userAccountRepository.save(userAccount);
	}

	@Override
	@Transactional
	public User getUserById(Long id){
		Optional<User> userOptional = userAccountRepository.findById(id);
		if(!userOptional.isPresent())return null; 
		return userOptional.get();
	}
	
	public List<User> getAllUser() {
		return Lists.newArrayList(userAccountRepository.findAll());
	}
	
	@Transactional(rollbackFor = UserNotFoundException.class)
	public User updateUser(User userAccount, Long userAccountId) throws UserNotFoundException {
		if (userAccountId == null || userAccount.getPhone() == null)
			throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(), "User Not found");

		Optional<User> existingUser = userAccountRepository.findById(userAccountId);
		if (!existingUser.isPresent())
			throw new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "User Not found");
		if (userAccount.getPhone() != null) {
			if (!ValidationUtils.isValidPhoneNo(userAccount.getPhone()))
				throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(), "This phone no. is invalid");
		}
		userAccount.setId(userAccountId);
		return userAccountRepository.save(userAccount);

	}
   

}