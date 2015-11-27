package com.lawrence.account.persist;

public class AccountPersistException extends Exception {

	public AccountPersistException() {
		super();
	}
	
	public AccountPersistException(String msg, Exception e){
		super(msg, e);
	}
}
