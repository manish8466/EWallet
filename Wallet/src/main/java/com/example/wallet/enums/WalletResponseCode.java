package com.example.wallet.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WalletResponseCode {

	USERCREATE000("USERCREATE000", "User created successfully.", HttpStatus.ACCEPTED),
	USERUPDATE000("USERUPDATE000", "User updated successfully.", HttpStatus.ACCEPTED),
	USERUPDATE400("USERUPDATE400", "User id not found for update.", HttpStatus.NOT_FOUND),
	USERDETAIL000("USERDETAIL000", "User details fetched successfully.", HttpStatus.OK),
	USERSDETAIL000("USERSDETAIL000", "Users info fetched successfully.", HttpStatus.OK),
	USERHISTORY000("USERHISTORY000", "User history fetched successfully", HttpStatus.OK),
	USERTXNDETAIL000("USERTXNDETAIL000", "User transaction details fetched successfully ", HttpStatus.OK),
	USERADDMONEY000("USERADDMONEY000", "User added amount to wallet successfully", HttpStatus.OK);
	private String code;
	private String message;
	private HttpStatus httpStatus;

}
