package com.example.demo.security.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.login.dtos.request.AuthenticationRequest;
import com.example.demo.login.dtos.response.AuthenticationResponse;
import com.example.demo.security.dtos.Response;
import com.example.demo.security.service.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("authenticate")
	public ResponseEntity<Response<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {

		// Step 1- Authentication
		try {
			AuthenticationResponse response = authenticationService.authenticate(request);
			Response<AuthenticationResponse> pdmsResponse = Response.build(response);
			return ResponseEntity.ok(pdmsResponse);
		} catch (Exception ex) {
			String msg = String.format("Authentication Failed : %s",ex.getMessage().toUpperCase());
			log.error(msg);
			throw ex;
		}
	}
	
	@GetMapping("hello")
	public String hello() {
		return "hello";
	}

}
