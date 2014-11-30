package com.team09.qanda.models;

import io.searchbox.core.search.sort.Sort;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.team09.qanda.esearch.SimpleSortFactory;
import com.team09.qanda.views.QView;

/**
 * This class represents a list of QuestionThreads, which is used
 * to display the list of questions on the main screen, as well
 * as the questions stored locally.
 *
 */
public class ThreadList extends QModel<QView> {
	private ArrayList<QuestionThread> threads;
	private transient Gson gson;
	private transient Sort sortType;
	private int numThreads;
	
	/**
	 * The default constructor sets up the variables needed by the class.
	 * It creates a default Gson, sets the number of threads to 10, sets
	 * the sort to most upvoted, and makes an empty list for the QuestionThreads.
	 */
	public ThreadList() {
		gson=new Gson();
		numThreads=10;
		sortType=new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort();
		this.threads=new ArrayList<QuestionThread>();
	}
	
	/**
	 * This constructor takes an optional parameter to set the type of
	 * sorting when instantiating the object.
	 * @param sortType The style of sorting to be used.
	 */
	public ThreadList(Sort sortType) {
		gson=new Gson();
		numThreads=10;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	
	/**
	 * This constructor takes optional parameters to set the type of
	 * sorting when instantiating the object, as well as to set
	 * the number of threads it should request from the server.
	 * @param sortType The style of sorting to be used.
	 * @param numThreads The number of threads to be requested from the server.
	 */
	public ThreadList(Sort sortType, int numThreads) {
		gson=new Gson();
		this.numThreads=numThreads;
		this.sortType=sortType;
		this.threads=new ArrayList<QuestionThread>();
	}
	
	/**
	 * This sets the type of sorting to be done. This affects
	 * the query to the server.
	 * @param sort The style of sorting to be used.
	 */
	public void setSortType(Sort sort){
		this.sortType=sort;
	}
	
	/**
	 * Returns the current sorting style of the object
	 * @return The style of sorting being used.
	 */
	public Sort getSortType() {
		return this.sortType;
	}
	
	/**
	 * Returns the current number of threads being requested from the server.
	 * @return The number of threads being requested as an integer
	 */
	public int getNumThreads() {
		return this.numThreads;
	}
	
	/**
	 * Sets the number of threads to be requested from the server.
	 * @param num The number of threads to be requested as an integer.
	 */
	public void setNumThreads(int num) {
		this.numThreads=num;
	}

	/**
	 * Gets the list of threads stored in the object.
	 * @return An ArrayList containing all of the QuestionThreads from the object
	 */
	public ArrayList<QuestionThread> getThreads() {
		return this.threads;
	}
	
	/**
	 * A convenience function to return the questions from all of the
	 * threads in the object
	 * @return An ArrayList containing the questions of all of the threads.
	 */
	public ArrayList<Post> getQuestions() {
		ArrayList<Post> questions=new ArrayList<Post>();
		for (QuestionThread t:threads) {
			questions.add(t.getQuestion());
		}
		return questions;
	}
	
	/**
	 * This sets the current list of QuestionThreads in the object.
	 * It can be used to add or remove a question
	 * @param threads The new list of QuestionThreads
	 */
	public void setThreads(ArrayList<QuestionThread> threads) {
		this.threads=threads;
		notifyViews();
	}

	/**
	 * Gets a single QuestionThread at the specified location from the object
	 * @param i The location of the object to be returned
	 * @return A QuestionThread from the postion in the list.
	 */
	public QuestionThread get(int i) {
		return threads.get(i);
	}

}
