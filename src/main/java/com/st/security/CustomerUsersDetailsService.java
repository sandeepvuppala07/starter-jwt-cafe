
package com.st.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.st.dao.CafeUserDaoImpl;

@Service
public class CustomerUsersDetailsService implements UserDetailsService {

	@Autowired
	CafeUserDaoImpl cafeUserDaoImpl;

	private com.st.domain.Cafeuser userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("CustomerUsersDetailsService --> loadUserByUsername() username: "+username);

		userDetail = cafeUserDaoImpl.findUser(username);
		if (!Objects.isNull(username))
			return new User(userDetail.getCfemail(), userDetail.getCfpassword(), new ArrayList<>());
		else
			throw new UsernameNotFoundException("User name not found");

	}

	public com.st.domain.Cafeuser getUserDetail() {
		System.out.println("CustomerUsersDetailsService --> getUserDetail() ");

		return userDetail;
	}

}
