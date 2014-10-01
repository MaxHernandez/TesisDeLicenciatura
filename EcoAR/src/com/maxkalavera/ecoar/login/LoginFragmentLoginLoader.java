package com.maxkalavera.ecoar.login;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.Pair;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.Product;
import com.maxkalavera.utils.jsonmodels.CSRFJsonModel;

public class LoginFragmentLoginLoader extends AsyncTaskLoader<Boolean> {
	String username = "max";
	String password = "123456";
	String url = "http://192.168.1.73:8080/login/";
	String prefsSession = "Session_prefs";
	HTTPRequest requestHandler = new HTTPRequest();
	
	public LoginFragmentLoginLoader(Context context, String username, String password) {
		super(context);
		this.username = username;
		this.password = password;
	}

	@Override
	public Boolean loadInBackground() {
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", String.valueOf(this.username)));
		params.add(new BasicNameValuePair("password", String.valueOf(this.password)));
		
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
			 Log.e("EcoAR-ResponseData", responseData);
			 
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
