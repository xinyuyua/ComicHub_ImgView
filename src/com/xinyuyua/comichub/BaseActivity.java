package com.xinyuyua.comichub;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * Activity for quit the app
 * Copy from internet
 * Call exit() to exit the app
 *
 */

public class BaseActivity extends Activity {
	public static LinkedList<Activity> sAllActivitys = new LinkedList<Activity>();
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		sAllActivitys.add(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		sAllActivitys.remove(this);
	}

	
	public static void finishAll() {
		for(Activity activity : sAllActivitys) {
			activity.finish();
		}
		
		sAllActivitys.clear();
	}

	public static void exit() {
		finishAll();
		System.exit(0);
	}
}
