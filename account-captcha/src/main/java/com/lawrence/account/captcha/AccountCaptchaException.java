package com.lawrence.account.captcha;

public class AccountCaptchaException extends Exception{

	public AccountCaptchaException() {
		super();
	}
	
	AccountCaptchaException(String msg, Exception e){
		super(msg, e);
	}
}
