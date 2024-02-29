package com.st.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.st.constants.UserConstants;
import com.st.dao.CafeUserDaoImpl;
import com.st.domain.Cafeuser;
import com.st.domain.Emp;
import com.st.dto.Empdto;
import com.st.repository.EmpRepo;
import com.st.security.CustomerUsersDetailsService;
import com.st.security.JWTUtil;
import com.st.security.Jwtfilter;
import com.st.utils.CafeUtils;
import com.st.utils.MailUtils;
import com.st.wrapper.CafeUserWrapper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiveImpl implements UserService{
	private static final Logger log=LoggerFactory.getLogger(UserServiveImpl.class);
	
  @Autowired
  private CafeUserDaoImpl cafeuserdaoimpl;

  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private CustomerUsersDetailsService customerUsersDetailsService;
  
  @Autowired
  private JWTUtil jWTUtil;
  
  @Autowired
  private Jwtfilter jwtfilter;
  
  @Autowired
  private MailUtils mailUtils;
  
  @Autowired
  EmpRepo empRepo;
  
  @Autowired
  ModelMapper modelmapper;
	
	@Override
	public ResponseEntity<String> userSignup(Map<String, String> requestMap) {
	   
		System.out.println("UserServiveImpl --> userSignup() requestMap: "+requestMap);
		log.info("ServiveImpl-userSignup  requestMap="+requestMap);
		try {
				if(validateSignup(requestMap)) {
				    Cafeuser cafeuser = findUser(requestMap.get("cfemail"));
					if(Objects.isNull(cafeuser))
						return cafeuserdaoimpl.saveUser(getUserFromMap(requestMap));
					else
						return CafeUtils.getResponseEntity(UserConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
				}
				else 
				{
					
					return CafeUtils.getResponseEntity(UserConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
				
	}
	
	private boolean validateSignup(Map<String, String> requestMap) {
		
		if (requestMap.containsKey("cfcontactno") && requestMap.containsKey("cfemail") &&
				requestMap.containsKey("cfpassword") && requestMap.containsKey("cfname")) {
			return true;
			
		} else {
	
			return false;
		}
				
	}

	private Cafeuser findUser(String email) {
		
		Cafeuser cafeuser = cafeuserdaoimpl.findUser(email);
		return cafeuser;
	}
	
	private Cafeuser getUserFromMap(Map<String, String> requestmap) {
		Cafeuser cafeuser= new Cafeuser();
		cafeuser.setCfname(requestmap.get("cfname"));
		cafeuser.setCfemail(requestmap.get("cfemail"));
		cafeuser.setCfpassword(requestmap.get("cfpassword"));
		cafeuser.setCfcontactno(requestmap.get("cfcontactno"));
		cafeuser.setCfrole(requestmap.get("cfrole"));
		cafeuser.setCfstatus(requestmap.get("cfstatus"));
		return cafeuser;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> loginMap) {
		
		System.out.println("UserServiveImpl --> login() loginMap: "+loginMap);

		log.info("ServiceImpl --> login() loginmap: "+loginMap);
		try {
			Authentication auth = authenticationManager.authenticate(
					                new UsernamePasswordAuthenticationToken(loginMap.get("cfemail"),loginMap.get("cfpassword")));
			if(auth.isAuthenticated()) {
				if(customerUsersDetailsService.getUserDetail().getCfstatus().equalsIgnoreCase("true")){ //verify admin approved or not
					String token=jWTUtil.generateToken(customerUsersDetailsService.getUserDetail().getCfemail(),
							                               customerUsersDetailsService.getUserDetail().getCfrole());
					return ResponseEntity.ok(token);
				}
				else {
					return new ResponseEntity<String>("Wait for Admin Approval",HttpStatus.BAD_REQUEST);
				}
			}
		}
		catch(Exception exp){
			log.error(""+exp);
		}
		
		return new ResponseEntity<String>(UserConstants.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
	}

	@Override
	public List<CafeUserWrapper> getAllUsersData() {
		
		System.out.println("UserServiveImpl --> getAllUsersData() ");
		try {
			
			if(jwtfilter.isAdmin()) {
				
				List<CafeUserWrapper> cafeUserWrapperList = new ArrayList<>();
				
				List<Object[]> cafeUserWrapperdata = cafeuserdaoimpl.getAllUsersData();
				for(Object[] cuw:cafeUserWrapperdata) {
					
					CafeUserWrapper cafeUserWrapper = new CafeUserWrapper();
					cafeUserWrapper.setUserId(Integer.parseInt(String.valueOf(cuw[0])));
					cafeUserWrapper.setUserName(String.valueOf(cuw[1]));
					cafeUserWrapper.setEmail(String.valueOf(cuw[2]));
					cafeUserWrapper.setContactNo(String.valueOf(cuw[3]));
					cafeUserWrapper.setStatus(String.valueOf(cuw[4]));
					cafeUserWrapperList.add(cafeUserWrapper);
				}
				return cafeUserWrapperList;
			 
			}
				
			else
				return new ArrayList<>();
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> updateMap) {
		try {
			if(jwtfilter.isAdmin()) {
				Optional<Cafeuser> optionalCafeUser =cafeuserdaoimpl.findById(Integer.parseInt(updateMap.get("cfuid")));
				
				if(optionalCafeUser.isPresent()) {
					//cafeuserdaoimpl.updateStatus(updateMap.get("cfstatus"), Integer.parseInt(updateMap.get("cfuid")));
					Cafeuser cafeuser=optionalCafeUser.get();
					cafeuser.setCfstatus(updateMap.get("cfstatus"));
				    Integer upduserId = cafeuserdaoimpl.updateStatus(cafeuser);
				    
				    //send mail 
				    sendMail(updateMap.get("cfstatus"), cafeuser.getCfemail(), jwtfilter.getCurrentUser(), cafeuserdaoimpl.getAllAdmins());
				    
				    return CafeUtils.getResponseEntity("User :"+upduserId+" Status updated successfully", HttpStatus.OK);
				}
				else
					return CafeUtils.getResponseEntity("User Id doesn't Exists", HttpStatus.OK);
			}
			else {
				return CafeUtils.getResponseEntity(UserConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception exp) {
			
			exp.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	
	private void sendMail(String status,String emailTO,String currentAdmin, List<String> adminCCList) {
		
		adminCCList.remove(jwtfilter.getCurrentUser());//removing current admin 
		
		if(status!=null && status.equalsIgnoreCase("true")) {
			
			mailUtils.sendSimpleMail(emailTO, "Account Approved","HI "+emailTO+" ,\n Your CafeUser account has been approved by "+currentAdmin, adminCCList);

		}
		else {
			mailUtils.sendSimpleMail(emailTO, "Account Disabled","HI "+emailTO+" ,\n Your CafeUser account has been disabled by "+currentAdmin, adminCCList);
		}
		
	}

	@Override
	public ResponseEntity<String> checkToken() {
		
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> passwordMap) {
		try {
			Cafeuser cafeuser=findUser(jwtfilter.getCurrentUser());
			if(!cafeuser.equals(null)) {
				if(cafeuser.getCfpassword().equals(passwordMap.get("oldpassword"))) {
					cafeuser.setCfpassword(passwordMap.get("newpassword"));
					cafeuserdaoimpl.saveUser(cafeuser);
					return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK); 
				}
				else
					return CafeUtils.getResponseEntity("Invalid Old Password", HttpStatus.BAD_REQUEST); 
			}
			else
				return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
	}

	
	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> forgotMap) {

		try {
			
			Cafeuser cafeuser= findUser(forgotMap.get("email"));
			if(!Objects.isNull(cafeuser) && !Strings.isNullOrEmpty(cafeuser.getCfemail())) {
				
				mailUtils.forgotMail(cafeuser.getCfemail(),"Login Credentials from CafeManagment System", cafeuser.getCfpassword());
				return CafeUtils.getResponseEntity("Check Your Mail for Credentials", HttpStatus.OK);
			}
			else
				return CafeUtils.getResponseEntity("Check Your Mail for Credentials", HttpStatus.OK);
			
		}
        catch(Exception exp){
    	   exp.printStackTrace();
       }
		
		return CafeUtils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> saveEmp(Empdto empdto) {
		
		Emp emp = modelmapper.map(empdto, Emp.class);
		empRepo.save(emp);
		return CafeUtils.getResponseEntity("Emp saved Successfully", HttpStatus.OK);
	}
	
	
}









