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
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

public class LocalStorageHandler {
	//For local data storage using GSON/JSON
	private FileOutputStream os;
	private Gson gson;
	private static final String FILENAME = "user.txt";
	
	public LocalStorageHandler() {
		this.gson=new GsonBuilder().setPrettyPrinting().create();
	}
	
	public void saveId(Context context, String id, String filename) {
		try {
			ArrayList<String> ids=getIds(context, filename);
			ids.add(id);
			deleteFile(context, filename);
			OutputStreamWriter osw = new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(ids,new TypeToken<ArrayList<String>>(){}.getType(),jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveIds(Context context, ArrayList<String> ids, String filename) {
		try {
			deleteFile(context, filename);
			OutputStreamWriter osw = new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(ids,new TypeToken<ArrayList<String>>(){}.getType(),jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getIds(Context context, String filename) {
		InputStreamReader in;
		try {
			in = new InputStreamReader(context.openFileInput(filename));
			JsonReader reader=new JsonReader(in);
			ArrayList<String> ids=gson.fromJson(reader, new TypeToken<ArrayList<String>>(){}.getType());
			in.close();
			return ids;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public ThreadList getThreadList(Context context, String filename) {
		//Load all the Question threads from a given file (favourites or read later) and return a ThreadList containing them
		try {
			ThreadList tl=new ThreadList();
			InputStreamReader in=new InputStreamReader(context.openFileInput(filename));
			JsonReader reader=new JsonReader(in);
			tl = (ThreadList) gson.fromJson(reader, ThreadList.class);;
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
			ThreadList threadList = getThreadList(context, filename);
			ThreadListController tlc = new ThreadListController(threadList);
			tlc.addThread(qt);
			deleteFile(context, filename);
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(threadList, ThreadList.class, jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveQuestionThreads(Context context, ArrayList<QuestionThread> qts, String filename) {
		try {
			ThreadList threadList = new ThreadList();
			ThreadListController tlc = new ThreadListController(threadList);
			for (QuestionThread qt:qts) {
				tlc.addThread(qt);
			}
			deleteFile(context, filename);
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(threadList, ThreadList.class, jw);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername(Context context) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));
			String line;
			if ((line = input.readLine()) != null){
				return line;
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveUsername(Context context,String username) {
		try {
			FileOutputStream fos;
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(username.getBytes());
			fos.close();
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
