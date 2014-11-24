package com.team09.qanda.esearch;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

/**
 * 
 * This class is used to connect with ElasticSearch and receive questionsThreads and upload new questionsThreads.
 *
 */

public class ElasticSearchHandler {
	private String URL;
	private String INDEX;
	private String TYPE;
	private String queryAll;
	private ArrayList<QuestionThread> threads=new ArrayList<QuestionThread>();
	private JestClient client;
	private Gson gson;
	
	public ElasticSearchHandler(){
		this("http://cmput301.softwareprocess.es:8080/","cmput301f14t09","qthread");
	}
	public ElasticSearchHandler(String URL,String Index,String Type){
		this.URL = URL;
		INDEX=Index;
		TYPE=Type;
		this.queryAll="{\"query\":{\"match_all\":{}}}";
		this.gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		JestClientFactory f=new JestClientFactory();
		f.setDroidClientConfig(new DroidClientConfig.Builder(URL).discoveryEnabled(true)
				.discoveryFrequency(120, TimeUnit.SECONDS).connTimeout(10000).multiThreaded(true).build());
		client=f.getObject();
	}
	
	//this version defaults to sort by upvotes
	//could change the design of this method to reduce repetition
	//more overloading could be done for when no arguments are passed
	//or just sortstyle is passed and not numQuestions **will be a data type problem 
	//but could be resolved by changing sortStyle to char or something, if necessary**
	public ArrayList<QuestionThread> getThreads() {
		return getThreads(new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort(),10);
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	//overloaded more specific version, for sorting
	//this is default version
	public ArrayList<QuestionThread> getThreads(Sort sort, int numThreads) {
		return getThreads(queryAll,sort,numThreads);
	}
	
	public ArrayList<QuestionThread> getThreads(String query,Sort sort, int numThreads) {
		Search search=(Search) new Search.Builder(query).addIndex(INDEX).addType(TYPE)
				.setParameter("size", numThreads).addSort(sort).build();
		try {
			JestResult result=client.execute(search);
			ESResults r=gson.fromJson(result.getJsonString(), ESResults.class);
			ArrayList<ESResult> esResults=r.getHits().getHits();
			threads.clear();
			for (int i=0;i<esResults.size();i++) {
				QuestionThread qt=esResults.get(i).get_source();
				qt.setId(esResults.get(i).get_id());
				threads.add(qt);
			}
			return threads;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return threads;
	}
	
	private String makeQueryString(String id) {
		return "{\"query\": {\"query_string\": {\"query\": \""+id+"\"}}}";
	}
	
	public QuestionThread getThread(String id) {
		String query=makeQueryString(id);
		Search search=(Search) new Search.Builder(query).addIndex(INDEX).addType(TYPE).build();
		try {
			JestResult result=client.execute(search);
			ESResults r=gson.fromJson(result.getJsonString(), ESResults.class);
			QuestionThread qt=r.getHits().getHits().get(0).get_source();
			return qt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	
	public boolean saveThread(QuestionThread thread, String id) {
		Index index=new Index.Builder(thread).index(INDEX).type(TYPE).id(id).build();
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
	
	public ArrayList<QuestionThread> search(String searchString) {
		String query="{\"query\":{\"filtered\": {\"query\":{\"query_string\":{\"fields\": [\"answers.text\", \"question.text\"],\"query\": \"/"+searchString+".*/\"}},\"filter\": {}}}}";
		return getThreads(query, new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort(), 10);
	}
	
	public void cleanup() {
		this.client.shutdownClient();
	}
}
