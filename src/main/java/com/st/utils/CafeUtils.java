package com.st.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {

	private CafeUtils(){
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responsemessage,HttpStatus httpstatus){
		
		//ResponseEntity<String>("{\"message\":\"something went wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return new ResponseEntity<String>("{\"message\":\""+responsemessage+"\"}",httpstatus);
	}
}
