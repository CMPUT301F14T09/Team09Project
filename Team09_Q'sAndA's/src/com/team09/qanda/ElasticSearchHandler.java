package com.team09.qanda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
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
		String URL = "http://localhost:9200/test/";
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	public ArrayList<QuestionThread> getThreads(int numQuestions) {
		int sortStyle = 0;
		return new ArrayList<QuestionThread>();
	}
	
	//sortStyle is id of the element found at the
	//the index of the spinner element found in the string resource
	public ArrayList<QuestionThread> getThreads(int sortStyle,int numQuestions) {
		return new ArrayList<QuestionThread>();
	}

	//returns boolean of whether the threads were successfully saved or not
	//compare with threads currently on server and only add what is new
	//DONT OVERWRITE SERVER
	public boolean saveThreads(ThreadList thread) throws IllegalStateException, IOException {
		HttpPost httpPost = new HttpPost(URL);
		StringEntity stringentity = null;
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
