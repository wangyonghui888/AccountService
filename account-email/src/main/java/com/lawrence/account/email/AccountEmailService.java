package com.lawrence.account.email;

public interface AccountEmailService {

	public boolean sendMail(String to, String subject, String htmlContent) throws AccountEmailException;
}
