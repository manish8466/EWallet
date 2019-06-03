package com.example.wallet.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.dto.TransactionDTO;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.exceptions.BalanceLowException;
import com.example.wallet.exceptions.InvalidUserException;
import com.example.wallet.repository.AccountRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.UserAccountService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserAccountService accountService;
	@Autowired
	AccountRepository accountRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Transaction creditWallet(Transaction transaction)  {
		log.info("[creditWallet] for userAccountId {}",transaction.getUserAccount().getId());
		User user=accountService.getUserById(transaction.getUserAccount().getId());
		if(user == null) {
			throw new InvalidUserException("user is not present in system with this userAccountId");
		}
		double balance = user.getAccount().getAmount();
		if(transaction.getAmount() <=0) {
			throw new BalanceLowException("Cannot add amount to wallet,amount is less than or equal to 0");
		}
        balance= balance+transaction.getAmount();
        user.getAccount().setAmount(balance);
			accountService.save(user);
        if (balance>= 0) {
			transaction.setAccount(user.getAccount());
			transaction.setUserAccount(user);
			return transactionRepository.save(transaction);
		}
      return null;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public HashMap<String,String> moneytransfer(TransactionDTO walletDTO, Long toUserAccountId, Long fromUserAccountId) {
		log.info("[moneytransfer] fromUserAccountId {} and toUserAccountId {}", fromUserAccountId, toUserAccountId);
		User toUser=accountService.getUserById(toUserAccountId);
		User fromUser=accountService.getUserById(fromUserAccountId);
		if(toUser == null || fromUser == null) {
			throw new InvalidUserException("toUserAccountId or fromUserAccoun is invalid");
		}
		double fromBalance=fromUser.getAccount().getAmount()-walletDTO.getAmount();
		fromUser.getAccount().setAmount(fromBalance);
		if (fromBalance < 0) {
			throw new BalanceLowException("Insufficient balance in the sender wallet");
		}
		double toBalance=toUser.getAccount().getAmount()+walletDTO.getAmount();
		toUser.getAccount().setAmount(toBalance);
		Transaction transaction = new Transaction();
		transaction.setUserAccount(fromUser);
		transaction.setAccount(fromUser.getAccount());
		transaction.setAmount(walletDTO.getAmount());
		transaction.setTransactionDate(new Date());
		transaction.setTransactionType(1);
		transaction.setTransactionReference(UUID.randomUUID().toString());
		try {
			accountService.save(fromUser);
			accountService.save(toUser);
			 Transaction tarTransaction1=transactionRepository.save(transaction);
			HashMap<String,String> map= new HashMap<>();
			 map.put("referanceId",tarTransaction1.getTransactionReference());
			 map.put("message","transaction process successfully");
			return  map;
		} catch (Exception e) {
			log.error("[moneytransfer] exception occured with message {}", e.getMessage());
		}

		return  null;
	}

	@Override
	public List<HashMap<String, String>> userHistory(Long id)  {
		log.info("[userHistory] of accountId {}",id);
		User user = accountService.getUserById(id);
		if(user == null) {
			throw new InvalidUserException("User is not present with this accountId");
		}
		List<Transaction> list = transactionRepository.findByUserAccountId(id);
		List<HashMap<String,String>> response= new ArrayList<>();
		for (Transaction transaction : list) {
			  HashMap<String,String> map= new HashMap<>();
			  map.put("phone",transaction.getUserAccount().getPhone().toString());
			  map.put("amountTransfer",String.valueOf(transaction.getAmount()));
			  map.put("date",transaction.getTransactionDate().toString());
			  map.put("transactionReference",transaction.getTransactionReference());
			  response.add(map);
		}
		return response;
	}

}
