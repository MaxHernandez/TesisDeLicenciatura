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

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.datamodels.ProductModel;
import com.maxkalavera.utils.jsonmodels.CSRFJsonModel;

public class LoginFragmentLoginLoader extends AsyncTaskLoader<Boolean> {
	String usernameBuff;
	String passwordBuff;
	String url;
	HTTPRequest requestHandler; 
	
	public LoginFragmentLoginLoader(Context context, String username, String password) {
		super(context);
		this.requestHandler = new HTTPRequest(context);
		this.usernameBuff = username;
		this.passwordBuff = password;
		this.url = context.getResources().getString(R.string.webservice_login);
	}

	@Override
	public Boolean loadInBackground() {
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", String.valueOf(this.usernameBuff)));
		params.add(new BasicNameValuePair("password", String.valueOf(this.passwordBuff)));
		
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
