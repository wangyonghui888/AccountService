package com.lawrence.account.captcha.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.InitializingBean;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.lawrence.account.captcha.AccountCaptchaException;
import com.lawrence.account.captcha.AccountCaptchaService;
import com.lawrence.account.captcha.RandomGenerator;

public class AccountCaptchaServiceImpl implements AccountCaptchaService, InitializingBean{
	
	private List<String> preDefinedTexts;
	
	private DefaultKaptcha producer;
	
	private int count;
	
	private Map<String, String> captchaDict = new HashMap<String, String>();
	
	 //Get string defined in preDefinedTexts as captcha value repeatly.
	private String getCaptchaString(){
		if(preDefinedTexts != null && preDefinedTexts.size() > 0){
//			return preDefinedTexts.get(count ++ % preDefinedTexts.size());
			String text = preDefinedTexts.get(count);
			count = (count + 1) % preDefinedTexts.size();
			return text;
		}else{
			return producer.createText();
		}
	}

	@Override
	public String genKey() throws AccountCaptchaException {
		String key = RandomGenerator.getRandomString();
		String value = getCaptchaString();
		captchaDict.put(key, value);
		return key;
	}

	@Override
	public byte[] genImage(String key) throws AccountCaptchaException {
		byte[] result = null;
		String value = captchaDict.get(key);
		if(value == null) throw new AccountCaptchaException("Captcha key not found: " + key);
		BufferedImage image = producer.createImage(value);
		ByteArrayOutputStream out = null;
		try{
			out = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", out);
			result = out.toByteArray();
		}catch(Exception e){
			throw new AccountCaptchaException("fail to write image.", e);
		}finally{
			try{
				if(out != null) out.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public boolean validate(String key, String value) throws AccountCaptchaException {
		String storagedValue = captchaDict.get(key);
		if(storagedValue == null) throw new AccountCaptchaException("Captcha key not found: " + key);
		if(storagedValue.equals(value)) {
			captchaDict.remove(key);
			return true;
		}
		//TODO remove useless captcha when fail to validate: not yet finish. 
		return false;
	}

	@Override
	public List<String> getPreDefinedTexts() {
		return preDefinedTexts;
	}

	@Override
	public void setPreDefinedTexts(List<String> texts) {
		preDefinedTexts = texts;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		producer = new DefaultKaptcha();
		producer.setConfig(new Config(new Properties()));
	}

}
