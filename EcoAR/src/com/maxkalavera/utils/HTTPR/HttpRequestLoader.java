package com.maxkalavera.utils.HTTPR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.jsonmodels.CSRFJsonModel;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;



public class HttpRequestLoader extends AsyncTaskLoader<Response> {
	Context context;
	OkHttpClient client;
	Request.Builder builder;
	JsonObject parametersJson;
	Uri.Builder uriBuilder; 
	Boolean cookiesFlag;
	Boolean csrfFlag; 
	String prefsSession;
	String url;
	String host;
	String method; 
	
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	public static final String GET = "GET", POST = "POST";
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public HttpRequestLoader(Context context, String url, String method) {
		super(context);
		this.context = context;
		this.client = new OkHttpClient();
		this.builder = new Request.Builder();
		this.cookiesFlag = false;
		this.csrfFlag = false;
		this.prefsSession = context.getResources().getString(R.string.sessionprefsfile);
		this.url = url;
		this.method = method; 
		this.host = null;
		
		if (this.method == this.GET ) {
			this.uriBuilder = Uri.parse(url).buildUpon();
		}else{
			this.parametersJson = new JsonParser().parse("{}").getAsJsonObject();
		}

		
	}
	
	/************************************************************
	 * Configuration Methods
	 ************************************************************/
	public Request.Builder getBuilder(){return this.builder;}
	public void setCookiesOn(){this.cookiesFlag = true;}
	public void setCookiesOff(){this.cookiesFlag = false;}
	public void setCSRFOn(){this.csrfFlag = true;}
	public void setCSRFOff(){this.csrfFlag = false;}	
	public void setURL(String url){this.url = url;} //this.builder.url(url);};
	public void addHeader(String name, String value){this.builder.addHeader(name, value);}

	/************************************************************
	 * Request Parameters Adition Methods
	 ************************************************************/
	public void addParam(String name, String value){
		if (this.method == this.GET ) {
			this.uriBuilder.appendQueryParameter(name, value);
		}else{
			this.parametersJson.addProperty(name, value);
		}
	}
	
	/************************************************************
	 * Cookies Methods
	 ************************************************************/
	private void loadCookies() throws MalformedURLException {
		URL uri = new URL(this.url);
		String host = uri.getHost();
		CookieManager cookieManager = CookieManager.getInstance();
		String cookie = cookieManager.getCookie(host);
		if (cookie != null) {
			this.getBuilder().addHeader("Cookie", cookie);
		}
	}
	
	private void saveCookies(Response response) {
		CookieManager cookieManager = CookieManager.getInstance();
		List<String> cookies = response.headers("Set-Cookie");
		for (int i = 0; i < cookies.size(); i++){
			cookieManager.setCookie(this.host, cookies.get(i));
		}
		if (cookies.size() > 0)
			CookieSyncManager.getInstance().sync();
	}
	
	/************************************************************
	 * CSRF Protection Methods
	 ************************************************************/
	private void csrfLoad() {
		SharedPreferences sessionSharedPreferences = this.context.getSharedPreferences(prefsSession, Context.MODE_PRIVATE);
		String csrf_token = sessionSharedPreferences.getString("csrf_token", null);
		if (csrf_token != null) {
			this.addParam("csrf_token", csrf_token);
		}
	}
	
	private void csrfSave(Response response) {
		CSRFJsonModel csrfJsonModel;
		try {
			csrfJsonModel = CSRFJsonModel.deserialize(response.body().string());
			if (csrfJsonModel != null) {
				SharedPreferences sessionSharedPreferences = this.context.getSharedPreferences(prefsSession, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sessionSharedPreferences.edit();
				editor.putString("csrf_token", csrfJsonModel.csrf_token);
				editor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/************************************************************
	 * Main Method
	 ************************************************************/
	@Override
	public Response loadInBackground(){
		Response response = null;
		
		// Module to load up Cookies
		try {
			if (this.cookiesFlag) this.loadCookies();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		// Loads the last crfg value receibed 
		if (this.csrfFlag)
			this.csrfLoad();
		
		// This part makes the Request to the server
		try {						
			if (this.method == this.GET ) {
				this.builder.url( this.uriBuilder.build().toString());
				this.builder.method(this.GET, null);
			}else{
				Gson gson = new Gson();
				String json = gson.toJson(this.parametersJson);
				
				this.builder.url(this.url);
				this.builder.method(this.method, RequestBody.create(JSON, json));
			}			
			Request request = this.builder.build();
			response = client.newCall(request).execute();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		// Save the response's cookies
		try{
			if (this.cookiesFlag) this.saveCookies(response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// Check if the request has been successful
		if (!response.isSuccessful())
			return null;
		
		if (this.csrfFlag)
			this.csrfSave(response);
		
		return null; 
	}
	
	
};
