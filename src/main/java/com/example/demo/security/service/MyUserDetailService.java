package com.example.demo.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.user.entity.User;

public interface MyUserDetailService extends UserDetailsService {
	
	User loadMyUserByUsername(String username);

}
