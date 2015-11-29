package com.lawrence.account.captcha;

import java.util.List;

public interface AccountCaptchaService {

	public String generateCaptchaKey() throws AccountCaptchaException;
	
	public byte[] generateCaptchaImage() throws AccountCaptchaException;
	
	public boolean validate() throws AccountCaptchaException;
	
	public List<String> getPreDefinedTexts();
	
	public void setPreDefinedTexts(List<String> texts);
	
}
