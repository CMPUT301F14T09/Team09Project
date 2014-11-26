package com.team09.qanda.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.Constants;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

/**
 * 
 * Activity for any locally stored questions, including favorites, user's questions, and read later. 
 *
 */

public class UserThreadsActivity extends Activity {

	private ThreadList threads;
	private ThreadListController tlc;
	private ThreadListAdapter adapter;
	private ListView userThreadList;
	private Context context=this;
	private ApplicationState curState = ApplicationState.getInstance();
	private String FILENAME;
	private LocalStorageHandler lsh = new LocalStorageHandler();
	private boolean fromFavourite=false;
	private boolean fromLater=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_threads);
		userThreadList = (ListView) findViewById(R.id.UserListView);
		FILENAME = (String) getIntent().getExtras().getSerializable("FILENAME");
		if (FILENAME.equals(Constants.FAVOURITES_FILENAME)) {
			fromFavourite=true;
		}
		else if (FILENAME.equals(Constants.READ_LATER_FILENAME)) {
			fromLater=true;
		}
		threads=lsh.getThreadList(context, FILENAME);
		tlc = new ThreadListController(threads);
		
		
		userThreadList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
				if (FILENAME.equals(Constants.READ_LATER_FILENAME)) {
					tlc.removeThread(selectedThread);
					lsh.deleteQuestionThread(context, selectedThread, Constants.READ_LATER_FILENAME, Constants.LATER_IDS_FILENAME);
					adapter = new ThreadListAdapter(context, R.layout.user_row_layout, threads.getThreads(),false);
					userThreadList.setAdapter(adapter);
				}
				displayThread(selectedThread);
												
			}
		});
		
		adapter = new ThreadListAdapter(context, R.layout.user_row_layout, threads.getThreads(),false);
		userThreadList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_threads, menu);
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
		if (id == R.id.main) {
			startActivity(new Intent(UserThreadsActivity.this, MainActivity.class));
			return true;
		}
		if (id == R.id.favourites) {
			userThreadsActivity(Constants.FAVOURITES_FILENAME);
			return true;
		}
		if (id == R.id.read_laters) {
			userThreadsActivity(Constants.READ_LATER_FILENAME);
			return true;
		}
		if (id == R.id.user_questions) {
			userThreadsActivity(Constants.MY_QUESTIONS_FILENAME);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
 
	}
	
	public void userThreadsActivity(String FILENAME) {
		Intent intent = new Intent(UserThreadsActivity.this, UserThreadsActivity.class);
		intent.putExtra("FILENAME", FILENAME);
		startActivity(intent);
	}
	
	public void displayThread(QuestionThread thread) {
		Intent intent = new Intent(UserThreadsActivity.this, QuestionThreadActivity.class);
		intent.putExtra("Selected Thread", thread);
		intent.putExtra("main", false);
		intent.putExtra("favourite", fromFavourite);
		intent.putExtra("later", fromLater);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(UserThreadsActivity.this, MainActivity.class));
	    finish();
	}
}
