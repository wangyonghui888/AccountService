package com.lawrence.account.captcha;

import java.util.List;

public interface AccountCaptchaService {

	public String genKey() throws AccountCaptchaException;
	
	public byte[] genImage(String key) throws AccountCaptchaException;
	
	public boolean validate(String key, String value) throws AccountCaptchaException;
	
	public List<String> getPreDefinedTexts();
	
	public void setPreDefinedTexts(List<String> texts);
	
}
