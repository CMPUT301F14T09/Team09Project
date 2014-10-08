package com.team09.qanda;


public class QuestionController {
	//Handles the questions displayed to the user on the main screen of the application
	private ThreadList tl;
	
	public QuestionController(ThreadList threadList) {
		this.tl=threadList;
	}
	
	public void addThread(QuestionThread thread) {
		tl.addThread(thread);
	}
	
}
