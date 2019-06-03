package com.example.wallet.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.TransactionDTO;
import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.dto.mapper.TransactionMapper;
import com.example.wallet.dto.mapper.UserAccountMapper;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.enums.WalletResponseCode;
import com.example.wallet.exceptions.InvalidUserException;
import com.example.wallet.exceptions.UserNotFoundException;
import com.example.wallet.response.CoreResponse;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.UserAccountService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/users")
@Slf4j
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping
	@ApiOperation(value = "Get All users ", response = List.class)
	public ResponseEntity getUsers() {
		List<User> userAccounts = userAccountService.getList();
		return new ResponseEntity<List<UserAccountDTO>>(UserAccountMapper.doToDTOList(userAccounts), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Get User by id", response = UserAccountDTO.class)
	public ResponseEntity getUserById(@PathVariable("id") Long id) {
		User userAccount;
		try {
			userAccount = userAccountService.userAccountByPK(id);
		} catch (UserNotFoundException ex) {
			Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserAccountDTO>(UserAccountMapper.doToDTO(userAccount), HttpStatus.OK);

	}

	@PostMapping("/")
	@ApiOperation(value = "create User", response = UserAccountDTO.class)
	public ResponseEntity createUser(@RequestBody UserAccountDTO userAccountDTO) {
		User saved;
		try {
			saved = userAccountService.save(UserAccountMapper.dtoToDO(userAccountDTO));
		} catch (Exception ex) {
			Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<UserAccountDTO>(UserAccountMapper.doToDTO(saved), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "update User by id", response = UserAccountDTO.class)
	public ResponseEntity updateUser(@PathVariable("id") Long userAccountId,
			@RequestBody UserAccountDTO userAccountDTO) {
		User saved;
		try {
			saved = userAccountService.update(UserAccountMapper.dtoToDO(userAccountDTO), userAccountId);
		} catch (Exception ex) {
			Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<UserAccountDTO>(UserAccountMapper.doToDTO(saved), HttpStatus.OK);
	}

	@GetMapping("/{id}/passbook")
	@ApiOperation(value = "get PassBook by UserId", response = List.class, tags = "getPassBook")
	public ResponseEntity getUserPassbook(@PathVariable("id") Long id) {
		List<Transaction> passbook;
		try {
			passbook = transactionService.transactionsByUserAccountID(id);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<TransactionDTO>>(TransactionMapper.doToDTOList(passbook), HttpStatus.OK);
	}
	
	@PostMapping("/user")
	@ApiOperation(value = "create User", response = UserAccountDTO.class)
	public ResponseEntity<CoreResponse<User>> createUsers(@RequestBody UserAccountDTO userAccountDTO)
			throws UserNotFoundException {
		log.info("[createUsers] create user with phone {}", userAccountDTO.getPhone());
		User user = null;
		user = userAccountService.createUser(userAccountDTO);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERCREATE000, user);
	}
	
	@GetMapping("/user/{id}")
	@ApiOperation(value = "Get User by id", response = UserAccountDTO.class)
	public ResponseEntity<CoreResponse<UserAccountDTO>> getUser(@PathVariable("id") Long id) throws UserNotFoundException {
			User userAccount = userAccountService.getUserById(id);
			if(userAccount == null) {
			//	throw new UserNotFoundException(HttpStatus.BAD_REQUEST.value(), "User is not present with this id");
				throw new InvalidUserException("Farzi");
			}
			return CoreResponse.buildWithSuccess(WalletResponseCode.USERDETAIL000, UserAccountMapper.doToDTO(userAccount));
	}
	
	@GetMapping("/users")
	@ApiOperation(value = "Get All users ", response = List.class)
	public ResponseEntity<CoreResponse<List<UserAccountDTO>>> getAllUsers() {
		List<User> userAccounts = userAccountService.getAllUser();
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERSDETAIL000, UserAccountMapper.doToDTOList(userAccounts));
	}
	
	@PutMapping("/users/{id}")
	@ApiOperation(value = "update User by id", response = UserAccountDTO.class)
	public ResponseEntity<CoreResponse<UserAccountDTO>> updateUserId(@PathVariable("id") Long userAccountId,
			@RequestBody UserAccountDTO userAccountDTO) throws UserNotFoundException {
		User saved = userAccountService.updateUser(UserAccountMapper.dtoToDO(userAccountDTO), userAccountId);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERUPDATE000, UserAccountMapper.doToDTO(saved));
	}
}