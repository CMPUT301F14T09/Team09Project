package com.team09.qanda;

import java.util.ArrayList;

public class ElasticSearchHandler {
	private String URL;
	
	public ElasticSearchHandler(){
		URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t09/";
	}
	
	public ArrayList<QuestionThread> getThreads() {
		return new ArrayList<QuestionThread>();
	}

	//returns boolean of whether the threads were successfully saved or not
	public boolean saveThreads() {
		return true;
	}

	public ThreadList search(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

}
