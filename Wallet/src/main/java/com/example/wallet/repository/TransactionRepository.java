package com.example.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet.entity.Transaction;

/**
 * @author Manish.doodi
 *
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


	List<Transaction> findByUserAccountId(Long accountId);
}