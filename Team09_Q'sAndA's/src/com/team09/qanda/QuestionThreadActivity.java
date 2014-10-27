package com.team09.qanda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class QuestionThreadActivity extends Activity {
	
	private QuestionThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_thread);
		
		thread = (QuestionThread) getIntent().getExtras().getSerializable("Selected Thread");
		
		TextView question = (TextView) findViewById(R.id.threadQuestion);
		question.setText(thread.getQuestion().getText());
		
		TextView author = (TextView) findViewById(R.id.threadQuestionAuthor);
		author.setText(thread.getQuestion().getAuthor().getName());
		
		TextView upvotes = (TextView) findViewById(R.id.questionUpvotes);
		upvotes.setText(thread.getQuestion().getUps() + " Point(s)");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_thread, menu);
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
}
