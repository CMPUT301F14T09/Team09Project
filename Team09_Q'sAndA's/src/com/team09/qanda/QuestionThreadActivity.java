package com.team09.qanda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class QuestionThreadActivity extends Activity {

	private QuestionThread thread;
	private ThreadAdapter adapter;
	private ListView threadPostsList;
	private EditText answerTextField;
	private User author;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_thread);

		thread = (QuestionThread) getIntent().getExtras().getSerializable("Selected Thread");

		threadPostsList = (ListView) findViewById(R.id.ThreadPostsView);

		answerTextField = (EditText) findViewById(R.id.editAnswerText);

		// This is a test author
		author = new User();
		
		// Instantiate thread
		instantiate();
		/*
		TextView question = (TextView) findViewById(R.id.threadQuestion);
		question.setText(thread.getQuestion().getText());

		TextView author = (TextView) findViewById(R.id.threadQuestionAuthor);
		author.setText(thread.getQuestion().getAuthor().getName());

		TextView upvotes = (TextView) findViewById(R.id.questionUpvotes);
		upvotes.setText(thread.getQuestion().getUps() + " Point(s)");

		TextView 
		 */
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
		//ArrayList<QuestionThread> testthreads = new ArrayList<QuestionThread>();
		//testthreads.add(new QuestionThread(new Post(new User(), "Question 2?")));
		//testAdapter = new ArrayAdapter<QuestionThread>(this,R.layout.list_item, testthreads);
		adapter = new ThreadAdapter(this, R.layout.thread_row_layout, thread);
		threadPostsList.setAdapter(adapter);

		/*
			mainThreadsList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
					displayThread(selectedThread);

				}
			});
		 */
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

		//ArrayList<QuestionThread> testthreads = new ArrayList<QuestionThread>();
		//testthreads.add(new QuestionThread(new Post(new User(), "Question 2?")));
		//testAdapter = new ArrayAdapter<QuestionThread>(this,R.layout.list_item, testthreads);
		adapter = new ThreadAdapter(this, R.layout.thread_row_layout, thread);
		threadPostsList.setAdapter(adapter);

		
		//mainThreadsList.setOnItemClickListener(new OnItemClickListener() {
		//	public void onItemClick(AdapterView<?> parent, View view,
		//			int position, long id) {

		//		QuestionThread selectedThread = (QuestionThread) parent.getItemAtPosition(position);
		//		displayThread(selectedThread);

		//	}
		//});
		 
	}
	*/

	public void submitAnswer(View v) {
		System.out.println("Answer Count : " + thread.getAnswers().size());
		String answerText = answerTextField.getText().toString();
		// Create a Post object for the answer
		Post answer = new Post(author, answerText);
		// PostController for QuestionThread
		QuestionThreadController qctl = new QuestionThreadController(thread);
		// Add answer in the questionThread
		qctl.addAnswer(answer);
		System.out.println("New Answer Count : " + thread.getAnswers().size());
		answerTextField.setText("");

	}
}
