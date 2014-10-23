package com.maxkalavera.ecoar.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.UserSession;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.Pair;

public class MainCheckSessionLoader extends AsyncTaskLoader<Boolean>  {
	String url;
	UserSession userSession;
	HTTPRequest requestHandler;
	
	public MainCheckSessionLoader(Context context, UserSession userSession) {
		super(context);
		this.userSession =  userSession;
		this.requestHandler = new HTTPRequest(context);
		this.url = context.getResources().getString(R.string.webservice_csession);
	}
	
	@Override
	public Boolean loadInBackground() {
		List<Integer> acceptanceStatusCodes = new LinkedList<Integer>();
		acceptanceStatusCodes.add(HttpStatus.SC_OK);
		acceptanceStatusCodes.add(HttpStatus.SC_FORBIDDEN);
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
			 		this.userSession.setSessionStatus(false);
			 		return false;
			 	default:
			 		return null;
			 }
		 }
		
		return null;
	}

}
