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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.team09.qanda.controllers.ThreadListController;
import com.team09.qanda.esearch.ElasticSearchHandler;
import com.team09.qanda.models.QuestionThread;
import com.team09.qanda.models.ThreadList;

/**
 * This class handles all of the logic behind saving things to and getting
 * things from the phone's local storage. Objects are stored as their JSON
 * equivalent string using GSON.
 */
public class LocalStorageHandler {
	private FileOutputStream os;
	private Gson gson;
	private static final String FILENAME = "user.txt";
	private ElasticSearchHandler esh;
	
	public LocalStorageHandler() {
		this.gson=new GsonBuilder().create();
		this.esh=new ElasticSearchHandler();
	}
	
	/**
	 * This method loads a ThreadList from local storage
	 * @param context The context from which the method is being called
	 * @param filename The file to get the ThreadList from
	 * @return The ThreadList from the file
	 */
	public ThreadList getThreadList(Context context, String filename) {
		try {
			ThreadList tl=new ThreadList();
			InputStreamReader in=new InputStreamReader(context.openFileInput(filename));
			JsonReader reader=new JsonReader(in);
			tl = (ThreadList) gson.fromJson(reader, ThreadList.class);;
			return tl; 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ThreadList();
	}
	
	/**
	 * Deletes a given QuestionThread from local storage
	 * @param context The context from which the method is being called
	 * @param qt The QuestionThread to be deleted
	 * @param filename The file to delete the QuestionThread from
	 */
	public void deleteQuestionThread(Context context, QuestionThread qt, String filename) {
		ThreadList tl=getThreadList(context, filename);
		ThreadListController tlc = new ThreadListController(tl);
		tlc.removeThread(qt);
		saveQuestionThreads(context, tl.getThreads(), filename);
	}
	
	/**
	 * Deletes a given QuestionThread from local storage, as well as
	 * its associated id
	 * @param context The context from which the method is being called
	 * @param qt The QuestionThread to be deleted
	 * @param filename The file to delete the QuestionThread from
	 * @param id_filename The file to delete the id from
	 */
	public void deleteQuestionThread(Context context, QuestionThread qt, String filename, String id_filename) {
		ThreadList tl=getThreadList(context, filename);
		ThreadListController tlc = new ThreadListController(tl);
		tlc.removeThread(qt);
		saveQuestionThreads(context, tl.getThreads(), filename);
		deleteId(context, qt.getId(), id_filename);
	}
	
	/**
	 * Adds a QuestionThread to a ThreadList in local storage
	 * @param context The context from which the method is being called
	 * @param qt The QuestionThread to be saved
	 * @param filename The file to save the QuestionThread to
	 */
	public void saveQuestionThread(Context context, QuestionThread qt, String filename) {
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
	
	/**
	 * Adds a QuestionThread to a ThreadList in local storage, as well as its
	 * associated id to a separate file
	 * @param context The context from which the method is being called
	 * @param qt The QuestionThread to be saved
	 * @param filename The file to save the QuestionThread to
	 * @param id_filename The file to save the id to
	 */
	public void saveQuestionThread(Context context, QuestionThread qt, String filename, String id_filename) {
		try {
			ThreadList threadList = getThreadList(context, filename);
			ThreadListController tlc = new ThreadListController(threadList);
			tlc.addThread(qt);
			deleteFile(context, filename);
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(threadList, ThreadList.class, jw);
			saveId(context, qt.getId(), id_filename);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes a ThreadList from a list of QuestionThreads and saves it to local storage
	 * @param context The context from which the method is being called
	 * @param qts The list of QuestionThreads to be saved
	 * @param filename The file to save the ThreadList to
	 */
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
	
	/**
	 * Makes a ThreadList from a list of QuestionThreads and saves it to local storage, as
	 * well as saving all of their associated ids to a separate file
	 * @param context The context from which the method is being called
	 * @param qts The list of QuestionThreads to be saved
	 * @param filename The file to save the ThreadList to
	 * @param id_filename The file to save the ids to
	 */
	public void saveQuestionThreads(Context context, ArrayList<QuestionThread> qts, String filename, String id_filename) {
		try {
			ThreadList threadList = new ThreadList();
			ThreadListController tlc = new ThreadListController(threadList);
			ArrayList<String> ids=new ArrayList<String>();
			for (QuestionThread qt:qts) {
				tlc.addThread(qt);
				ids.add(qt.getId());
			}
			deleteFile(context, filename);
			OutputStreamWriter osw=new OutputStreamWriter(
					context.openFileOutput(filename, Context.MODE_PRIVATE));
			JsonWriter jw=new JsonWriter(osw);
			gson.toJson(threadList, ThreadList.class, jw);
			saveIds(context, ids, id_filename);
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveId(Context context, String id, String filename) {
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
	
	private void saveIds(Context context, ArrayList<String> ids, String filename) {
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
	
	/**
	 * Gets a list of ids from local storage. Can be used to check which
	 * questions are currently saved. Faster than loading all of the questions
	 * @param context The context from which the method is being called
	 * @param filename The file to get the ids from
	 * @return An ArrayList of strings
	 */
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
	
	private void deleteId(Context context, String id, String filename) {
		ArrayList<String> ids=getIds(context, filename);
		while (ids.contains(id)) {
			ids.remove(id);
		}
		saveIds(context, ids, filename);
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
	
	/**
	 * Saves a string to local storage. Used to make text boxes persistent
	 * @param context The context from which the method is being called
	 * @param text Text to be saved
	 * @param filename The file to save the text to
	 */
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
	
	/**
	 * Gets a string from local storage. Used to set the text of the text boxes
	 * @param context The context from which the method is being called
	 * @param filename The file to get the text from
	 * @return A string containing the contents of the file
	 */
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
			if (text.equals("null")) {
				text="";
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
	
	/**
	 * This method retrieves all of the questions which are in local storage
	 * from the server and saves them again to their files. This ensures
	 * users have the most current version of their saved questions.
	 * 
	 * @param context The context from which the method is being called
	 */
	public void refreshLocals(Context context) {
		ArrayList<String> later_ids=getIds(context, Constants.LATER_IDS_FILENAME);
		ArrayList<String> favourite_ids=getIds(context, Constants.FAVOURITE_IDS_FILENAME);
		ArrayList<String> my_qs_ids=getIds(context, Constants.MY_QUESTIONS_IDS_FILENAME);
		refreshLocal(context, Constants.READ_LATER_FILENAME, later_ids);
		refreshLocal(context, Constants.FAVOURITES_FILENAME, favourite_ids);
		refreshLocal(context, Constants.MY_QUESTIONS_FILENAME, my_qs_ids);
	}
	
	private void refreshLocal(Context context,String filename, ArrayList<String> ids) {
		ArrayList<QuestionThread> qts=new ArrayList<QuestionThread>();
		for (String id:ids) {
			QuestionThread qt=esh.getThread(id);
			qts.add(qt);
		}
		deleteFile(context, filename);
		saveQuestionThreads(context, qts, filename);
	}
	
	public boolean deleteFile(Context context, String filename) {
		String dir=context.getFilesDir().getAbsolutePath();
		File f=new File(dir,filename);
		return f.delete();
	}

}
