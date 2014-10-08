package com.team09.qanda;

import java.util.ArrayList;

public class ThreadList {
	private ElasticSearchHandler esh;
	private ArrayList<QuestionThread> threads;
	private ArrayList<Post> questions;
	
	public ThreadList() {
		retrieveThreads();
		populateQuestions();
	}
	
	private void retrieveThreads() {
		this.threads=esh.getThreads();
	}
	
	private void populateQuestions() {
		for (QuestionThread t:threads) {
			questions.add(t.getQuestion());
		}
	}
	
	public ArrayList<QuestionThread> getThreads() {
		return this.threads;
	}
	
	public ArrayList<Post> getQuestions() {
		return this.questions;
	}

}
