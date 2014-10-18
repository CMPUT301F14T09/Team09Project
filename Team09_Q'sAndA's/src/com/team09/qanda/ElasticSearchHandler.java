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
	//compare with threads currently on server and only add what is new
	//DONT OVERWRITE SERVER
	public boolean saveThreads(ThreadList thread) {
		thread.refresh();
		return true;
	}

	//method is only here for testing purposes, deleting content from elastic search
	//server is not in requirements
	//return boolean of whether the delete was successful or not
	public boolean delete(Post p){
		return true;
	}
	
	public ThreadList search(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

}
