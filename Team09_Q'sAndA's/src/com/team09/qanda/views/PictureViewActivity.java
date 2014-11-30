package com.team09.qanda.views;

import com.team09.qanda.ImageHandler;
import com.team09.qanda.R;
import com.team09.qanda.R.id;
import com.team09.qanda.R.layout;
import com.team09.qanda.R.menu;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Activity used to view an image attached to a post.
 *
 */
public class PictureViewActivity extends Activity {

	private Post post;
	private Bitmap image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_view);
		
		post = (Post) getIntent().getExtras().getSerializable("Selected Post");
		ImageView postImage = (ImageView) findViewById(R.id.postImage);
		TextView postText = (TextView) findViewById(R.id.postText);
		TextView postAuthor = (TextView) findViewById(R.id.postAuthor);
		ImageHandler imageHandler = new ImageHandler();
		String imageString = post.getImage();
		image = imageHandler.stringToBitmap(imageString);
		postImage.setImageBitmap(image);
		postText.setText(post.getText());
		postAuthor.setText("- "+post.getAuthor().getName());

		postImage.setLongClickable(true);
		postImage.setOnLongClickListener(new ImageView.OnLongClickListener() { 
	        public boolean onLongClick(View v) {
	        	imageClick();
	            return true;
	        }
	    });
	}
	
	public void imageClick() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage("Save image?")
               .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   try {
                		   if (MediaStore.Images.Media.insertImage(getContentResolver(), image, "Image by " + post.getAuthor().getName() + " from Team09_Q'sAndA's" , post.getText()) != null) {
                			   Toast.makeText(getApplicationContext(), "Image saved.", Toast.LENGTH_SHORT).show();
                		   }
                		   else {
      		            		Toast.makeText(getApplicationContext(), "Image not saved.", Toast.LENGTH_SHORT).show();
                		   }

                	   } catch(Exception E) {
   		            		Toast.makeText(getApplicationContext(), "Image not saved.", Toast.LENGTH_SHORT).show();
                	   }
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                   }
               });
		dialog.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture_view, menu);
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
