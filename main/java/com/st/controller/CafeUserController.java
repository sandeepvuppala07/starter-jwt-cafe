package com.st.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.st.constants.UserConstants;
import com.st.domain.Cafeuser;
import com.st.dto.Empdto;
import com.st.service.UserService;
import com.st.utils.CafeUtils;
import com.st.wrapper.CafeUserWrapper;

@RestController
@RequestMapping(value="/user")
public class CafeUserController {

	private static final Logger log=LoggerFactory.getLogger(CafeUserController.class);
	@Autowired
	private UserService userservice;
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> userSignup(@RequestBody Map<String, String> requestMap){
		
		System.out.println("CafeUserController --> userSignup() requestMap: "+requestMap);
		log.info(""+requestMap);
		try {
			return userservice.userSignup(requestMap);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
       //return new ResponseEntity<String>("{\"message\":\"something went wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	/*
	 * @PostMapping("/saveuser") public void testSaveCafeUser(@RequestBody Cafeuser
	 * cafeuser) {
	 * 
	 * log.info("Cafe User from api"+cafeuser);
	 * userservice.testSaveCafeUser(cafeuser); }
	 * 
	 * @GetMapping("/test") public void test() { System.out.println("Working API");
	 * }
	 */
	
	@RequestMapping("/test")
	public String test() {
		
		System.out.println("Test in controller working");
		return "Working";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> loginMap){
		System.out.println("CafeUserController --> login() requestMap: "+loginMap);

		return userservice.login(loginMap);
	}
	
	@GetMapping("/getdata")
	public ResponseEntity<List<CafeUserWrapper>> getAllUsersData(){
		System.out.println("CafeUserController --> getAllUsersData() ");
		try {
			return ResponseEntity.ok(userservice.getAllUsersData());
		    	
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		//error --> return empty list
		return new ResponseEntity<List<CafeUserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/updatestatus")
	public ResponseEntity<String> update(@RequestBody Map<String, String> updateMap){
		
		try {
			
			return userservice.update(updateMap);
			
		} catch (Exception exp) {
			
		}
		return  CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping("/checktoken")
	public ResponseEntity<String> checkToken(){
		
		try {
			
			return userservice.checkToken();
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<String> changePassword(@RequestBody Map<String, String> passwordMap){
		
		
		try {
			return userservice.changePassword(passwordMap);
			
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> forgotMap){
		
		try {
			
			return userservice.forgotPassword(forgotMap);
			
		} catch (Exception exp) {
			exp.printStackTrace();
			
		}
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/savemp")
	public ResponseEntity<String> saveEmp(@RequestBody String userRequest) throws JSONException{
		JSONObject jsonObject = new JSONObject(userRequest);
		
		String ename = jsonObject.getString("ename");
		String job = jsonObject.getString("job");
		Integer mgr = jsonObject.getInt("mgr");
		Integer comm = jsonObject.getInt("comm");
		Integer sal = jsonObject.getInt("sal");
		Integer deptno = jsonObject.getInt("deptno");
		Empdto empdto = new Empdto();
		empdto.setEname(ename);
		empdto.setJob(job);
		empdto.setMgr(mgr);
		empdto.setComm(comm);
		empdto.setSal(sal);
		empdto.setDeptno(deptno);
		
		
	return userservice.saveEmp(empdto);
		
	}
}



