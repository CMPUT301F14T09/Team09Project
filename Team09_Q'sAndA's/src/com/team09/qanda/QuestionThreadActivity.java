package com.team09.qanda;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * This activity displays a questionThread. This includes displaying the question and any answers
 * and allows the user to upvote questions and answers or post an answer or a reply.
 *
 */

public class QuestionThreadActivity extends Activity {

	private QuestionThread thread;
	private ThreadAdapter adapter;
	private ListView threadPostsList;
	private EditText answerTextField;
	private ApplicationState curState = ApplicationState.getInstance();
	private PostController questionPostController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_thread);

		thread = (QuestionThread) getIntent().getExtras().getSerializable("Selected Thread");
		
		threadPostsList = (ListView) findViewById(R.id.ThreadPostsView);

		answerTextField = (EditText) findViewById(R.id.editAnswerText);
		
		
		// Instantiate thread
		instantiate();
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

	public void instantiate() {
		adapter = new ThreadAdapter(this, R.layout.thread_row_layout, thread);
		threadPostsList.setAdapter(adapter);

	}
	
	/** This was moved and changed to instantiate(). I'm saving it for backup just in case anyone needs it.
	 * I made the change because the listview was not updating when answers are added in the list.
	 * I suspected that it was due to having the things in the onStart(), but feel free to make changes
	 * I'm open to suggestions and help! (This occured in the MainActivity as well)
	 * -Edrick
	 */
	/*
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		 
	}
	*/

	// Method called by the onClick of answerSubmissionButton
	public void submitAnswer(View v) {
		System.out.println("Answer Count : " + thread.getAnswers().size());
		String answerText = answerTextField.getText().toString();
		// Create a Post object for the answer
		Post answer = new Post(curState.getUser(), answerText);
		// PostController for QuestionThread
		QuestionThreadController qtc = new QuestionThreadController(thread);
		qtc.addAnswer(answer);
		System.out.println("New Answer Count : " + thread.getAnswers().size());
		AsyncSave task=new AsyncSave();
		task.execute(new QuestionThreadController[] {qtc});
		answerTextField.setText("");
	}
	
	// Called when user taps the upvote button. Gives the question post an upvote and updates the thread.
	public void upvoteQuestion(View v) {
		Post post = thread.getQuestion();
		questionPostController = new PostController(post);
		if (!questionPostController.alreadyUpvoted()) {
			questionPostController.addUp();
		    QuestionThreadController qtc = new QuestionThreadController(thread);
			AsyncSave task=new AsyncSave();
			task.execute(new QuestionThreadController[] {qtc});
			Toast.makeText(this, "upvote added by " , Toast.LENGTH_SHORT).show();
		}
		v.setEnabled(false);
	}
	
	private class AsyncSave extends AsyncTask<QuestionThreadController, Void, Void> {

		@Override
		protected Void doInBackground(QuestionThreadController... params) {
			for (QuestionThreadController qtc:params) {
		    	qtc.saveThread(thread.getId());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged(); 
		}
	}
	
	public ThreadAdapter getAdapter() {
		return adapter;
	}
}
