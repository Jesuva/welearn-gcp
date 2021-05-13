package com.welearn.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User {
	@Email		
	private String email;
    @Size(min=3)  
	private String password;
    @Size(min=3,max=20)
	private String name;
	private String role;
	
	public User(String name,String email,String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	public User() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
