package com.maxkalavera.ecoar.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.maxkalavera.utils.HTTPRequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.Pair;

public class MainCheckSessionLoader extends AsyncTaskLoader<Boolean>  {
	String url = "http://192.168.1.73:8080/csession/";
	String prefsSession = "Session_prefs";
	HTTPRequest requestHandler = new HTTPRequest();
	
	public MainCheckSessionLoader(Context context) {
		super(context);
	}
	
	@Override
	public Boolean loadInBackground() {
		List<Integer> acceptanceStatusCodes = new LinkedList<Integer>();
		acceptanceStatusCodes.add(HttpStatus.SC_OK);
		acceptanceStatusCodes.add(HttpStatus.SC_FORBIDDEN);
		 Log.e("EcoAR-DEBG", "Menos UNO");
		 Pair<String, Integer> responsePair = 
				 this.requestHandler.sendSessionGetRequest(
						 getContext(),
						 this.url, 
						 null, 
						 null, 
						 acceptanceStatusCodes);
		 
		 if (responsePair != null) {
			 String responseData = (String) responsePair.first;
			 int responseStatusCode = (Integer) responsePair.second;
			 switch(responseStatusCode) {
			 	case HttpStatus.SC_OK: 
			 		return true;
			 	case HttpStatus.SC_FORBIDDEN:
			 		SharedPreferences sessionSharedPreferences = getContext().getSharedPreferences(this.prefsSession, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sessionSharedPreferences.edit();
					editor.putBoolean("sessionAuthenticated", false);
					editor.commit();
			 		return false;
			 	default:
			 		return null;
			 }
		 }
		
		return null;
	}

}
