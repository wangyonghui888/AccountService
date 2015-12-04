package com.lawrence.account.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.lawrence.account.captcha.AccountCaptchaService;
import com.lawrence.account.persist.Account;

public class AccountServiceTest {
	
	private GreenMail greenMail;
	private AccountService accountService;
	private AccountCaptchaService captchaService;
	private String accountId = "testId";
	private String accountId02 = "testId02";
	private String password = "123456";
	
	@Before
	public void prepare(){
		//1. Ioc
		String[] springConfigFiles = {"account-service.xml",
										"account-email.xml",
										"account-persist.xml",
										"account-captcha.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(springConfigFiles);
		accountService = (AccountService) ctx.getBean("accountService");
		
		//2. prepare datas for captcha image module
		captchaService = (AccountCaptchaService) ctx.getBean("accountCaptchaService");
		List<String> captchaValues = (List) Arrays.asList("hello", "world", "java");
		captchaService.setPreDefinedTexts(captchaValues);
		
		//3. start green mail service
		ServerSetup mailConfig = new ServerSetup(8888, null, ServerSetup.PROTOCOL_SMTP);
		greenMail = new GreenMail(mailConfig);
		greenMail.setUser("Test@accountService.com", "123456");
		greenMail.start();
	}

	@Test
	public void testRegister() throws Exception{
		String captchaKey = captchaService.genKey();
		byte[] captchaImage = captchaService.genImage(captchaKey);
		String captchaValue = "hello";//set in prepare()
		String activateUrl = "http://localhost:8080/account/activate";
		
		//an account to be activated (after activation cases).
		SignUpRequest request = new SignUpRequest();
		request.setId(accountId);
		request.setName("testName01");
		request.setPassword(password);
		request.setPasswordConfirm(password);
		request.setEmail("test01@example.com");
		request.setCaptchaKey(captchaKey);
		request.setCaptchaValue(captchaValue);
		request.setActivateServiceUrl(activateUrl);
		boolean registerFlag = accountService.register(request);
		assertTrue(registerFlag);
		
		//account activation
		greenMail.waitForIncomingEmail(2000, 1);
		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		Message mail = msgs[0];
		String activateLink = GreenMailUtil.getBody(mail);
		String activateKey = activateLink.substring(activateLink.indexOf("=") + 1);
		boolean flag = accountService.activate(activateKey);
		assertTrue(flag);
		
		//another account for more test cases
		String captchaKey2 = captchaService.genKey();
		byte[] captchaImage2 = captchaService.genImage(captchaKey2);
		String captchaValue2 = "world";
		
		SignUpRequest request2 = new SignUpRequest();
		request2.setId(accountId02);
		request2.setName("testName02");
		request2.setPassword(password);
		request2.setPasswordConfirm(password);
		request2.setEmail("test02@example.com");
		request2.setCaptchaKey(captchaKey2);
		request2.setCaptchaValue(captchaValue2);
		request2.setActivateServiceUrl(activateUrl);
		boolean registerFlag2 = accountService.register(request2);
		assertTrue(registerFlag2);

	}
	
	@Test
	public void testLoginBeforeActivation() throws Exception{
		Account account = accountService.login(accountId02, password);
		assertNull(account);
	}
	
	@Test
	public void testLoginAfterActivation() throws Exception{
		Account account = accountService.login(accountId, password);
		assertNotNull(account);
	}
	
	@Test
	public void testLoginAfterActivationIncorrect() throws Exception{
		Account account = accountService.login(accountId, "wrong_password");
		assertNull(account);
	}
	
	@After
	public void after(){
		greenMail.stop();
	}
}
