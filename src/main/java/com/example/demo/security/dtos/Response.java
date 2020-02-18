package com.example.demo.security.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(content = Include.NON_NULL)
public class Response<T> {
	private boolean errored;
	private T data;
	
	public static <U> Response<U> build(U u){
		return Response.<U>builder()
				.data(u)
				.build();
	}
	
	public static <U> Response<U> buildError(U u){
		return Response.<U>builder()
				.errored(true)
				.data(u)
				.build();
	}

}