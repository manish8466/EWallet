package com.example.wallet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.TransactionDTO;
import com.example.wallet.dto.mapper.TransactionMapper;
import com.example.wallet.entity.Transaction;
import com.example.wallet.enums.WalletResponseCode;
import com.example.wallet.response.CoreResponse;
import com.example.wallet.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("add-money/{userAccountId}")
	public ResponseEntity<CoreResponse<TransactionDTO>> addMoney(@PathVariable("userAccountId") Long userAccountId,
			@RequestBody TransactionDTO walletDTO) {
		log.info("[addMoney] for the userAccountId {}", userAccountId);
		Transaction saved;
		walletDTO.setUserAccountId(userAccountId);
		saved = transactionService.creditWallet(TransactionMapper.dtoToDO(walletDTO));
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERADDMONEY000, TransactionMapper.doToDTO(saved));
	}

	@PostMapping("transfer/{toUser}/from/{fromUser}")
	public ResponseEntity<CoreResponse<Map<String, String>>> transferMoney(@PathVariable("toUser") Long toUserAccountId,
			@PathVariable("fromUser") Long fromUserAccountId, @RequestBody TransactionDTO walletDTO) {
		log.info("[transferMoney] fromUserAccount {} toUserAccount {}", fromUserAccountId, toUserAccountId);
		Map<String, String> transaction = null;
			transaction = transactionService.moneytransfer(walletDTO, toUserAccountId, fromUserAccountId);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERTXNDETAIL000, transaction);
	}

	@GetMapping("user/history/{userAccountId}")
	public ResponseEntity<CoreResponse<List<HashMap<String, String>>>> history(
			@PathVariable("userAccountId") Long userAccountId) {
		List<HashMap<String, String>> transactions;
		log.info("[history] history of userAccountId {}", userAccountId);
		transactions = transactionService.userHistory(userAccountId);
		return CoreResponse.buildWithSuccess(WalletResponseCode.USERHISTORY000, transactions);
	}

}