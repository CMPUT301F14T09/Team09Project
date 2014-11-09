package com.team09.qanda.views;

import com.team09.qanda.R;
import com.team09.qanda.R.id;
import com.team09.qanda.R.layout;
import com.team09.qanda.R.menu;
import com.team09.qanda.models.Post;
import com.team09.qanda.models.QuestionThread;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Activity used to view an image attached to a post.
 *
 */
public class PictureViewActivity extends Activity {

	private Post post;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_view);
		
		post = (Post) getIntent().getExtras().getSerializable("Selected Post");
		ImageView postImage = (ImageView) findViewById(R.id.postImage);
		TextView postText = (TextView) findViewById(R.id.postText);
		postImage.setImageBitmap(post.getImage());
		postText.setText(post.getText());

		
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
