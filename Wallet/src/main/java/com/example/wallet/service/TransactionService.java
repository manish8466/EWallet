package com.example.wallet.service;

import java.util.HashMap;
import java.util.List;

import com.example.wallet.dto.TransactionDTO;
import com.example.wallet.entity.Transaction;

/**
 * @author manish.doodi
 *
 */

/** Service for Transaction */
public interface TransactionService {

	Transaction creditWallet(Transaction txn);

	HashMap<String, String> moneytransfer(TransactionDTO walletDTO, Long toUserAccountId, Long fromUserAccountId);

	List<HashMap<String, String>> userHistory(Long id);
}
