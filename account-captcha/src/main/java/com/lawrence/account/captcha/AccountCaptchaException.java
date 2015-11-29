package com.lawrence.account.captcha;

public class AccountCaptchaException extends Exception{

	public AccountCaptchaException() {
		super();
	}
	
	public AccountCaptchaException(String msg, Exception e){
		super(msg, e);
	}
	
	public AccountCaptchaException(String  msg){
		super(msg);
	}
}
