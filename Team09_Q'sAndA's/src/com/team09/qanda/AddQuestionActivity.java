package com.team09.qanda;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddQuestionActivity extends Activity {

	private String textFieldEntry;
	private EditText textField;
	private ApplicationState curState = ApplicationState.getInstance();

	static final String ADD_QUESTION_RESULT = "RESULT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_add_question);
		
		textField = (EditText) findViewById(R.id.add_question_field);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_question, menu);
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
		}// else if (id == android.R.id.home) {
		//	NavUtils.navigateUpFromSameTask(this);
	     //   return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	
	// Creates a new question with the current user as the author
	public void submitQuestion(View v) {
		textFieldEntry = textField.getText().toString();
    	Post newPost = new Post(curState.getUser(), textFieldEntry);
    	QuestionThread newQuestion = new QuestionThread(newPost);
    	QuestionThreadController qtc = new QuestionThreadController(newQuestion);
    	AsyncSave task=new AsyncSave();
		task.execute(new QuestionThreadController[] {qtc});
		//Intent result = new Intent();
		//result.putExtra(ADD_QUESTION_RESULT, textFieldEntry);
		//setResult(RESULT_OK, result);
		//finish();
	}
	
	private class AsyncSave extends AsyncTask<QuestionThreadController, Void, Void> {

		@Override
		protected Void doInBackground(QuestionThreadController... params) {
			for (QuestionThreadController qtc:params) {
		    	qtc.saveThread();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			finish();
		}
	}
	
	
}
