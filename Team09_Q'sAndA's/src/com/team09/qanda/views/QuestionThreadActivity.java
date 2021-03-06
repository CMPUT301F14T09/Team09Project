package com.team09.qanda.views;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.Constants;
import com.team09.qanda.GPSHandler;
import com.team09.qanda.ImageHandler;
import com.team09.qanda.LocDialogFragment;
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

public class QuestionThreadActivity extends FragmentActivity implements LocDialogFragment.LocDialogListener{

	private QuestionThread thread;
	private ThreadAdapter adapter;
	private ExpandableListView threadPostsList;
	private EditText answerTextField;
	private ApplicationState curState = ApplicationState.getInstance();
	private static int IMAGE_REQUEST = 1;
	private String imageString = null;
	private boolean fromMain;
	private boolean fromFavourite;
	private boolean fromLater;
	private String answerFilename;
	private Context context=this;
	private PostController questionPostController;
	private LocalStorageHandler lsh=new LocalStorageHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_thread);
		getActionBar().setDisplayHomeAsUpEnabled(true);
			
		thread = (QuestionThread) getIntent().getExtras().getSerializable("Selected Thread");
		fromMain=getIntent().getExtras().getBoolean("main");
		fromFavourite=getIntent().getExtras().getBoolean("favourite");
		fromLater=getIntent().getExtras().getBoolean("later");
		threadPostsList = (ExpandableListView) findViewById(R.id.ThreadPostsView);
		answerFilename="answer"+thread.getId()+".txt";
		answerTextField = (EditText) findViewById(R.id.editAnswerText);
		String persistentText=lsh.getText(context, answerFilename);
		if (!persistentText.isEmpty()) {
			answerTextField.setText(persistentText);
		}
		
		answerTextField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				lsh.saveText(context, answerTextField.getText().toString(), answerFilename);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.help) {
			onScreenHelp();
			return true;
		}
		
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
		if (id == R.id.read_laters) {			
			userThreadsActivity(Constants.READ_LATER_FILENAME);
			return true;
		}
		if (id == R.id.user_questions) {
			userThreadsActivity(Constants.MY_QUESTIONS_FILENAME);
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void instantiate() {
		adapter = new ThreadAdapter(this, thread);
		threadPostsList.setAdapter(adapter);
	}
	
	/**
    *
    * This method opens up the on-Screen Help dialog when called
    * The on-Screen Help dialog contains helpful information regarding the
    * current screen/activity the user is in. The information is stored in the
    * res/layouts directory. This activity's help information is stored in
    * res/layouts/questionactivity_help.xml
    * 
    * @param 
    * @see 
    */
	public void onScreenHelp() {
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.questionactivity_help, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("On-Screen Help");
		builder.setView(view);
		
		// create an alert dialog
	    AlertDialog alertD = builder.create();
	    alertD.setCanceledOnTouchOutside(true);
	    alertD.show();
	}
	
	/**
    *
    * This method allows navigation to lists of locally saved questions (favourites,
    * my questions and read laters) upon selection from the menu.
    * It starts the UserThreadsActivity to display these lists.
    * 
    * @param FILENAME The file which saves the questions locally
    */
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

	/**
	 * 
	 * attachImage
	 * Calls an Intent to allow user to attach an image.
	 * Called when user presses attach button.
	 * 
	 */
	public void attachImage(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, IMAGE_REQUEST);  
	}
	
	
	/**
	 *  onActivityResult
	 *  Called when an image has been chosen by the user, starts ImageHandler
	 *  
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			if (requestCode == IMAGE_REQUEST) {
				ImageHandler imageHandler = new ImageHandler();
				Bitmap image = imageHandler.handleImage(data, this.context);
				imageString = imageHandler.imageToString(image);
          		Toast.makeText(getApplicationContext(), "Image attached.", Toast.LENGTH_SHORT).show();
			}
		} 
	}
	
	// Method called by the onClick of answerSubmissionButton
	public void submitAnswer(View v) {
		showLocDialog();
	}
	
	public void submitAnswer(Post answer) {
		if (isConnected()) {
			System.out.println("Answer Count : " + thread.getAnswers().size());
			PostController pc = new PostController(answer);
			if (imageString!= null) {
				pc.attachImage(imageString);
			}
			// PostController for QuestionThread
			QuestionThreadController qtc = new QuestionThreadController(thread);
			qtc.addAnswer(answer);
			System.out.println("New Answer Count : " + thread.getAnswers().size());
			AsyncSave task=new AsyncSave();
			task.execute(new QuestionThreadController[] {qtc});
			answerTextField.setText("");
			lsh.deleteFile(context, answerFilename);
			// set image null to avoid lingering image
			imageString = null;
		}
		else {
			Toast.makeText(context, "Could not post answer. Check network connection and try again", Toast.LENGTH_SHORT).show();
		}
	}
	
    public void showLocDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new LocDialogFragment();
        dialog.show(getSupportFragmentManager(), "LocDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	String answerText = answerTextField.getText().toString();
		// Create a Post object for the answer
		Post answer = new Post(curState.getUser(), answerText);
		answer.setCity(new GPSHandler(context).getCity());
		submitAnswer(answer);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	String answerText = answerTextField.getText().toString();
		// Create a Post object for the answer
		Post answer = new Post(curState.getUser(), answerText);
		submitAnswer(answer);
    }
	
	private boolean isConnected() {
		//Determine if the user has connectivity
		//http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
		//22 November, 2014
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
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
