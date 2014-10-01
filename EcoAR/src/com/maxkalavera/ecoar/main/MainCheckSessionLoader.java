package com.maxkalavera.ecoar.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.maxkalavera.utils.HTTPRequest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Pair;

public class MainCheckSessionLoader extends AsyncTaskLoader<Boolean>  {
	String url = "http://192.168.1.73:8080/csession/";
	HTTPRequest requestHandler = new HTTPRequest();
	
	public MainCheckSessionLoader(Context context) {
		super(context);
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
			 	case HttpStatus.SC_ACCEPTED: 
			 		return true;
			 	case HttpStatus.SC_FORBIDDEN:
			 		return false;
			 	default:
			 		return null;
			 }
		 }
		
		return null;
	}

}
