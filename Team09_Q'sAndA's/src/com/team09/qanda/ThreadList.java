package com.team09.qanda;

import java.util.ArrayList;

import com.google.gson.Gson;

public class ThreadList extends QModel<QView> {
	private ArrayList<QuestionThread> threads;
	private transient Gson gson;
	private String sortType;
	private int numThreads;
	
	public ThreadList() {
		gson=new Gson();
		numThreads=10;
		sortType="default";
		this.threads=new ArrayList<QuestionThread>();
	}
	
	public ThreadList(String sortType) {
		gson=new Gson();
		numThreads=10;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	
	public ThreadList(String sortType, int numThreads) {
		gson=new Gson();
		this.numThreads=numThreads;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	
	public String getSortType() {
		return this.sortType;
	}
	
	public int getNumThreads() {
		return this.numThreads;
	}
	
	public void setNumThreads(int num) {
		this.numThreads=num;
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
	
	public void setThreads(ArrayList<QuestionThread> threads) {
		this.threads=threads;
		notifyViews();
	}

	public QuestionThread get(int i) {
		return threads.get(i);
	}
	
	public String jsonify() {
		return gson.toJson(this);
	}

}
