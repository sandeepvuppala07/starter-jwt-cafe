package com.st.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.st.domain.Cafeuser;
import com.st.dto.Empdto;
import com.st.wrapper.CafeUserWrapper;

public interface UserService {

	public ResponseEntity<String> userSignup(Map<String, String> requestMap);
	//public void testSaveCafeUser(Cafeuser cafeuser);
	
	public ResponseEntity<String> login(@RequestBody Map<String, String> loginMap);
	
	public List<CafeUserWrapper> getAllUsersData();
	
	public ResponseEntity<String> update(@RequestBody Map<String, String> updateMap);
	
	public  ResponseEntity<String> checkToken();
	
	public  ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordMap);
	
	public  ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> forgotMap);
	
	public ResponseEntity<String> saveEmp( Empdto empdto);
	
}
