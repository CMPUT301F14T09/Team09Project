package com.team09.qanda.esearch;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.search.sort.Sort;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

/**
 * 
 * This class deals with all of the communication with the elasticsearch server.
 * It saves and loads questions if the user has a connection.
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
	
	/**
	 * The default constructor passes in the information to start a connection
	 * with the server under the main index and type.
	 */
	public ElasticSearchHandler(){
		this("http://cmput301.softwareprocess.es:8080/","cmput301f14t09","qthread");
	}
	
	/**
	 * And constructor with optional parameters to connect to a different server,
	 * index, or type. Used for testing so questions aren't pushed to
	 * the main index when tests are run.
	 * @param URL The URL of the server to connect to
	 * @param Index The index within the server to reference
	 * @param Type The type i the index to save to
	 */
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
	
	/**
	 * Gets the list of questions stored on the server based on the 
	 * url, index, and type passed to the constructor.
	 * It returns a list sorted by most upvoted questions, which is the
	 * default sorting order, as well, it defaults to a list of 10 elements.
	 * @return An array list containing up to 10 questions sorted by upvotes
	 */
	public ArrayList<QuestionThread> getThreads() {
		return getThreads(new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort(),10);
	}

	/**
	 * Gets the questions stored on the server with optional parameters
	 * to specify the sorting and number of threads to return.
	 * @param sort The type of sorting to be used
	 * @param numThreads The number of questions to be retrieved
	 * @return An array list containing up to the requested number of questions in the requested sorting style
	 */
	public ArrayList<QuestionThread> getThreads(Sort sort, int numThreads) {
		return getThreads(queryAll,sort,numThreads);
	}
	
	/**
	 * Gets the questions stored on the server with optional parameters
	 * to specify the sorting and number of threads to return, as well as a query to match
	 * @param query A string containing a specific query
	 * @param sort The type of sorting to be used
	 * @param numThreads The number of questions to be retrieved
	 * @return An array list containing up to the requested number of questions in the requested sorting style
	 * 		   matching the given query
	 */
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
				threads.add(qt);
			}
			return threads;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return threads;
	}
	
	/**
	 * Makes a query to be used to request a single question by its id
	 * @param id The id of the question on the server
	 * @return A String containing a query for a question by id
	 */
	private String makeQueryString(String id) {
		return "{\"query\": {\"query_string\": {\"query\": \""+id+"\"}}}";
	}

	/**
	 * Gets a single question from the server based on its id. Used to
	 * obtain the latest versions of locally stored questions.
	 * @param id The id of the question
	 * @return A question with the specified id, or null if one does not exist.
	 */
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

	/**
	 * Saves a question to the server.
	 * @param thread The question to be saved
	 * @return True if the save was successful, false otherwise
	 */
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
	
	/**
	 * Saves a question to the server using a specific id
	 * @param thread The question to be saved
	 * @param id The id to be used for the question
	 * @return True if the save was successful, false otherwise
	 */
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
	
	/**
	 * Goes through the questions stored in a ThreadList and saves them each
	 * to the server.
	 * @param tl The ThreadList object to save the questions from
	 * @return True if the save was successful, false otherwise
	 */
	public boolean saveThreads(ThreadList tl){
		for(QuestionThread qt: tl.getThreads()){
			if(!saveThread(qt)) {
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
		String query="{\"query\":{\"filtered\": {\"query\":{\"query_string\":{\"fields\": [\"answers.text\", \"question.text\"],\"query\": \"*"+searchString+"*\"}},\"filter\": {}}}}";
		return getThreads(query, new SimpleSortFactory(SimpleSortFactory.MostUpvotes).createSort(), 10);
	}
	
	/**
	 * Shuts down the connection to elasticsearch
	 */
	public void cleanup() {
		this.client.shutdownClient();
	}
}
