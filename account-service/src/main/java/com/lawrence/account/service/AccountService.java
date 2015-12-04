package com.lawrence.account.service;

import com.lawrence.account.persist.Account;

public interface AccountService {

	public boolean register(SignUpRequest request) throws AccountServiceException;
	
	public Account login(String id, String password) throws AccountServiceException;
	
	public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException;
	
	public String generateCaptchaKey() throws AccountServiceException;
	
	public boolean activate(String key) throws AccountServiceException;
	
}
