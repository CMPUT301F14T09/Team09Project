package com.team09.qanda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ElasticSearchHandler {
	private HttpClient httpclient = new DefaultHttpClient();
	private Gson gson = new Gson();
	private String URL;
	private static final int DEFAULT = 0; //sort by upvotes query used by default
	
	public ElasticSearchHandler(){
		//URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t09/";
		//TODO: URL is my current local elasticsearch server for easier testing/debugging
		String URL = "http://localhost:9200/test/";
	}
	
	/**
	 * get the http response and return json string
	 * copied from lab Oct 23,2014
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	//this is default version
	//could change the design of this method to reduce repetition
	public ArrayList<QuestionThread> getThreads(int numQuestions) {
		return getThreads(numQuestions, DEFAULT);
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	//overloaded more specific version, for sorting
	public ArrayList<QuestionThread> getThreads(int sortStyle, int numQuestions) {
		ThreadList thread = null;
		try{
			//TODO:http string currently invalid need to deal with ids
			HttpGet getRequest = new HttpGet(URL+"999?pretty=1");

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<ThreadList>>(){}.getType();
			ElasticSearchResponse<ThreadList> esResponse = gson.fromJson(json, elasticSearchResponseType);
			thread = esResponse.getSource();
			
			getRequest.releaseConnection();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return thread.getThreads();
	}

	private boolean saveQuestions(QuestionThread q) {
		HttpPost httpPost = new HttpPost(URL + q.hashCode());
		StringEntity stringentity = null;
		
		//TODO:need to change this section to deal with QuestionThreads and not ThreadList
		try {
			stringentity = new StringEntity(gson.toJson(q));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		httpPost.setHeader("Accept","application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);
		HttpEntity entity = response.getEntity();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent()));
		} catch (IllegalStateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
		}
		String output;
		System.err.println("Output from Server -> ");
		try {
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}
			EntityUtils.consume(entity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		httpPost.releaseConnection();
		//TODO: need to fix refresh, because design change
		//thread.refresh();
		return true;
	}
	
	//returns boolean of whether the threads were successfully saved or not
	//compare with threads currently on server and only add what is new
	//DONT OVERWRITE SERVER
	public boolean saveThreads(ThreadList thread) throws IllegalStateException, IOException {
		for (QuestionThread q: thread.getThreads()){
			if(!saveQuestions(q)){
				return false;
			}
		}
		return true;
	}

	//method is only here for testing purposes, deleting content from elastic search
	//server is not in requirements
	//return boolean of whether the delete was successful or not
	public boolean delete(Post p){
		//TODO:http string currently invalid need to deal with ids
		HttpDelete httpDelete = new HttpDelete(URL + "999");
		httpDelete.addHeader("Accept","application/json");

		try{
			HttpResponse response = httpclient.execute(httpDelete);
		
			String status = response.getStatusLine().toString();
			System.out.println(status);
		
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}
			EntityUtils.consume(entity);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		httpDelete.releaseConnection();
		return true;
	}
	
	public ThreadList search(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

}
