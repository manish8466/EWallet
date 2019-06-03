package com.example.wallet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.wallet.dto.TransactionDTO;
import com.example.wallet.dto.mapper.TransactionMapper;
import com.example.wallet.entity.Transaction;
import com.example.wallet.exceptions.BalanceLowException;
import com.example.wallet.exceptions.UserNotFoundException;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.UserAccountService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/transferTo")
@Slf4j
public class TransactionController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private TransactionService transactionService;

	@ApiOperation(value = "Add Money in Wallet ", response = TransactionDTO.class, tags = "transact")
	@PostMapping("/{id}")
	public ResponseEntity addMoney(@PathVariable("id") Long userAccountId, @RequestBody TransactionDTO walletDTO) {
		log.info("[addMoney] for the userAccountId {}",userAccountId);
		Transaction saved;
		try {
			walletDTO.setUserAccountId(userAccountId);
			saved = transactionService.createTransaction(TransactionMapper.dtoToDO(walletDTO));
		} catch (BalanceLowException ex) {
			Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			Logger.getLogger(UserAccountController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<TransactionDTO>(TransactionMapper.doToDTO(saved), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Pay from wallet ", response = List.class, tags = "transact")
	@PostMapping("/{toUser}/from/{fromUser}")
	public ResponseEntity transferMoney(@PathVariable("toUser") Long toUserAccountId,
			@PathVariable("fromUser") Long fromUserAccountId, @RequestBody TransactionDTO walletDTO) {
		HashMap<String,String> transaction;

		try {
			transaction = transactionService.transfer(walletDTO, toUserAccountId, fromUserAccountId);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (BalanceLowException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(transaction, HttpStatus.OK);
	}

	@ApiOperation(value = "Pay from wallet ", response = List.class, tags = "transact")
	@GetMapping("/history/{userId}")
	public ResponseEntity history(@PathVariable("userId") Long toUserAccountId) {
		List<HashMap<String,String>> transactions;

		try {
			transactions = transactionService.history(toUserAccountId);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (BalanceLowException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

}