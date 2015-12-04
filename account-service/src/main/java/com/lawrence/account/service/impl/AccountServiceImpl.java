package com.lawrence.account.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.lawrence.account.captcha.AccountCaptchaException;
import com.lawrence.account.captcha.AccountCaptchaService;
import com.lawrence.account.captcha.RandomGenerator;
import com.lawrence.account.email.AccountEmailException;
import com.lawrence.account.email.AccountEmailService;
import com.lawrence.account.persist.Account;
import com.lawrence.account.persist.AccountPersistException;
import com.lawrence.account.persist.AccountPersistService;
import com.lawrence.account.service.AccountService;
import com.lawrence.account.service.AccountServiceException;
import com.lawrence.account.service.SignUpRequest;

public class AccountServiceImpl implements AccountService{
	
	private AccountPersistService persistService;
	private AccountEmailService emailService;
	private AccountCaptchaService captchaService;

	private Map<String, String> activationIdMap = new HashMap<String, String>();
	
	@Override
	public boolean register(SignUpRequest request) throws AccountServiceException {
		try {
			boolean validatePassFlag = captchaService.validate(request.getCaptchaKey(), request.getCaptchaValue());
			if(!validatePassFlag) return false;
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to validate captcha key.", e);
		}
		
		String pwd = request.getPassword();
		String pwdConfirm = request.getPasswordConfirm();
		if(pwd != null && pwdConfirm != null && pwd.equals(pwdConfirm)){
			Account account = new Account();
			account.setId(request.getId());
			account.setName(request.getName());
			account.setPassword(pwd);
			account.setEmail(request.getEmail());
			account.setActivated(false);
			try {
				persistService.createAccount(account);
			} catch (AccountPersistException e) {
				throw new AccountServiceException("Unable to create account.", e);
			}
			
			String actKey = RandomGenerator.getRandomString();//this key is used for account activation.
			activationIdMap.put(actKey, account.getId());
			
			String activateServiceUrl = request.getActivateServiceUrl();
			activateServiceUrl += activateServiceUrl.endsWith("/") ? actKey : "?key=" + actKey;
			try {
				return emailService.sendMail(account.getEmail(), "Verify Your Account", activateServiceUrl);
			} catch (AccountEmailException e) {
				throw new AccountServiceException("Unable to send verification mail.", e);
			}
		}else{
			throw new AccountServiceException("2 passwords do not match.");
		}
	}

	@Override
	public Account login(String id, String password) throws AccountServiceException {
		try {
			Account account = persistService.readAccount(id);
			if(account != null&& account.isActivated() && account.getPassword().equals(password)){
				return account;
			}
		} catch (AccountPersistException e) {
			throw new AccountServiceException("Unable to login.", e);
		}
		return null;
	}

	@Override
	public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException {
		try {
			return captchaService.genImage(captchaKey);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to create captcha image.", e);
		}
	}

	@Override
	public String generateCaptchaKey() throws AccountServiceException {
		try {
			return captchaService.genKey();
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to generate captcha key.", e);
		}
	}

	@Override
	public boolean activate(String key) throws AccountServiceException {
		String accountId = activationIdMap.get(key);
		if(accountId == null) throw new AccountServiceException("Invalid key to activate account.");
		try {
			Account account = persistService.readAccount(accountId);
			account.setActivated(true);
			persistService.updateAccount(account);
			activationIdMap.remove(key);
			return true;
		} catch (AccountPersistException e) {
			throw new AccountServiceException("Unable to change account activation status.");
		}
	}

	public AccountPersistService getPersistService() {
		return persistService;
	}

	public void setPersistService(AccountPersistService persistService) {
		this.persistService = persistService;
	}

	public AccountEmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(AccountEmailService emailService) {
		this.emailService = emailService;
	}

	public AccountCaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(AccountCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

}
