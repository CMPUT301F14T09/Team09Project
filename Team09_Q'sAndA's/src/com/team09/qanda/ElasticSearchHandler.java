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
		int sortStyle = 0;
		ThreadList thread = null;
		try{
			//TODO:http string currently invalid need to deal with ids
			HttpGet getRequest = new HttpGet(URL+"999?pretty=1");

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			// We have to tell GSON what type we expect
			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<ThreadList>>(){}.getType();
			// Now we expect to get a Recipe response
			ElasticSearchResponse<ThreadList> esResponse = gson.fromJson(json, elasticSearchResponseType);
			// We get the recipe from it!
			thread = esResponse.getSource();
			
			getRequest.releaseConnection();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return thread.getThreads();
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	//overloaded more specific version, for sorting
	public ArrayList<QuestionThread> getThreads(int sortStyle,int numQuestions) {
		return new ArrayList<QuestionThread>();
	}

	//returns boolean of whether the threads were successfully saved or not
	//compare with threads currently on server and only add what is new
	//DONT OVERWRITE SERVER
	public boolean saveThreads(ThreadList thread) throws IllegalStateException, IOException {
		HttpPost httpPost = new HttpPost(URL);
		StringEntity stringentity = null;
		
		//TODO:need to change this section to deal with QuestionThreads and not ThreadList
		try {
			stringentity = new StringEntity(gson.toJson(thread));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setHeader("Accept","application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);
		HttpEntity entity = response.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
		String output;
		System.err.println("Output from Server -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}

		try {
			
			EntityUtils.consume(entity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.releaseConnection();
		//TODO: need to fix refresh, because design change
		//thread.refresh();
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
