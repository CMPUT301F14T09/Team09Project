package com.team09.qanda;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity{ //Main question view
	
	private ArrayAdapter<CharSequence> spinner;
	private ActionBar.OnNavigationListener listener;
	private ThreadList threads;
	private ThreadListAdapter adapter;
	private ListView mainThreadsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		
		listener=new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				return false;
			}
		};
		
		mainThreadsList = (ListView) findViewById(R.id.MainListView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		getSearchView(menu).setSearchableInfo(getSearchManager().getSearchableInfo(getComponentName()));
	    getSearchView(menu).setIconifiedByDefault(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//threads.refresh(0, 10);
		threads = new ThreadList();
		populateList();
		
		//ArrayList<QuestionThread> testthreads = new ArrayList<QuestionThread>();
		//testthreads.add(new QuestionThread(new Post(new User(), "Question 2?")));
		//testAdapter = new ArrayAdapter<QuestionThread>(this,R.layout.list_item, testthreads);
		adapter = new ThreadListAdapter(this, R.layout.main_row_layout, threads.getThreads());
		mainThreadsList.setAdapter(adapter);
	}

	
	
	/** TEMPORARY - to create a list of threads **/
	private void populateList() {
		Post qpost1 = new Post(new User("John"), "A very very very very very very very very very very very long Question 1?");
		PostController pc1 = new PostController(qpost1);
		
		QuestionThread q1 = new QuestionThread(qpost1);
		QuestionThreadController qtc1 = new QuestionThreadController(q1);
		
		pc1.addUp();
		qtc1.addAnswer(new Post(new User(), "Answer 1."));
		
		Post qpost2 = new Post(new User(), "Question 2?");
		PostController pc2 = new PostController(qpost2);
		
		QuestionThread q2 = new QuestionThread(qpost2);
		QuestionThreadController qtc2 = new QuestionThreadController(q2);
		
		pc2.addUp();
		pc2.addUp();
		qtc2.addAnswer(new Post(new User(), "Answer 2."));
		qtc2.addAnswer(new Post(new User(), "Answer 3."));
		
		ThreadListController cn1=new ThreadListController(threads);
		cn1.addThread(q1);
		cn1.addThread(q2);
		
		
	}
	
	//for testing purposes
	public ThreadListAdapter getAdapter(){
		return adapter;
	}

	public void setAdapter(ThreadListAdapter adapter){
		this.adapter = adapter;
	}
	
	public ArrayAdapter<CharSequence> getSpinnerAdapter(){
		return spinner;
	}
	public ActionBar.OnNavigationListener getNavigationListener(){
		return listener;
	}
	
	/** TESTING PURPOSES - to open and test AddQuestionActivity */
	public void testButton(MenuItem item) {
	    Intent intent = new Intent(this, AddQuestionActivity.class);
	    startActivity(intent);
	}
	
	/** TESTING PURPOSES - to open and test QuestionThreadActivity */
	public void testQThreadButton(MenuItem item) {
	    Intent intent = new Intent(this, QuestionThreadActivity.class);
	    startActivity(intent);
	}
	private SearchManager getSearchManager(){
		return (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	}
	private SearchView getSearchView(Menu menu){
		return (SearchView) menu.findItem(R.id.action_search).getActionView();
	}
	
}
