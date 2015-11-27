package com.lawrence.account.persist.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.lawrence.account.persist.Account;
import com.lawrence.account.persist.AccountPersistException;
import com.lawrence.account.persist.AccountPersistService;

public class AccountPersistServiceImpl implements AccountPersistService{
	
	public static final String ELEMENT_ROOT = "accounts";

	private String file;

	private SAXReader reader = new SAXReader();
	
	private Document readDocument() throws AccountPersistException{
		File dataFile = new File(file);
		if(!dataFile.exists()){
			dataFile.getParentFile().mkdirs();
			Document doc = DocumentHelper.createDocument();
			
			doc.addElement(ELEMENT_ROOT);
			writeDocument(doc);
		}
		try {
			return reader.read(dataFile);
		} catch (DocumentException e) {
			throw new AccountPersistException("Unable to read file.", e);
		}
	}
	
	private void writeDocument(Document doc) throws AccountPersistException{
		Writer out = null;
		
		try {
			out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
			writer.write(doc);
		} catch (IOException e) {
			throw new AccountPersistException("Unable to write document.", e);
		}finally{
			try {
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private Account buildAccount(Element e){
		if(e == null) return null;

		Account account = new Account();
		account.setId(e.elementText("id"));
		account.setEmail(e.elementText("email"));
		account.setName(e.elementText("name"));
		account.setPassword(e.elementText("password"));
		account.setActivated(Boolean.parseBoolean(e.elementText("activated")));
		return account;
	}
	
	private Element getElementById(Document doc, String id) throws AccountPersistException{
		for(Element e : (List<Element>)doc.getRootElement().elements()){
			if(id.equals(e.elementText("id"))) return e;
		}
		return null;
	}
	
	@Override
	public Account createAccount(Account account) throws AccountPersistException {
		Document doc = readDocument();
		Element accountE = doc.getRootElement().addElement("account");
		accountE.addElement("id").setText(account.getId());;
		accountE.addElement("name").setText(account.getName());
		accountE.addElement("email").setText(account.getEmail());
		accountE.addElement("password").setText(account.getPassword());
		accountE.addElement("activated").setText("false");
		writeDocument(doc);
		return account;
	}

	@Override
	public Account readAccount(String id) throws AccountPersistException {
		Document doc = readDocument();
		Element e = getElementById(doc, id);
		return buildAccount(e);
	}

	@Override
	public Account updateAccount(Account account) throws AccountPersistException {
		Document doc = readDocument();
		Element e = getElementById(doc, account.getId());
		e.element("name").setText(account.getName());
		e.element("email").setText(account.getEmail());
		e.element("password").setText(account.getPassword());
		e.element("activated").setText(account.isActivated() + "");
		writeDocument(doc);
		return account;
	}

	@Override
	public boolean deleteAccount(String id) throws AccountPersistException {
		Document doc = readDocument();
		Element e = getElementById(doc, id);
		doc.getRootElement().remove(e);
		writeDocument(doc);
		return true;
	}
	
	public void setFile(String file) {
		this.file = file;
	}

}
