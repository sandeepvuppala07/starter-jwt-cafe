
package com.st.security;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class Jwtfilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtutil;

	@Autowired
	private CustomerUsersDetailsService customerUsersDetailsService;

	Claims claims = null;
	private String userName = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("Jwtfilter --> doFilterInternal() --> request.getServletPath()" + request.getServletPath());
		if (request.getServletPath().matches("/user/login|/user/forgotpassword|/user/signup")) {// /user/signup|

			filterChain.doFilter(request, response);
		} else {

			String authorizationheader = request.getHeader("Authorization");
			String token = null;
			if (authorizationheader != null && authorizationheader.startsWith("Bearer ")) {
				token = authorizationheader.substring(7);
				userName = jwtutil.extractUserName(token);
				claims = jwtutil.extractAllClaims(token);
			}
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = customerUsersDetailsService.loadUserByUsername(userName);
				if (jwtutil.validToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(upat);

				}
			}
			filterChain.doFilter(request, response);
		}

	}

	public boolean isAdmin() {

		return "admin".equalsIgnoreCase((String) claims.get("role"));
	}

	public boolean isUser() {
		return "user".equalsIgnoreCase((String) claims.get("role"));
	}

	public String getCurrentUser() {
		return userName;
	}
}
