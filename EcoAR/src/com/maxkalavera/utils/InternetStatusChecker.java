package com.maxkalavera.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.maxkalavera.utils.httprequest.InternetStatus;

public class InternetStatusChecker {

	public static boolean checkInternetStauts(Context context) {
		InternetStatus internetStatus = new InternetStatus(context);
		if (internetStatus.isOnline())
			return true;

		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	    dialog.setTitle("Fallo en la conexion");
	    dialog.setMessage("Se necesita conexion a internet para utilizar este servicio.");
	    dialog.setPositiveButton("Ok",null);
	    dialog.show();
	    
		return false;
	}
	
}
