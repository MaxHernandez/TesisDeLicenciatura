package com.maxkalavera.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.maxkalavera.utils.httprequest.InternetStatus;

public class InternetStatusChecker {

	public static boolean checkInternetStauts(Context context) {
		InternetStatus internetStatus = new InternetStatus(context);
		if (internetStatus.isOnline())
			return true;

		ErrorMesages.noInternetService(context);
	    
		return false;
	}
	
}
