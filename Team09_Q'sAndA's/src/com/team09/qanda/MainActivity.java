package com.team09.qanda;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
	private ThreadList threads;
	private ThreadListController tlc;
	private ThreadListAdapter adapter;
	private ListView mainThreadsList;
	private Context context=this;
	private UserState curUser = UserState.getInstance();
	static final int ADD_QUESTION_REQUEST = 1;
	static final String FILENAME = ""; //TODO: filename of where username is stored
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpActionBarSpinner();
		
		getActionBar().setDisplayShowTitleEnabled(false);
		
		listener=new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				return false;
			}
		};
		
		mainThreadsList = (ListView) findViewById(R.id.MainListView);
		
		ViewGroup footer = (ViewGroup) getLayoutInflater().inflate(R.layout.load_more_footer, mainThreadsList,
                false);
		mainThreadsList.addFooterView(footer);
		
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
		tlc=new ThreadListController(threads);
		AsyncGet task=new AsyncGet();
		task.execute(new ThreadListController[] {tlc});
		//Post p=threads.get(0).getQuestion();
		//PostController pc=new PostController(p);
		//pc.addUp();
		//tlc.refreshThreads();
		//Log.i("threads loaded", threads.jsonify());
		//populateList();
		
		//ArrayList<QuestionThread> testthreads = new ArrayList<QuestionThread>();
		//testthreads.add(new QuestionThread(new Post(new User(), "Question 2?")));
		//testAdapter = new ArrayAdapter<QuestionThread>(this,R.layout.list_item, testthreads);
		adapter = new ThreadListAdapter(this, R.layout.main_row_layout, threads.getThreads());
		mainThreadsList.setAdapter(adapter);
		
		
		mainThreadsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
				displayThread(selectedThread);
												
			}
		});
		instantiate();
	}
	protected void instantiate() {
		//super.onStart();
		//threads.refresh(0, 10);
		//threads = new ThreadList();
		System.out.println("New Threads List Initialize Size : " + threads.getThreads().size());
		//populateList();
		
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(this.openFileInput(FILENAME)));
			String line;

			//TODO:need to determine how username will be stored in file
			while ((line = input.readLine()) != null) {
				User user = new User();
				user.setName(line);
				curUser.setUser(user);
			}

		} catch (FileNotFoundException e) {
			setUsername();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//ArrayList<QuestionThread> testthreads = new ArrayList<QuestionThread>();
		//testthreads.add(new QuestionThread(new Post(new User(), "Question 2?")));
		//testAdapter = new ArrayAdapter<QuestionThread>(this,R.layout.list_item, testthreads);
		//adapter = new ThreadListAdapter(this, R.layout.main_row_layout, threads.getThreads());
		//mainThreadsList.setAdapter(adapter);
		
		
		mainThreadsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
				displayThread(selectedThread);
												
			}
		});
	}
	
	/**
    *
    * This method creates a prompt for setting username
    */	
	public void setUsername(){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final User user = new User();
	    View promptView = layoutInflater.inflate(R.layout.name_prompt, null);
	
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	
	    // set name_prompt.xml to be the layout file of the alertdialog builder
	    alertDialogBuilder.setView(promptView);
	    final EditText input = (EditText) promptView.findViewById(R.id.usernameInput); 
	    final Context c = this;
	    
	    // setup a dialog window
	    alertDialogBuilder
	        .setCancelable(false)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // get user input and set it to result
	            	user.setName(input.getText().toString());
	            	curUser.setUser(user);
	            	Toast.makeText(c, "Your Username is: " + user.getName(), Toast.LENGTH_SHORT).show();
	            }
	        })
	        .setNegativeButton("Skip(Use default)", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	curUser.setUser(user);
	                dialog.cancel();
	            }
	        });
	
	    // create an alert dialog
	    AlertDialog alertD = alertDialogBuilder.create();
	
	    alertD.show();
	}

	/**
    *
    * This method starts the question thread viewing activity.
    * It uses the intent to pass the selected thread.
    *
    * @param 
    * @see 
    */
	
	public void displayThread(QuestionThread thread) {
		Intent intent = new Intent(MainActivity.this, QuestionThreadActivity.class);
		intent.putExtra("Selected Thread", thread);
		startActivity(intent);
	}
	
	
	/**
    *
    * This method loads more threads using the elastic search handler
    * through a thread list controller and notifies the adapter
    *
    * @param 
    * @see 
    */
	
	public void loadMore(View v) {
		AsyncGetMore task=new AsyncGetMore();
		task.execute(new ThreadListController[] {tlc});
	}
	
	/* Add question button method */
	public void addQuestion(View v) {
		Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, AddQuestionActivity.class);
	    startActivityForResult(intent, ADD_QUESTION_REQUEST);
	}
	
	/* TODO : onActivityResult */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == MainActivity.ADD_QUESTION_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // The Author asked a question
	        	String textFieldEntry = data.getExtras().getString(AddQuestionActivity.ADD_QUESTION_RESULT);
	        	Toast.makeText(getApplicationContext(), textFieldEntry, Toast.LENGTH_SHORT).show();
	            // Do something with the contact here (bigger example below)
	        }
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
			adapter = new ThreadListAdapter(context, R.layout.main_row_layout, threads.getThreads());
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
			adapter = new ThreadListAdapter(context, R.layout.main_row_layout, threads.getThreads());
			mainThreadsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	
	//for testing purposes
	public ThreadListAdapter getAdapter(){
		return adapter;
	}

	public void setAdapter(ThreadListAdapter adapter){
		this.adapter = adapter;
	}
	
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
				tlc.sortThreads(spinner.getItem(itemPosition));
				return true;
			}
		};
		bar.setListNavigationCallbacks(spinner,listener);
	}
	private List<String> getSortOptionsList(){
		return Arrays.asList(getString(R.string.sort_HasPicture),
										getString(R.string.sort_MostRecent),
										getString(R.string.sort_Oldest),
										getString(R.string.sort_MostUpvotes),
										getString(R.string.sort_LeastUpvoted));
	}
	
}
