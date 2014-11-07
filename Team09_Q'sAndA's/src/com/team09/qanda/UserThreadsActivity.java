package com.team09.qanda;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
	private LocalStorageHandler localStorageHandler = new LocalStorageHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_threads);
		userThreadList = (ListView) findViewById(R.id.UserListView);
		FILENAME = (String) getIntent().getExtras().getSerializable("FILENAME");
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	//	threads = localStorageHandler.getThreadList(getBaseContext(), "My Questions.txt");
		String text = localStorageHandler.getText(getBaseContext(), "My Questions.txt");
	//	tlc = new ThreadListController(threads); 
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
