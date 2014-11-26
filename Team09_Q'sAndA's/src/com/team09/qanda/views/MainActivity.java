package com.team09.qanda.views;

import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.Constants;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.ThreadListAdapter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.esearch.SimpleSortFactory;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;

/**
 * 
 * This class is the activity for the main screen
 * of the app. It displays a list of all the questions
 * and allows the user to navigate to other screens.
 *
 * @author 
 * @version 
 */

public class MainActivity extends Activity{ //Main question view
	
	private ArrayAdapter<String> spinner;
	private ActionBar.OnNavigationListener listener;
	private ThreadList threads=new ThreadList();
	private ThreadListController tlc;
	private ThreadListAdapter adapter;
	private ListView mainThreadsList;
	private LocalStorageHandler lsh=new LocalStorageHandler();
	private Context context=this;
	private ApplicationState curState = ApplicationState.getInstance();
	static final String FILENAME = "user.txt"; //TODO: filename of where username is stored
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUpActionBarSpinner();
		
		getActionBar().setDisplayShowTitleEnabled(false);
		mainThreadsList = (ListView) findViewById(R.id.MainListView);
		
		ViewGroup footer = (ViewGroup) getLayoutInflater().inflate(R.layout.load_more_footer, mainThreadsList,
                false);
		mainThreadsList.addFooterView(footer);
		mainThreadsList.setOnItemClickListener(new ThreadClickListener(lsh, context, threads));
		
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
		
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                	@Override
                	public void run() {
                		AsyncGet getTask=new AsyncGet();
                		getTask.execute(new ThreadListController[] {tlc});
                		if (isConnected()) {
                			AsyncRefreshLocal refreshTask=new AsyncRefreshLocal();
                			refreshTask.execute(new LocalStorageHandler[] {lsh});
                		}
                        swipeView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//set up the search bar
		/*The following Android Developer Guide was used, in order
		 * to learn how to setup a Search Bar widget: http://developer.android.com/guide/topics/search/search-dialog.html
		 * Obtained: Nov 9, 2014
		 * */
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
		if (id == R.id.help) {
			// TODO
			onScreenHelp();
			return true;
		}
		if (id == R.id.favourites) {
			userThreadsActivity(Constants.FAVOURITES_FILENAME);
			return true;
		}
		if (id == R.id.readLater) {
			userThreadsActivity(Constants.READ_LATER_FILENAME);
			return true;
		}
		if (id == R.id.my_questions) {
			userThreadsActivity(Constants.MY_QUESTIONS_FILENAME);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		tlc=new ThreadListController(threads);
		AsyncGet getTask=new AsyncGet();
		getTask.execute(new ThreadListController[] {tlc});
		if (isConnected()) {
			AsyncRefreshLocal refreshTask=new AsyncRefreshLocal();
			refreshTask.execute(new LocalStorageHandler[] {lsh});
		}
		adapter = new ThreadListAdapter(context, R.layout.main_row_layout, threads.getThreads(),true);
		mainThreadsList.setAdapter(adapter);
		
		if(curState.getUser() == null){
			String name;
			if ((name = new LocalStorageHandler().getUsername(context)) == null){
				Log.i("Persistence", "fetching username...");
				setUsername();
				Log.i("Persistence", "saving username...");

			//	new LocalStorageHandler().saveUsername(context, curState.getUser().getName());
			} else {
				User user = new User(context);
				user.setName(name);
				curState.setUser(user);
			}
		}	 
	}
	
	/**
    *
    * This method creates a prompt for setting username
    * Once the username has been set, it can never be changed.
    * The user can choose to skip setting a username which will make
    * the system generate a random username during the app use. The
    * app will once again prompt the user for a username the next time
    * the user opens the app if they skipped setting a username.
    */	
	public void setUsername(){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final User user = new User(context);
	    View promptView = layoutInflater.inflate(R.layout.name_prompt, null);
	
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	
	    // set name_prompt.xml to be the layout file of the alertdialog builder
	    alertDialogBuilder.setView(promptView);
	    final EditText input = (EditText) promptView.findViewById(R.id.usernameInput); 
	    
	    // setup a dialog window
	    alertDialogBuilder
	        .setCancelable(false)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // get user input and set it to result
	            	String name = input.getText().toString();
	            	if (name.trim().isEmpty()){
	            		curState.setUser(user);
	            		new LocalStorageHandler().saveUsername(context, curState.getUser().getName());
	            		Toast.makeText(context, "Invalid entry! using default username...", Toast.LENGTH_SHORT).show();
	            	} else {
		            	user.setName(name);
		            	curState.setUser(user);
		            	new LocalStorageHandler().saveUsername(context, curState.getUser().getName());
		            	Toast.makeText(context, "Your Username is: " + user.getName(), Toast.LENGTH_SHORT).show();
	            	}
	            }
	        })
	        .setNegativeButton("Skip(Use default)", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	curState.setUser(user);
	                dialog.cancel();
	            }
	        });
	
	    // create an alert dialog
	    AlertDialog alertD = alertDialogBuilder.create();
	
	    alertD.show();
	}
	
	/**
    *
    * This method loads more threads using the elastic search handler
    * through a thread list controller and notifies the adapter.
    * It adds up to 10 more questions every time it is clicked 
    * to the current list of questions in the MainActivity
    *
    * @param 
    * @see 
    */
	
	public void loadMore(View v) {
		AsyncGetMore task=new AsyncGetMore();
		task.execute(new ThreadListController[] {tlc});
	}
	
	/**
    *
    * This method starts the add question thread activity.
    * The adding of the questiong thread is done within the 
    * AddQuestion Activity
    * 
    * @param 
    * @see 
    */
	public void addQuestion(View v) {
		Intent intent = new Intent(this, AddQuestionActivity.class);
	    startActivity(intent);
	}
	
	private boolean isConnected() {
		//Determine if the user has connectivity
		//http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
		//22 November, 2014
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}
	
	public void userThreadsActivity(String FILENAME) {
		Intent intent = new Intent(MainActivity.this, UserThreadsActivity.class);
		intent.putExtra("FILENAME", FILENAME);
		startActivity(intent);
	}
	
	/**
    *
    * This method opens up the on-Screen Help dialog when called
    * The on-Screen Help dialog contains helpful information regarding the
    * current screen/activity the user is in. The information is stored in the
    * res/layouts directory. This activity's help information is stored in
    * res/layouts/main_help.xml
    * 
    * @param 
    * @see 
    */
	
	public void onScreenHelp() {
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.main_help, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("On-Screen Help");
		builder.setView(view);
		
		// create an alert dialog
	    AlertDialog alertD = builder.create();
	    alertD.setCanceledOnTouchOutside(true);
	    alertD.show();
	}
	
	private class AsyncRefreshLocal extends AsyncTask<LocalStorageHandler,Void,Void> {
		@Override
		protected Void doInBackground(LocalStorageHandler... params) {
			for (LocalStorageHandler lsh:params) {
				lsh.refreshLocals(context);
			}
			return null;
		}
	}
	
	private class AsyncGet extends AsyncTask<ThreadListController, Void, Void> {

		@Override
		protected Void doInBackground(ThreadListController... params) {
			for (ThreadListController tlc:params) {
				tlc.refreshThreads();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			adapter = new ThreadListAdapter(context, R.layout.main_row_layout, threads.getThreads(),true);
			mainThreadsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private class AsyncGetMore extends AsyncTask<ThreadListController, Void, Void> {

		@Override
		protected Void doInBackground(ThreadListController... params) {
			for (ThreadListController tlc:params) {
				tlc.getMoreThreads();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter = new ThreadListAdapter(context, R.layout.main_row_layout, threads.getThreads(),true);
			mainThreadsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
	}
	/**For testing purposes only!
	 * Gets the spinner of the Main Activity's Action Bar
	 * @return the Spinner Adapter
	 */
	public ArrayAdapter<String> getSpinnerAdapter(){
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
	
	private void setUpActionBarSpinner() {
		ActionBar bar=getActionBar();
		bar.setDisplayShowTitleEnabled(false);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		spinner=new ArrayAdapter<String>(this,R.layout.spinner_item, getSortOptionsList());
		listener=new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				tlc.changeSortType(new SimpleSortFactory(spinner.getItem(itemPosition)));
				AsyncGet task=new AsyncGet();
				task.execute(new ThreadListController[] {tlc});
				return true;
			}
		};
		bar.setListNavigationCallbacks(spinner,listener);
	}
	/**For testing purposes only!
	 * Gets the ThreadList of the Main Activity
	 * @return threads, the ThreadList of the Main Activity
	 */
	public ThreadList getThreadList(){
		return this.threads;
	}
	private List<String> getSortOptionsList(){
		return Arrays.asList(new String[]{SimpleSortFactory.MostUpvotes,
										SimpleSortFactory.LeastUpvotes,
										SimpleSortFactory.MostRecent,
										SimpleSortFactory.Oldest,
										SimpleSortFactory.HasPictures});
	}

	/**
	 * For testing puroposes only!
	 * @return tlc, the ThreadListController of the Main Activity
	 */
	public ThreadListController getThreadListController() {
		return tlc;
	}

	public ArrayAdapter<QuestionThread> getAdapter() {
		return adapter;
	}
	
}
