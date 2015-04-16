package com.maxkalavera.utils.database;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionDAO {
	private Context context;
	private String sharedPreferencesKeyword;
	private String sessionStatusKeyword;

	/*************************************************************
	 * 
	 *************************************************************/
	public UserSessionDAO(Context context) {
		this.context = context;
		this.sharedPreferencesKeyword = context.getResources().getString(R.string.session_filename);
		this.sessionStatusKeyword = context.getResources().getString(R.string.session_status_keyword);
	}
	
	public Context getContext() {
		return this.context;
	}
	
	/*************************************************************
	 * 
	 *************************************************************/
	public Boolean checkSessionStatus() {
		SharedPreferences sessionSharedPreferences = this.getContext().getSharedPreferences(
				this.sharedPreferencesKeyword, 
				Context.MODE_PRIVATE);
		
		if (sessionSharedPreferences .contains(this.sessionStatusKeyword)) {
			Boolean status;
			status = sessionSharedPreferences.getBoolean(this.sessionStatusKeyword , 
					false);
			return status;
		}
		
		// 
		/*UserDataDAO userDataDAO = new UserDataDAO(this.getContext());
		UserDataJsonModel userData = userDataDAO.getUserData();
		if ( (userData != null) || (userData.sessionStatus != null) ) {
			SharedPreferences.Editor editor = sessionSharedPreferences.edit();
			editor.putBoolean(this.sessionStatusKeyword, userData.sessionStatus);
			editor.commit();
			return userData.sessionStatus;
		}*/
		
		return null;
	}
	
	/*************************************************************
	 * 
	 *************************************************************/
	public void setSessionStatus(Boolean status) {
		// Save in shared preferences 
		SharedPreferences sharedPreferences = 
				this.getContext().getSharedPreferences(
						this.sharedPreferencesKeyword, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(this.sessionStatusKeyword, status);
		editor.commit();
		
		// Save in internal memory 
		/*UserDataDAO userDataDAO = new UserDataDAO(this.getContext());
		UserDataJsonModel userData = userDataDAO.getUserData();
		userData.sessionStatus = status;
		userDataDAO.saveUserData(userData);*/
	}
}
