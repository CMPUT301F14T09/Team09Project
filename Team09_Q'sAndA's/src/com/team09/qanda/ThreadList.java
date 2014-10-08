package com.team09.qanda;

import java.util.ArrayList;

public class ThreadList {
	private ElasticSearchHandler esh;
	private ArrayList<QuestionThread> threads;
	
	public ThreadList() {
		retrieveThreads();
	}
	
	private void retrieveThreads() {
		this.threads=esh.getThreads();
	}

	
	public ArrayList<QuestionThread> getThreads() {
		return this.threads;
	}
	
	public ArrayList<Post> getQuestions() {
		ArrayList<Post> questions=new ArrayList<Post>();
		for (QuestionThread t:threads) {
			questions.add(t.getQuestion());
		}
		return questions;
	}
	
	public void addThread(QuestionThread thread) {
		threads.add(thread);
	}

}
