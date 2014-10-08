package com.team09.qanda;

public class User {
	private String name;
	
	public User() {
		this.name="User"+System.currentTimeMillis();
	}
	
	public User(String name) {
		this.name=name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}
	
}