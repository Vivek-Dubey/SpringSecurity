package com.example.demo.security.service;

import com.example.demo.login.dtos.request.AuthenticationRequest;
import com.example.demo.login.dtos.response.AuthenticationResponse;

public interface AuthenticationService {

	AuthenticationResponse authenticate(AuthenticationRequest request);
	
}
