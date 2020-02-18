package com.example.demo.security.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.security.dtos.MyUserDetails;
import com.example.demo.security.service.MyUserDetailService;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepository;

@Service
public class MyUserDetailServiceImpl implements MyUserDetailService {

	@Autowired
	private UserRepository userRepository;	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = loadUserByUsernameInternal(username);
		return user.map(MyUserDetails::new).get();
	}
	
	@Override
	public User loadMyUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = loadUserByUsernameInternal(username);
		return user.get();
	}

	private Optional<User> loadUserByUsernameInternal(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("User Not Found!!"));
		return user;
	}
	
}
