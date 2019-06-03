package com.example.wallet.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.dto.mapper.UserAccountMapper;
import com.example.wallet.entity.Account;
import com.example.wallet.entity.User;
import com.example.wallet.exceptions.InvalidUserException;
import com.example.wallet.repository.UserAccountRepository;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.utility.ValidationUtils;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	@Transactional
	public User save(User userAccount) {
		return userAccountRepository.save(userAccount);
	}

	@Transactional(rollbackFor = InvalidUserException.class)
	@Override
	public User createUser(UserAccountDTO userAccountDto) {
		log.info("[createUser] with mobile {}", userAccountDto.getPhone());
		if (userAccountDto.getPhone() == null) {
			throw new InvalidUserException("Provide mobile number to the user");
		}
		Optional<User> existingUser = userAccountRepository.findByPhone(userAccountDto.getPhone());
		if (existingUser.isPresent())
			throw new InvalidUserException("User already exists with this mobile Number");

		if (userAccountDto.getPhone() != null) {
			if (!ValidationUtils.isValidPhoneNo(userAccountDto.getPhone()))
				throw new InvalidUserException("This phone no. is invalid, provide valid mobile Number");
		}
		User userAccount = UserAccountMapper.dtoToDO(userAccountDto);
		Account account = new Account();
		account.setAmount(0);
		userAccount.setAccount(account);
		return userAccountRepository.save(userAccount);
	}

	@Override
	@Transactional
	public User getUserById(Long id) {
		log.info("[getUserById] with userAccountId {}", id);
		Optional<User> userOptional = userAccountRepository.findById(id);
		if (!userOptional.isPresent())
			return null;
		return userOptional.get();
	}

	public List<User> getAllUser() {
		log.info("[getAllUser] get all users");
		return Lists.newArrayList(userAccountRepository.findAll());
	}

	@Transactional(rollbackFor = InvalidUserException.class)
	public User updateUser(User userAccount, Long userAccountId) {
		log.info("[updateUser] with userAccountId {}", userAccountId);
		if (userAccountId == null || userAccount.getPhone() == null)
			throw new InvalidUserException("UserAccountId or phone number cannot be null");

		Optional<User> existingUser = userAccountRepository.findById(userAccountId);
		if (!existingUser.isPresent())
			throw new InvalidUserException("User Not found with this userAccountId");
		if (userAccount.getPhone() != null) {
			if (!ValidationUtils.isValidPhoneNo(userAccount.getPhone()))
				throw new InvalidUserException("This phone no. is invalid");
		}
		userAccount.setId(userAccountId);
		return userAccountRepository.save(userAccount);

	}
   

}