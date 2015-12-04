package com.lawrence.account;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lawrence.account.captcha.AccountCaptchaService;

public class AccountCaptchaServiceTest {

	private AccountCaptchaService service = null;
	String value1 = "12345";
	String value2 = "abcdef";
	
	@Before
	public void prepare(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-captcha.xml");
		service = (AccountCaptchaService) ctx.getBean("accountCaptchaService");
		
		List<String> preDefinedValue = Arrays.asList(value1, value2);
		service.setPreDefinedTexts(preDefinedValue);
	}
	
	@Test
	public void testGenCaptcha() throws Exception{
		String key = service.genKey();		
		assertNotNull(key);
		
		byte[] imageData = service.genImage(key);
		assertTrue(imageData.length > 0);
		
		FileOutputStream out = null;
		File image = new File("target/" + key + ".jpg");
		try{
			out = new FileOutputStream(image);
			out.write(imageData);
		}finally{
			if(out != null) out.close();
		}
		assertTrue(image.exists() && image.length() > 0);
	}
	
	@Test
	public void testValidateCorrect() throws Exception{
		String key1 = service.genKey();
		service.genImage(key1);
		assertTrue(service.validate(key1, value1));
		
		String key2 = service.genKey();
		service.genImage(key2);
		assertTrue(service.validate(key2, value2));
		
		String key3 = service.genKey();
		service.genImage(key3);
		assertTrue(service.validate(key3, value1));
	}
	
	@Test
	public void testValidateIncorrect() throws Exception{
		String key1 = service.genKey();
		service.genImage(key1);
		assertFalse(service.validate(key1, value2));
	}
}
