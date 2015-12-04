package com.lawrence.account.email.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.lawrence.account.email.AccountEmailException;
import com.lawrence.account.email.AccountEmailService;

public class AccountEmailServiceImpl implements AccountEmailService{
	
	private JavaMailSender sender;
	
	private String systemEmail;

	@Override
	public boolean sendMail(String to, String subject, String htmlContent) throws AccountEmailException {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setFrom(systemEmail);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);
			
			sender.send(message);
			return true;
		} catch (MessagingException e) {
			throw new AccountEmailException("Fail to send email", e);
		}
	}

	public JavaMailSender getSender() {
		return sender;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public String getSystemEmail() {
		return systemEmail;
	}

	public void setSystemEmail(String systemEmail) {
		this.systemEmail = systemEmail;
	}

}
