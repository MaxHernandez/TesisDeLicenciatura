package com.maxkalavera.ecoar.login;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.Product;

public class LoginFragmentLoginLoader extends AsyncTaskLoader<Cursor> {
	String username = "max";
	String password = "123456";
	String url = "http://192.168.1.73:8080/login/";
	HTTPRequest requestHandler = new HTTPRequest();
	
	public LoginFragmentLoginLoader(Context context, String username, String password) {
		super(context);
		//this.username = username;
		//this.password = password;
	}

	@Override
	public Cursor loadInBackground() {
		
		String data = null;
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", String.valueOf(this.username)));
		params.add(new BasicNameValuePair("password", String.valueOf(this.password)));
				
		List<BasicNameValuePair> headers = new LinkedList<BasicNameValuePair>();
		headers.add(new BasicNameValuePair("Accept", "application/json"));
		
		try{
			int responseStatusCode = this.requestHandler.sendPostRequest(this.url, params, headers);
			
			if (responseStatusCode == HttpStatus.SC_ACCEPTED ){
				data = this.requestHandler.getDataOfPostRequest();
				Log.e("EcoAR-POST", data);
			}
			CookieSyncManager.getInstance().sync();
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		
		/*
		try{
			HttpResponse response = this.requestHandler.sendPostRequestApache(this.url, params, headers);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED )
				data = EntityUtils.toString(response.getEntity());
			Log.e("EcoAR-Post", data);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		*/
		
		return null;
	}

}
