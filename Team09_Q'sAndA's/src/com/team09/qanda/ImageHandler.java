package com.team09.qanda;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

public class ImageHandler {

	public int MAX_IMAGE_SIZE = (64*1024);
	
	public Bitmap handleImage(Intent data, Context context) {
		Uri uri = data.getData();
		InputStream input = null; 
		try {
			input = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Bitmap image = getImageFromStream(input);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, out);
		int imageByteSize = out.toByteArray().length;
		if (imageByteSize > MAX_IMAGE_SIZE) {
			image = compressImage(image);
		}
		return image;
	}
	
	public Bitmap getImageFromStream(InputStream input) {
		Bitmap image = BitmapFactory.decodeStream(input);
		return image;
	}
	
	public Bitmap getImageFromArray(byte[] imageArray, BitmapFactory.Options bmpFactoryOptions) {
	    Bitmap bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, bmpFactoryOptions);
		return bitmap;
	}
	
	public String imageToString(Bitmap image) {
		byte[] imageByte = arrayFromImage(image);
	    String imageString = Base64.encodeToString(imageByte, Base64.DEFAULT);
		return imageString;
	}
	
	public byte[] arrayFromImage(Bitmap image) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, out);
		byte[] imageByte = out.toByteArray();
		return imageByte;
	}
	
	public Bitmap compressImage(Bitmap image) {
		byte[] imageByte = arrayFromImage(image);
		int imageByteSize = imageByte.length;
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		int size = 2;
		// Image actually gets compressed more than this
		while ((imageByteSize/size) > (MAX_IMAGE_SIZE*2)) {
			size+=1;
		}
		bmpFactoryOptions.inSampleSize = size;
		image = getImageFromArray(imageByte, bmpFactoryOptions);
		return image;
	}
}
