package com.team09.qanda.views;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.Constants;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.ThreadAdapter;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

/**
 * 
 * This activity displays a questionThread. This includes displaying the question and any answers
 * and allows the user to upvote questions and answers or post an answer or a reply.
 *
 */

public class QuestionThreadActivity extends Activity {

	private QuestionThread thread;
	private ThreadAdapter adapter;
	private ExpandableListView threadPostsList;
	private EditText answerTextField;
	private ApplicationState curState = ApplicationState.getInstance();
	private PostController questionPostController;
	private static int IMAGE_REQUEST = 1;
	private byte[] image = null;
	private boolean fromMain;
	private boolean fromFavourite;
	private boolean fromLater;
	private Context context=this;
	private LocalStorageHandler lsh=new LocalStorageHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_thread);

		thread = (QuestionThread) getIntent().getExtras().getSerializable("Selected Thread");
		fromMain=getIntent().getExtras().getBoolean("main");
		fromFavourite=getIntent().getExtras().getBoolean("favourite");
		fromLater=getIntent().getExtras().getBoolean("later");
		threadPostsList = (ExpandableListView) findViewById(R.id.ThreadPostsView);

		answerTextField = (EditText) findViewById(R.id.editAnswerText);

		// Instantiate thread
		instantiate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (fromMain) {
			getMenuInflater().inflate(R.menu.question_thread, menu);
		}
		else {
			if (fromFavourite) {
				getMenuInflater().inflate(R.menu.favourites, menu);
			}
			else if (fromLater) {
				getMenuInflater().inflate(R.menu.read_later, menu);
			}
			else {
				getMenuInflater().inflate(R.menu.my_questions, menu);
			}
		}
		//getMenuInflater().inflate(R.menu.question_thread, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.favourite_question) {
			ArrayList<String> favourite_ids=lsh.getIds(context, Constants.FAVOURITE_IDS_FILENAME);
			if (favourite_ids.contains(thread.getId())) {
				Toast.makeText(context, "Question already in favourites", Toast.LENGTH_SHORT).show();
			}
			else {
				lsh.saveQuestionThread(context, thread, Constants.FAVOURITES_FILENAME, Constants.FAVOURITE_IDS_FILENAME);
				Toast.makeText(context, "Question added to favourites", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		if (id == R.id.remove_favourite) {
			lsh.deleteQuestionThread(context, thread, Constants.FAVOURITES_FILENAME, Constants.FAVOURITE_IDS_FILENAME);
			Toast.makeText(context, "Question removed from favourites", Toast.LENGTH_SHORT).show();
			userThreadsActivity(Constants.FAVOURITES_FILENAME);
			return true;
		}
		if (id == R.id.read_later) {
			ArrayList<String> later_ids=lsh.getIds(context, Constants.LATER_IDS_FILENAME);
			if (later_ids.contains(thread.getId())) {
				Toast.makeText(context, "Question already marked as read later", Toast.LENGTH_SHORT).show();
			}
			else {
				lsh.saveQuestionThread(context, thread, Constants.READ_LATER_FILENAME, Constants.LATER_IDS_FILENAME);
				Toast.makeText(context, "Question marked as read later", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		if (id == R.id.main) {
			startActivity(new Intent(QuestionThreadActivity.this, MainActivity.class));
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void instantiate() {
		adapter = new ThreadAdapter(this, R.layout.thread_row_layout, thread);
		threadPostsList.setAdapter(adapter);

	}
	
	public void userThreadsActivity(String FILENAME) {
		Intent intent = new Intent(QuestionThreadActivity.this, UserThreadsActivity.class);
		intent.putExtra("FILENAME", FILENAME);
		startActivity(intent);
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

	public void attachImage(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, IMAGE_REQUEST);
		
	}
	
	// Called when an image has been chosen by the user
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			if (requestCode == IMAGE_REQUEST) {
				Uri uri = data.getData();
				InputStream input = null; 
				try {
					input = getContentResolver().openInputStream(uri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Bitmap selectedImage = BitmapFactory.decodeStream(input);
				int imageSize = selectedImage.getByteCount();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				selectedImage.compress(Bitmap.CompressFormat.PNG, 100, out);	
				int imageByteSize = out.toByteArray().length;
				if (imageByteSize <= (64*1024)) {
					image = out.toByteArray();
					Toast.makeText(this, "Image attached.", Toast.LENGTH_SHORT).show();
				}
				// TODO: Implement image compression
				else {
					Toast.makeText(this, "Image too large.", Toast.LENGTH_SHORT).show();
				}
				
			}
		}
	}
	
	// Method called by the onClick of answerSubmissionButton
	public void submitAnswer(View v) {
		System.out.println("Answer Count : " + thread.getAnswers().size());
		String answerText = answerTextField.getText().toString();
		// Create a Post object for the answer
		Post answer = new Post(curState.getUser(), answerText);
		PostController pc = new PostController(answer);
		if (image!= null) {
			pc.attachImage(image);
		}
		// PostController for QuestionThread
		QuestionThreadController qtc = new QuestionThreadController(thread);
		qtc.addAnswer(answer);
		System.out.println("New Answer Count : " + thread.getAnswers().size());
		AsyncSave task=new AsyncSave();
		task.execute(new QuestionThreadController[] {qtc});
		answerTextField.setText("");
		// set image null to avoid lingering image
		image = null;
	}
		
	public void viewImage(View v) {
		Intent intent = new Intent(QuestionThreadActivity.this, PictureViewActivity.class);
		intent.putExtra("Selected Post", thread.getQuestion());
		startActivity(intent);
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
