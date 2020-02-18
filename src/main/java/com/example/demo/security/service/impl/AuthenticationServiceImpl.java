package com.example.demo.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.example.demo.login.dtos.request.AuthenticationRequest;
import com.example.demo.login.dtos.response.AuthenticationResponse;
import com.example.demo.security.jwt.utils.JwtUtils;
import com.example.demo.security.service.AuthenticationService;
import com.example.demo.security.service.MyUserDetailService;
import com.example.demo.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailService userDetailService;
	
	@Autowired
	private JwtUtils jwtUtils;
	

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			String msg = String.format("Bad Credentials : %s",e.getMessage().toUpperCase());
			log.error(msg);
			throw e;
		} catch (AuthenticationException e) {
			String msg = String.format("Authentication Exception : %s",e.getMessage().toUpperCase());
			log.error(msg);
			throw e;
		} catch (Exception e) {
			String msg = String.format("Exception : %s",e.getMessage().toUpperCase());
			log.error(msg);
			throw e;
		}
		
		final User principal = userDetailService.loadMyUserByUsername(request.getUsername());
		final String jwt = jwtUtils.generateToken(principal);
		
		return AuthenticationResponse.builder()
				.jwt(jwt)
				.username(principal.getUsername())
				.id(principal.getId())
				.build();
		
	}

}
