package com.example.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.dto.mapper.UserAccountMapper;
import com.example.wallet.entity.User;
import com.example.wallet.enums.WalletResponseCode;
import com.example.wallet.exceptions.InvalidUserException;
import com.example.wallet.response.CoreResponse;
import com.example.wallet.service.UserAccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@PostMapping("/user")
	public ResponseEntity<CoreResponse<User>> createUser(@RequestBody UserAccountDTO userAccountDTO) {
		log.info("[createUser] create user with phone {}", userAccountDTO.getPhone());
		User user = null;
		user = userAccountService.createUser(userAccountDTO);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERCREATE000, user);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<CoreResponse<UserAccountDTO>> getUser(@PathVariable("id") Long id) {
		log.info("[getUser] with userAccountId {}", id);
		User userAccount = userAccountService.getUserById(id);
		if (userAccount == null) {
			throw new InvalidUserException("User with this userAccountId is not present in system");
		}
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERDETAIL000, UserAccountMapper.doToDTO(userAccount));
	}
	
	@GetMapping("/users")
	public ResponseEntity<CoreResponse<List<UserAccountDTO>>> getAllUsers() {
		log.info("[getAllUsers] get Users");
		List<User> userAccounts = userAccountService.getAllUser();
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERSDETAIL000, UserAccountMapper.doToDTOList(userAccounts));
	}
	
	@PutMapping("/user/{id}")
	public ResponseEntity<CoreResponse<UserAccountDTO>> updateUser(@PathVariable("id") Long userAccountId,
			@RequestBody UserAccountDTO userAccountDTO) {
		log.info("[updateUser] with userAccountId {}", userAccountId);
		User saved = userAccountService.updateUser(UserAccountMapper.dtoToDO(userAccountDTO), userAccountId);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERUPDATE000, UserAccountMapper.doToDTO(saved));
	}
}