package com.team09.qanda;

import java.util.ArrayList;
import java.util.Arrays;

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

	public void clear() {
		// TODO Auto-generated method stub
		
	}
	//convenience function
	public void addThread(QuestionThread...questionThreads ) {
		threads.addAll(Arrays.asList(questionThreads));
		
	}

	public QuestionThread get(int i) {
		return threads.get(i);
	}

}
