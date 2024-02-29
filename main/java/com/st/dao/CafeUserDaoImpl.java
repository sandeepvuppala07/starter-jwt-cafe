package com.st.dao;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.st.constants.UserConstants;
import com.st.domain.Cafeuser;
import com.st.repository.CafeUserRepo;
import com.st.utils.CafeUtils;
import com.st.wrapper.CafeUserWrapper;

@Component
public class CafeUserDaoImpl {

	@Autowired
	private CafeUserRepo cafeuserrepo;
	
	
	private static final Logger log=LoggerFactory.getLogger(CafeUserDaoImpl.class);
			
	public Cafeuser findUser(String  email) {
		System.out.println("CafeUserDaoImpl --> findUser() email: "+email);

		//cafeuserrepo.save(cafeuser);
		Cafeuser cafeuser=cafeuserrepo.findUserByEmail(email);
		log.info(""+cafeuser);
		return cafeuser;

	}
	
	public ResponseEntity<String> saveUser(Cafeuser cafeuser){
		
		System.out.println("CafeUserDaoImpl --> saveUser() cafeuser: "+cafeuser);

		cafeuserrepo.save(cafeuser);
		return CafeUtils.getResponseEntity(UserConstants.SUCCESSFULLY_REGISTERED, HttpStatus.OK);
	}
	
	public List<Object[]> getAllUsersData(){
		return cafeuserrepo.getAllUsersData();
	}
	
	public  Optional<Cafeuser> findById(int id){
		
	    Optional<Cafeuser> cafeuserOpt=cafeuserrepo.findById(id);
	    return cafeuserOpt;
	}
	
	
	  public List<String> getAllAdmins(){
	  
	    return cafeuserrepo.getAllAdmins();
	  
	  }
	 
	
	public Integer updateStatus(Cafeuser cafeuser) {
		cafeuserrepo.save(cafeuser);
		return cafeuser.getCfuid();
	}
	
	
}




