package com.team09.qanda.views;

/*
 * This actvity is necessary for searching 
 */
import com.team09.qanda.R;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.models.ThreadList;

import android.app.Activity;
import android.app.SearchManager;
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
 * SearchActivity is used to search for questions and answers.
 *
 */

public class SearchActivity extends Activity {
	private ThreadListController tlc;
	private ThreadListAdapter adapter;
	private ThreadList tl;
	private String query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		tl=new ThreadList();
		tlc= new ThreadListController(tl);
		adapter=new ThreadListAdapter(this, R.layout.main_row_layout, tl.getThreads(),false);
		((ListView)findViewById(R.id.listView_search)).setAdapter(adapter);
		((ListView)findViewById(R.id.listView_search)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchActivity.this, QuestionThreadActivity.class);
				intent.putExtra("Selected Thread", adapter.getItemId(position));
				startActivity(intent);
				
			}
		});
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
	private void getSearchQuery(Intent intent){
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
			adapter.notifyDataSetChanged();
		}
		
	}
}
