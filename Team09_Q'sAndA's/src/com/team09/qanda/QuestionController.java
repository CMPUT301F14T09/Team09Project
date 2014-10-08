package com.team09.qanda;

import java.util.ArrayList;

public class QuestionController {
	//Handles the questions displayed to the user on the main screen of the application
	private ThreadList tl;
	private ArrayList<Thread> threads;
	private ArrayList<Post> questions;
	
	public QuestionController() {
		getThreads();
		populateQuestions();
	}
	
	private void getThreads() {
		tl.getThreads();
	}
	
	private void populateQuestions() {
		for (Thread t:threads) {
			questions.add(t.getQuestion());
		}
	}
	
	public ArrayList<Post> getQuestions() {
		return questions;
	}
	
}
