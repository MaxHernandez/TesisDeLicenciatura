package com.maxkalavera.utils;

import android.app.ActionBar;
import android.app.Activity;

public class ActionBarHandler {
	private Activity activity;
	
	public ActionBarHandler(Activity activity) {
		this.activity = activity;
		ActionBar actionBar = this.activity.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true); 		
	}
	
}
