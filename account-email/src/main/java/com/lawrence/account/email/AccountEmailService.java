package com.lawrence.account.email;

public interface AccountEmailService {

	public void sendMail(String to, String subject, String htmlContent) throws AccountEmailException;
}
