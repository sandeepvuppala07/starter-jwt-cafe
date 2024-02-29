package com.st.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.st.domain.Cafeuser;
import com.st.wrapper.CafeUserWrapper;

public interface CafeUserRepo extends JpaRepository<Cafeuser, Integer>{
	
	@Query(value="select * from cafeuser  where cfemail=?1", nativeQuery = true)
	Cafeuser findUserByEmail(String emailid);
	
    // parameterized constructor
	@Query(value="select cfuid, cfname, cfemail, cfcontactno, cfstatus from cafeuser cu where cu.cfrole='user' ", nativeQuery = true)
	List<Object[]> getAllUsersData();
	
	@Query(value ="select cfemail from cafeuser cu where cu.cfrole='admin' ", nativeQuery = true)
	List<String> getAllAdmins();
	
	
}
