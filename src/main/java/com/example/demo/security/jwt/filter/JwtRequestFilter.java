package com.example.demo.security.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.dtos.ErrorResponse;
import com.example.demo.security.dtos.Response;
import com.example.demo.security.jwt.utils.JwtUtils;
import com.example.demo.security.service.MyUserDetailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private MyUserDetailService userDetailService;
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader(JwtUtils.AUTHORIZATION_HEADER);
		String username = null;
		String jwt = null;
		
		try {
			if (authorizationHeader != null && authorizationHeader.startsWith(JwtUtils.AUTHORIZATION_HEADER_VALUE_PREFIX+" ")) {
				log.info("validating authorization header - starts");
				jwt = authorizationHeader.substring(7);
				try {
					username = jwtUtils.extractUsername(jwt);
				}catch (ExpiredJwtException e) {
					log.info("JWT expired, refreshing JWT...");
					throw e;
				}finally {
			
				}				
				log.info("validating authorization header - success");
			} else {
				log.error("Authorization header is null or empty.");
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				log.info("authentication process statrted for currently logged-in user - " + username);
				UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
				
				if (jwtUtils.validateToken(jwt, userDetails)) {
					// passing all the value as null to mark authentication flag as true
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							null, null,null);
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					log.info("authentication for currently logged-in user - " + username + " is successfull");
					log.info("currently logged-in user - " + username);
				}
			}
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {
			log.error("Authentication failed, cause - "+e.getMessage().toUpperCase());
			ErrorResponse<String> error = ErrorResponse.<String>builder().message(e.getMessage()).build();
			Response<ErrorResponse<String>> erroredResponse = Response.buildError(error);
			response.setContentType("application/json");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().write(convertObjectToJson(erroredResponse));
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
	}
	
	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

}
