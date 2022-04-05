package com.zensar.olx.db;

import java.util.HashMap;
import java.util.Map;

public class TokenStorage {

	private  static Map<String, String> tokenCache;
	
	static {
		tokenCache=new HashMap<>();
	}
	
	public static String getToken(String key){
		return tokenCache.get(key);
	}
	//This method is responsible for storing token in Cache on server
	//Both token-key and token-value is token itself
	public static void storeToken(String key,String token) {
		tokenCache.put(key, token);
	}
	
	//this method is responible for removing token from server Cache
	//this is written to check if token is present in cache OR not?
	public static String removeToken(String key) {
		System.out.println("---------------");
		System.out.println(tokenCache.get(key));
		System.out.println("-----------------");
		return tokenCache.remove(key);
		
	}
}
