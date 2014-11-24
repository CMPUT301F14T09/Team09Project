package com.team09.qanda.views;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.Constants;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

/**
 * 
 * This is the AddQuestionActivity class. 
 * This class handles the tasks related to adding a
 * new question in the threadList. This class uses the curState
 * singleton to provide the user object to as an author for the
 * new questions made. This class also handles the adding of the images
 * in the questions.
 * 
 */

public class AddQuestionActivity extends Activity {

	private String textFieldEntry;
	private EditText textField;
	private ApplicationState curState = ApplicationState.getInstance();
	private LocalStorageHandler lsh = new LocalStorageHandler();
	private Context context = this;
	private byte[] image = null;
	private String imageString = null;

	static final String ADD_QUESTION_RESULT = "RESULT";
	private static int IMAGE_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_add_question);		
		textField = (EditText) findViewById(R.id.add_question_field);
		String persistentText=lsh.getText(context, Constants.QUESTION_TEXT_FILE);
		if (!persistentText.isEmpty()) {
			textField.setText(persistentText);
		}
		textField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				lsh.saveText(context, textField.getText().toString(), Constants.QUESTION_TEXT_FILE);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		textField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitQuestion(v);
                    return true;
                }
                return false;
            }
        });
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
		if (id == R.id.attach_image) {
			attachImage();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void attachImage() {
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
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				selectedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
				int imageByteSize = out.toByteArray().length;
				if (imageByteSize <= (64*1024)) {
					image = out.toByteArray();
					imageString = Base64.encodeToString(image, Base64.DEFAULT);
					ImageView imageView = (ImageView)findViewById(R.id.attachedImage); 
					imageView.setImageBitmap(selectedImage);
					Toast.makeText(this, "Image attached.", Toast.LENGTH_SHORT).show();
				}
				// TODO: Implement image compression
				else {
					Toast.makeText(this, "Image too large.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	

	/**
    *
    * This method submits a new question to the server, with the current
    * user as its author. It creates a new post object and coverts it to
    * a question thread object.
    *
    * @param 
    * @see 
    */
	public void submitQuestion(View v) {
		if (isConnected()) {
			textFieldEntry = textField.getText().toString();
			Post newPost = new Post(curState.getUser(), textFieldEntry);
			PostController pc = new PostController(newPost);
			if (image != null) {
				pc.attachImage(imageString);
			}
			QuestionThread newQuestion = new QuestionThread(newPost);
			QuestionThreadController qtc = new QuestionThreadController(newQuestion);
			AsyncSave task=new AsyncSave();
			task.execute(new QuestionThreadController[] {qtc});
			lsh.saveQuestionThread(context, newQuestion, Constants.MY_QUESTIONS_FILENAME,Constants.MY_QUESTIONS_IDS_FILENAME);
			lsh.deleteFile(context, Constants.QUESTION_TEXT_FILE);
			// set image to null to avoid lingering attachment
			image = null;
		}
		else {
			Toast.makeText(context, "Could not post question. Check network connection and try again", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean isConnected() {
		//Determine if the user has connectivity
		//http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
		//22 November, 2014
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}
	
	/**
    *
    * This inner class uses AsyncTask to call the saveThread() method in
    * the QuestionThreadController which saves the question in the server.
    *
    * @param 
    * @see 
    */
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
