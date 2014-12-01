package com.team09.qanda.views;

/*
 * This actvity is necessary for searching 
 */
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.models.ThreadList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * SearchActivity is used to search for questions and answers.It is the
 * defacto searchable activity of the Appplication
 *
 */

public class SearchActivity extends Activity {
	private ThreadListController tlc;
	private ThreadListAdapter adapter;
	private ThreadList tl;
	private LocalStorageHandler lsh=new LocalStorageHandler();
	private String query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		tl=new ThreadList();
		tlc= new ThreadListController(tl);
		adapter=new ThreadListAdapter(this, R.layout.main_row_layout, tl.getThreads(),false);
		ListView lv=((ListView)findViewById(R.id.listView_search));
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new ThreadClickListener(lsh, this, tl));
		getSearchQuery(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
	/**
	 * Grab the search query from the search intent
	 * (if it is a search intent,i.e an intent with Intent.ACTION_SEARCH action). If the intent is not a
	 * Search intent, the activity will display a toast and then close
	 * @param intent, the search intent
	 */
	public void getSearchQuery(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			query = intent.getStringExtra(SearchManager.QUERY);
			getSearchResults getter=new getSearchResults();
			getter.execute(new ThreadListController[]{tlc});
		}
		else{
			Toast.makeText(this, "No Search Query... closing the activity",Toast.LENGTH_LONG).show();
			finish();
		}
	}
	private class getSearchResults extends AsyncTask<ThreadListController, Void, Void> {

		@Override
		protected Void doInBackground(ThreadListController... params) {
			for (ThreadListController tlc:params) {
				tlc.search(query);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			adapter.clear();
			adapter.addAll(tl.getThreads());
			adapter.notifyDataSetChanged();
			if(adapter.getCount()==0){
				Toast.makeText(SearchActivity.this,"No results", Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
