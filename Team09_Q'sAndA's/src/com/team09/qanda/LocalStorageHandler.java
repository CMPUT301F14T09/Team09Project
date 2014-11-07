package com.team09.qanda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;
import com.team09.qanda.models.User;

public class LocalStorageHandler {
	//For local data storage using GSON/JSON
	private FileOutputStream os;
	private Gson gson;
	
	public LocalStorageHandler() {
		this.gson=new Gson();
	}
	
	public ThreadList getThreadList(Context context, String filename) {
		//Load all the Question threads from a given file (favourites or read later) and return a ThreadList containing them
		try {
			ArrayList<QuestionThread> qts=new ArrayList<QuestionThread>();
			ThreadList tl=new ThreadList();
			InputStreamReader in=new InputStreamReader(context.openFileInput(filename));
			JsonReader reader=new JsonReader(in);
		/*	reader.beginArray();
			while (reader.hasNext()) {
				qts.add((QuestionThread) gson.fromJson(reader, QuestionThread.class));
			} 
			reader.endArray();
			reader.close(); */
			qts.add((QuestionThread) gson.fromJson(reader, QuestionThread.class));
			tl.setThreads(qts);
			return tl; 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ThreadList();
	}
	
	public void saveQuestionThread(Context context, QuestionThread qt, String filename) {
		//Save a single question thread to a file. Can be used for favourite and read later.
		try {
			
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_APPEND));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(qt, QuestionThread.class, jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public User getUser(Context context) {
		InputStreamReader in;
		try {
			in = new InputStreamReader(context.openFileInput("user.txt"));
			JsonReader reader=new JsonReader(in);
			gson.fromJson(reader, User.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new User("null");
	}
	
	public void saveUser(Context context,User user) {
		try {
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput("user.txt", Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(user, User.class, jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveText(Context context, String text, String filename) {
		try {
			os=context.openFileOutput(filename, Context.MODE_PRIVATE);
			os.write(text.getBytes());
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getText(Context context, String filename) {
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(
					context.openFileInput(filename)));
			String text="";			
			String line=br.readLine();
			text+=line;
			while ((line=br.readLine())!=null) {
				text+=line;
			}
			br.close();
			return text;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean deleteFile(Context context, String filename) {
		String dir=context.getFilesDir().getAbsolutePath();
		File f=new File(dir,filename);
		return f.delete();
	}

}
