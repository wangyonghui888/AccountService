package com.lawrence.account.captcha;

import java.util.Random;

public class RandomGenerator {

	private static String range = "0123456789abcdefghijklmnopqrstuvwxyz";
	
	public static synchronized String getRandomString(){
		int len = range.length();
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 8; i++){
			builder.append(range.charAt(random.nextInt(len)));
		}
		return builder.toString();
	}
	
}
