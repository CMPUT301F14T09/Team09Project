package com.team09.qanda;

public class User extends QModel<QView> {
	private String name;
	
	public User() {
		this.name="User"+System.currentTimeMillis();
	}
	
	public User(String name) {
		this.name=name;
	}
	
	public void setName(String name) {
		this.name=name;
		notifyViews();
	}
	
	public String getName() {
		return this.name;
	}
	
}