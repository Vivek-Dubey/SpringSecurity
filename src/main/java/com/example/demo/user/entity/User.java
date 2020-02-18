package com.example.demo.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user")
@AllArgsConstructor
@Setter
@Builder
@Getter
@NoArgsConstructor
public class User {

	public User(long id) {
		super();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "user_id")
	@Getter
	@Setter
	private long id;

	@Column(name="user_name",unique = true, length = 100)
	private String username;
	 
	@Column(name ="password", length = 255)
	private String password;
	
	@Column(name="status", length = 1)
	private int status;
	
}
