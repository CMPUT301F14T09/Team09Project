package com.team09.qanda.views;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.team09.qanda.ApplicationState;
import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.R;
import com.team09.qanda.R.id;
import com.team09.qanda.R.layout;
import com.team09.qanda.R.menu;
import com.team09.qanda.controllers.PostController;
import com.team09.qanda.controllers.QuestionThreadController;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

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
	private LocalStorageHandler localStorageHandler = new LocalStorageHandler();
	private Context context = this;
	private byte[] image = null;

	static final String ADD_QUESTION_RESULT = "RESULT";
	private static int IMAGE_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_add_question);		
		textField = (EditText) findViewById(R.id.add_question_field);
		
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
				if (imageSize < (64*1024)) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					selectedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
					image = out.toByteArray();
					ImageView imageView = (ImageView)findViewById(R.id.attachedImage); 
					imageView.setImageBitmap(selectedImage);
					Toast.makeText(this, "Image attached.", Toast.LENGTH_SHORT).show();
				}
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
		textFieldEntry = textField.getText().toString();
    	Post newPost = new Post(curState.getUser(), textFieldEntry);
    	PostController pc = new PostController(newPost);
    	if (image != null) {
    		pc.attachImage(image);
    	}
    	QuestionThread newQuestion = new QuestionThread(newPost);
    	QuestionThreadController qtc = new QuestionThreadController(newQuestion);

		localStorageHandler.deleteFile(getBaseContext(), "My Questions.txt");
		localStorageHandler.getThreadList(context, "My Questions.txt");
    	localStorageHandler.saveQuestionThread(context, newQuestion, "My Questions.txt");
    	
    	AsyncSave task=new AsyncSave();
		task.execute(new QuestionThreadController[] {qtc});
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
