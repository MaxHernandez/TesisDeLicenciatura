package com.maxkalavera.utils.httprequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.jsonmodels.BaseRequestJsonModel;
import com.maxkalavera.utils.jsonmodels.BaseResponseJsonModel;
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
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;



public class HttpRequestLoader extends AsyncTaskLoader<ResponseBundle> {
	private Context context;
	private OkHttpClient client;
	private Request.Builder builder;
	private JsonObject parametersJson;
	private Uri.Builder uriBuilder;
	private Boolean cookiesFlag;
	private Boolean csrfFlag;
	private Boolean JsonResponseFlag;  
	private String prefsSession;
	private String url;
	private String host;
	private String method; 
	private BaseResponseJsonModel responseJSONModel;
	
	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	public static final String GET = "GET", POST = "POST";
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public HttpRequestLoader(Context context, String url, String method, RequestParamsBundle uriParamsBundle) {
		super(context);
		this.context = context;
		this.client = new OkHttpClient();
		this.builder = new Request.Builder();
		this.uriBuilder = null;
		this.parametersJson = null;
		this.cookiesFlag = false;
		this.csrfFlag = false;
		this.JsonResponseFlag = false;
		this.prefsSession = context.getResources().getString(R.string.sessionprefsfile);
		this.url = url;
		this.method = method; 
		this.host = null;
		BaseResponseJsonModel responseJSONModel = null;
		
		this.uriBuilder = Uri.parse(url).buildUpon();
		if (this.method != GET )
			this.parametersJson = new JsonParser().parse("{}").getAsJsonObject();

		if (uriParamsBundle != null)
			this.addRequestParamsBundle(uriParamsBundle);
	}
	
	public HttpRequestLoader(Context context, String url, String method) {
		this(context, url, method, null); 
	}
	
	/************************************************************
	 * Configuration Methods
	 ************************************************************/
	public Request.Builder getBuilder(){return this.builder;}
	public void setCookiesOn(){this.cookiesFlag = true;}
	public void setCookiesOff(){this.cookiesFlag = false;}
	public void setCSRFOn(){this.csrfFlag = true;}
	public void setCSRFOff(){this.csrfFlag = false;}
	public void setJsonResponseOn(BaseResponseJsonModel responseJSONModel){this.JsonResponseFlag = true; 
		this.responseJSONModel = responseJSONModel;}
	public void setJsonResponseOff(){this.JsonResponseFlag = false;}
	
	public void setURL(String url){this.url = url;} //this.builder.url(url);};
	public void addHeader(String name, String value){this.builder.addHeader(name, value);}

	/************************************************************
	 * Request Parameters Addition Methods
	 ************************************************************/
	public void addURIParam(String name, String value){
		this.uriBuilder.appendQueryParameter(name, value);
	}
	
	public void addJSONParam(String name, String value){
		if (this.method != this.GET )
			this.parametersJson.addProperty(name, value);
	}
	
	public void addJSONObject(String name, BaseRequestJsonModel value){
		this.addJSONParam(name, value.serialize());
	}
	
	public void addRequestParamsBundle(RequestParamsBundle bundle) {		
		HashMap<String, String> uriParamsMap = bundle.getURIParamsMap();
		if (uriParamsMap != null) {
			Set<Entry<String, String>> uriParamsSet = uriParamsMap.entrySet();
			for (Entry<String, String> entry : uriParamsSet) {
				this.addURIParam(entry.getKey(), entry.getValue());
			}
		}
		
		HashMap<String, String> jsonParamsMap = bundle.getJSONParamsMap();
		if (jsonParamsMap != null) {
			Set<Entry<String, String>> jsonParamsSet = jsonParamsMap.entrySet();
			for (Entry<String, String> entry : jsonParamsSet) {
				this.addJSONParam(entry.getKey(), entry.getValue());
			}
		}
		
		HashMap<String, BaseRequestJsonModel> jsonModelMap = bundle.getJsonModelMap();
		if (jsonModelMap != null) {
			Set<Entry<String, BaseRequestJsonModel>> jsonModelSet = jsonModelMap.entrySet();
			for (Entry<String, BaseRequestJsonModel> entry : jsonModelSet) {
				this.addJSONObject(entry.getKey(), entry.getValue());
			}
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
			this.addURIParam("csrf_token", csrf_token);
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
	 * Json Deserialize 
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 ************************************************************/
	
	/*
	public BaseResponseJsonModel deserializeJson(Response response, Class className) throws JsonSyntaxException, IOException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class[] cArg = {String.class};
		Method serializeMethod =  className.getMethod("deserialize", cArg);
		return serializeMethod.invoke( null, response.body().toString());
	}
	*/

	public BaseResponseJsonModel deserializeJson(Response response, BaseResponseJsonModel jsonModel) {
		return jsonModel.deserialize(response.body().toString());
	}

	
	/************************************************************
	 * Main Method
	 ************************************************************/
	@Override
	public ResponseBundle loadInBackground(){
		return sendHTTPRequest();
	}
	
	
	public ResponseBundle sendHTTPRequest(){
		Response response = null;
		BaseResponseJsonModel jsonModel = null;
		
		// Module to load up Cookies
		try {
			if (this.cookiesFlag) this.loadCookies();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ResponseBundle(response);  
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
				this.builder.url( this.uriBuilder.build().toString());
				
				Gson gson = new Gson();
				String json = gson.toJson(this.parametersJson);
				this.builder.url(this.url);
				this.builder.method(this.method, RequestBody.create(JSON, json));
			}			
			Request request = this.builder.build();
			response = client.newCall(request).execute();
		}catch (IOException e) {
			e.printStackTrace();
			return new ResponseBundle(null); 
		}
		
		// Parse the Json to an object if is necesary
		if (JsonResponseFlag) {
			try{
				jsonModel = this.deserializeJson(response, this.responseJSONModel);
			}catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ResponseBundle(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		
		// Check if the request has been successful
		if (!response.isSuccessful())
			return new ResponseBundle(null, jsonModel); 
		
		// Save the response's cookies
		try{
			if (this.cookiesFlag) this.saveCookies(response);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseBundle(null, jsonModel); 
		}
		
		// Save de CSRF Token
		if (this.csrfFlag)
			this.csrfSave(response);
		
		return new ResponseBundle(response, jsonModel); 	
	}
	
	
};
