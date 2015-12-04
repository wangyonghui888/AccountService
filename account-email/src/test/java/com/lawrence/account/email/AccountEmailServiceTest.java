package com.lawrence.account.email;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

import static junit.framework.Assert.*;

public class AccountEmailServiceTest {
	
	private GreenMail greenMail;
	
	@Before
	public void startEMailService() throws Exception{
		//cause Permission deny with port below 1024 on Mac OS X, port 8888 should align with email.port in propertie file.
		ServerSetup setup = new ServerSetup(8888, null, ServerSetup.PROTOCOL_SMTP);
		greenMail = new GreenMail(setup);
		greenMail.setUser("greenMailReceiver", "123654");
		greenMail.start();
	}
	
	@After
	public void stopEmailService(){
		greenMail.stop();
	}

	@Test
	public void testSendEmail() throws Exception{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
		AccountEmailService emailService = (AccountEmailService) ctx.getBean("accountEmailService");
		
		//send mail by javax.mail
		String subject = "Subject to test";
		String content = "<h3>Hello World</h3>";
		String receiver = "receiver@gmail.com";
		boolean flag = emailService.sendMail(receiver, subject, content);
		assertTrue(flag);
		
		//retrieve mail through Green Mail
		greenMail.waitForIncomingEmail(2000, 1);
		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		
		Message receivedMail = msgs[0];
		assertEquals(subject, receivedMail.getSubject());
		assertEquals(content, GreenMailUtil.getBody(receivedMail).trim());
	}
}
