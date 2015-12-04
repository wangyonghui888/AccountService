package com.lawrence.account.service;

public class AccountServiceException extends Exception {

	public AccountServiceException() {
		super();
	}
	
	public AccountServiceException(String msg, Exception e){
		super(msg, e);
	}
	
	public AccountServiceException(String msg){
		super(msg);
	}
	
}
