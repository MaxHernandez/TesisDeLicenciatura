package com.maxkalavera.utils;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.squareup.okhttp.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public class LogoutChecker {

	public static void checkSessionOnResponse(Context context, Response response) {
		if (context == null || response == null) return;
		
		if (response.code() == 403) {
        	UserSessionDAO userSessionDAO = new UserSessionDAO(context);
        	userSessionDAO.setSessionStatus(false);
        	
        	UserDataDAO userDataDAO = new UserDataDAO(context);
        	userDataDAO.removeUserDataProfile();
        	
    		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
    	    dialog.setTitle(context.getResources().getString(R.string.logout_checker_title));
    	    dialog.setMessage(context.getResources().getString(R.string.logout_checker_message));
    		dialog.setPositiveButton(
    	    		context.getResources().getString(R.string.error_afirmative_button),
    	    		new DialogInterface.OnClickListener() {
    	    			Context context;
    	    			
    	    			public DialogInterface.OnClickListener setParams(Context context) {
    	    				this.context = context;
    	    				return this;
    	    			}
    	    			
    	    			public void onClick(DialogInterface dialog, int id) {  
    	    	        	Intent intent = new Intent(context, Home.class);
    	    	        	context.startActivity(intent);
    	    	        	((FragmentActivity) context).finish();
    	    			}
    	    		}.setParams(context)
    	    		);
    	    dialog.show();
		}
		
	}
}
