package com.team09.qanda;

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
import android.widget.SearchView;

public class MainActivity extends Activity{ //Main question view
	
	private ArrayAdapter<CharSequence> spinner;
	private ActionBar.OnNavigationListener listener;
	
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
	//for testing purposes
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
