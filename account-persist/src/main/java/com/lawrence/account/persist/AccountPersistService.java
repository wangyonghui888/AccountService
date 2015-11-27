package com.lawrence.account.persist;

public interface AccountPersistService {
	
	//Account CRUD operations
	
	Account createAccount(Account account) throws AccountPersistException;
	
	Account readAccount(String id) throws AccountPersistException;
	
	Account updateAccount(Account account)throws AccountPersistException;
	
	boolean deleteAccount(String id) throws AccountPersistException;

}
