package com.example.demo.common.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.security.dtos.ErrorResponse;
import com.example.demo.security.dtos.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

	@ExceptionHandler(value = {AuthenticationException.class})
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseEntity<Response<ErrorResponse<String>>> authenticationException(AuthenticationException e){
		log.error(e.getMessage());
		ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
											.message(e.getMessage())
											.build();
		Response<ErrorResponse<String>> response = Response
													.<ErrorResponse<String>>buildError(errorResponse);
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(response);
	}
	
	@ExceptionHandler(value = { BadCredentialsException.class})
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseEntity<Response<ErrorResponse<String>>> badCredentialsException(BadCredentialsException e){
		log.error(e.getMessage());
		ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
											.message(e.getMessage())
											.build();
		Response<ErrorResponse<String>> response = Response
													.<ErrorResponse<String>>buildError(errorResponse);
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(response);
	}
	
	@ExceptionHandler(value = {Exception.class})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Response<ErrorResponse<String>>> exception(Exception e){
		log.error(e.getMessage());
		ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
											.message("Something Went Wrong!!")
											.error(e.getMessage())
											.build();
		Response<ErrorResponse<String>> response = Response
													.<ErrorResponse<String>>buildError(errorResponse);
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
	}

	
}
