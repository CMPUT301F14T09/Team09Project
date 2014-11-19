package com.team09.qanda.models;

import io.searchbox.core.search.sort.Sort;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.team09.qanda.esearch.SimpleSortFactory;
import com.team09.qanda.views.QView;

public class ThreadList extends QModel<QView> {
	private ArrayList<QuestionThread> threads;
	private transient Gson gson;
	private transient Sort sortType;
	private int numThreads;
	
	public ThreadList() {
		gson=new Gson();
		numThreads=10;
		sortType=new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort();
		this.threads=new ArrayList<QuestionThread>();
	}
	
	public ThreadList(Sort sortType) {
		gson=new Gson();
		numThreads=10;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	
	public ThreadList(Sort sortType, int numThreads) {
		gson=new Gson();
		this.numThreads=numThreads;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	public void setSortType(Sort sort){
		this.sortType=sort;
	}
	public Sort getSortType() {
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
