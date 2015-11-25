package com.lawrence.account.email;

public interface AccountEmailService {

	public void sendMail(String to, String title, String htmlContent) throws AccountEmailException;
}
