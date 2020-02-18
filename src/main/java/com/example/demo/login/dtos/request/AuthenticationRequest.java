package com.example.demo.login.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // for json parsing
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
	private String username;
	private String password;
	
}
