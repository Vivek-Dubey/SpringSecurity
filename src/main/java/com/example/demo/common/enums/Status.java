package com.example.demo.common.enums;

import lombok.Getter;

public enum Status {
	
	ACTIVE(1),
	INACTIVE(2),
	DELETED(3);
	
	@Getter
	private final int value;
	
	private Status(int value) {
		this.value = value;
	}

}
