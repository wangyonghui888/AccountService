package com.lawrence.account.persist;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.junit.Assert.assertEquals;

public class AccountPersistServiceTest {
	
	private AccountPersistService service;

	private String testId = "testId";
	private String testId2 = "testId2";
	private String email = "example@example.com";
	private String updatedEmail = "updated@example.com";
	
	@Before
	public void prepare() throws AccountPersistException{
		File accountsFile = new File("target/classes/accounts.xml");
		if(accountsFile.exists()) accountsFile.delete();
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");
		service = (AccountPersistService) ctx.getBean("accountPersistService");
		Account account = new Account();
		account.setId(testId);
		account.setName("testName");
		account.setEmail(email);
		account.setPassword("testPassword");
		account.setActivated(false);
		service.createAccount(account);
		
		//create another account for more test cases.
		account.setId(testId2);
		service.createAccount(account);
	}

	@Test
	public void testCreateAccount() throws AccountPersistException{
		Account account = service.readAccount(testId);
		assertEquals(email, account.getEmail());
	}
	
	@Test
	public void testUpdateAccount() throws AccountPersistException{
		Account account = service.readAccount(testId);
		account.setEmail(updatedEmail);
		service.updateAccount(account);
		
		Account testAcc = service.readAccount(testId);
		assertEquals(updatedEmail, testAcc.getEmail());
	}
	
	@Test
	public void testDeleteAccount() throws AccountPersistException{
		boolean flag = service.deleteAccount(testId2);
		assertEquals(true, flag);
		
		Account account = service.readAccount(testId2);
		assertEquals(null, account);
	}
		
}
