package com.team09.qanda;

import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

public class ElasticSearchHandler {
	private String URL;
	private String INDEX;
	private String TYPE;
	private static final int DEFAULT=0;
	private ArrayList<QuestionThread> threads=new ArrayList<QuestionThread>();
	private JestClient client;
	private Gson gson;
	
	public ElasticSearchHandler(){
		//URL = "http://cmput301.softwareprocess.es:8080";
		//URL="http://192.168.1.105:9200";
		URL="http://206.75.37.9:9200";
		INDEX="cmput301f14t09";
		TYPE="qthread";
		this.gson=new Gson();
		JestClientFactory f=new JestClientFactory();
		f.setDroidClientConfig(new DroidClientConfig.Builder(URL).discoveryEnabled(true)
				.discoveryFrequency(120, TimeUnit.SECONDS).multiThreaded(true).build());
		client=f.getObject();
	}
	
	//this version defaults to sort by upvotes
	//could change the design of this method to reduce repetition
	//more overloading could be done for when no arguments are passed
	//or just sortstyle is passed and not numQuestions **will be a data type problem 
	//but could be resolved by changing sortStyle to char or something, if necessary**
	public ArrayList<QuestionThread> getThreads() {
		return getThreads(DEFAULT,10);
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	//overloaded more specific version, for sorting
	//this is default version
	public ArrayList<QuestionThread> getThreads(int sortType, int numThreads) {
		String sortQuery=getSortQuery(sortType);
		String query="{"+sortQuery+"\"query\":{\"match_all\":{}}}";
		Search search=(Search) new Search.Builder(query).addIndex(INDEX).addType(TYPE)
				.setParameter("size", numThreads).build();
		try {
			JestResult result=client.execute(search);
			//Log.i("error message",result.getErrorMessage());
			//Log.i("json string",result.getJsonString());
			List<QuestionThread> ts=result.getSourceAsObjectList(QuestionThread.class);
			return new ArrayList<QuestionThread>(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<QuestionThread>(threads);
	}

	

	public boolean saveThread(QuestionThread thread) {
		Index index=new Index.Builder(thread).index(INDEX).type(TYPE).build();
		try {
			client.execute(index);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveThreads(ThreadList thread){
		for(QuestionThread q: thread.getThreads()){
			if(!saveThread(q)) {
				return false;
			}
		}
		return true;
	}

	//method is only here for testing purposes, deleting content from elastic search
	//server is not in requirements
	//return boolean of whether the delete was successful or not
	public boolean delete(Post P) {
		return true;
	}
	
	public ThreadList search(String searchString) {
		return new ThreadList();
	}
	
	public void cleanup() {
		this.client.shutdownClient();
	}
	private String getSortQuery(int sortType) {
		//default means don't sort (should be most upvoted later on)
		String sort="";
		String direction="\"desc\"";
		if(sortType==DEFAULT){
			return sort;
		}
		else if(sortType==R.string.sort_HasPicture){
			sort="hasPictures";
		}
		else if(sortType==R.string.sort_MostUpvotes || sortType==R.string.sort_LeastUpvoted){
			sort="upVotes";
			if(sortType==R.string.sort_LeastUpvoted){
				direction="\"asc\"";
			}
		}
		else if(sortType==R.string.sort_MostRecent || sortType==R.string.sort_Oldest){
			sort="relativeDate";
			if(sortType==R.string.sort_Oldest){
				direction="\"asc\"";
			}
		}
		return "\"sort\":[{\""+sort+"\": {\"order\": "+direction+"}}],";
	}
}
