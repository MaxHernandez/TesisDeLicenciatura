package com.maxkalavera.ecoar.LogOut;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Pair;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.HTTPRequestTemp;
import com.maxkalavera.utils.UserSession;

public class LogOutLoader extends AsyncTaskLoader<Boolean> {
	String url;
	HTTPRequestTemp requestHandler; 
	UserSession userSession;
	
	public LogOutLoader(Context context) {
		super(context);
		this.requestHandler = new HTTPRequestTemp(context);
		this.url = context.getResources().getString(R.string.webservice_logout);
	}

	@Override
	public Boolean loadInBackground() {
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		
		List<BasicNameValuePair> headers = new LinkedList<BasicNameValuePair>();
		headers.add(new BasicNameValuePair("Accept", "application/json"));
		
		List<Integer> acceptanceStatusCodes = new LinkedList<Integer>();
		acceptanceStatusCodes.add(HttpStatus.SC_ACCEPTED);
		acceptanceStatusCodes.add(HttpStatus.SC_UNAUTHORIZED);
		acceptanceStatusCodes.add(HttpStatus.SC_FORBIDDEN);
		
		 Pair<String, Integer> responsePair = 
				 this.requestHandler.sendSessionPostRequest(
						 getContext(),
						 this.url, 
						 params, 
						 headers, 
						 acceptanceStatusCodes);	
		 
		 if (responsePair != null) {
			 String responseData = (String) responsePair.first;
			 int responseStatusCode = (Integer) responsePair.second;
			 
			 switch(responseStatusCode) {
			 	case HttpStatus.SC_ACCEPTED:
			 		return new Boolean(true);
			 	case HttpStatus.SC_UNAUTHORIZED:
			 		return new Boolean(false);
			 	case HttpStatus.SC_FORBIDDEN:
			 		return new Boolean(false);
			 	default:
			 		return null;
			 }
		 }
		 return null;
	}

}
