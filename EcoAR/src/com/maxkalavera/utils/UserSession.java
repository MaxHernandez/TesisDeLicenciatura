package com.maxkalavera.utils;

import com.maxkalavera.ecoar.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
	private Activity activity;
	private String prefsSession;
	private String sessionStatusKeyword;
	
	public UserSession(Activity activity) {
		this.activity = activity;
		this.prefsSession = activity.getResources().getString(R.string.sessionprefsfile);
		this.sessionStatusKeyword = activity.getResources().getString(R.string.sessionstatuskeyword);
	}
	
	public boolean checkSessionStatus() {
		return this.activity.getSharedPreferences(
				this.prefsSession, Context.MODE_PRIVATE).getBoolean(this.sessionStatusKeyword , false);
	}
	
	public void setSessionStatus(boolean status) {
		SharedPreferences sessionSharedPreferences = 
				this.activity.getSharedPreferences(
						this.prefsSession, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sessionSharedPreferences.edit();
		editor.putBoolean(this.sessionStatusKeyword, status);
		editor.commit();
	}
}
