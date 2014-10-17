package com.team09.qanda.test;

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.test.ActivityInstrumentationTestCase2;

import com.team09.qanda.LocalStorageHandler;
import com.team09.qanda.Post;
import com.team09.qanda.QuestionThread;
import com.team09.qanda.QuestionThreadActivity;
import com.team09.qanda.ThreadList;
import com.team09.qanda.User;

public class OfflineTest extends ActivityInstrumentationTestCase2<QuestionThreadActivity> {
	
	private Context context;
	
	public OfflineTest() {
		super(QuestionThreadActivity.class);
		this.context=getInstrumentation().getContext();
	}
	
	//Use case 19: Offline viewing
	public void testViewOffline() {
		LocalStorageHandler handler=new LocalStorageHandler();
		Post question=new Post(new User(), "Offline? No way");
		QuestionThread qt=new QuestionThread(question);
		handler.saveQuestionThread(qt, "Favourite.txt");
		handler.saveQuestionThread(qt, "Later.txt");
		WifiManager wifi=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);
		ThreadList favs=handler.getThreadList("Favourite.txt");
		ThreadList laters=handler.getThreadList("Later.txt");
		ArrayList<String> favQs=new ArrayList<String>();
		ArrayList<String> laterQs=new ArrayList<String>();
		for (Post q:favs.getQuestions()) {
			favQs.add(q.getText());
		}
		for (Post q:laters.getQuestions()) {
			laterQs.add(q.getText());
		}
		assertTrue(favQs.contains("Offline? No way"));
		assertTrue(laterQs.contains("Offline? No way"));
	}
}
