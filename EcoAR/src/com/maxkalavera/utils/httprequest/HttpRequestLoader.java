package com.maxkalavera.utils.httprequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.BaseRequestJsonModel;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;
import com.maxkalavera.utils.database.jsonmodels.CSRFJsonModel;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
	private MultipartBuilder multipartBuilder;
	private Boolean cookiesFlag;
	private Boolean csrfFlag;
	private Boolean multipartFlag;
	private String csrfSharedPreferencesFilename;
	private String csrfSharedPreferencesKeyword;
	private String url;
	private String host;
	private String method; 
	
	private BaseResponseJsonModel responseJSONModel;
	private BaseResponseJsonModel serverErrorResponseJSONModel;
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final String CSRF_TOKEN_HEADER_KEYWORD  = "X-CSRFToken";
	public static final String COOKIE  = "Cookie";
	public static final String COOKIES_HEADER  = "Set-Cookie";
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String DELETE = "DELETE";
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
	public HttpRequestLoader(Context context, String url, String method, RequestParamsBundle paramsBundle) {
		super(context);
		this.context = context;
		this.url = url;
		this.method = method; 
		
		this.builder = null;
		this.uriBuilder = null;
		this.parametersJson = null;
		
		this.cookiesFlag = false;
		this.csrfFlag = false;
		this.multipartFlag = false;
		
		this.host = null;
		this.responseJSONModel = null;
		this.serverErrorResponseJSONModel = null;
		
		this.client = new OkHttpClient();
		this.csrfSharedPreferencesFilename = context.getResources().getString(R.string.csrf_filename);
		this.csrfSharedPreferencesKeyword = context.getResources().getString(R.string.csrf_csrftoken_keyword);
		
		this.uriBuilder = Uri.parse(url).buildUpon();
		if (this.method != GET )
			this.parametersJson = new JsonParser().parse("{}").getAsJsonObject();

		if (paramsBundle != null)
			this.addRequestParamsBundle(paramsBundle);
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
	
	public void setJsonResponseOn(BaseResponseJsonModel responseJSONModel){
		this.responseJSONModel = responseJSONModel;}
	public void setJsonResponseOff(){this.responseJSONModel = null;}
	
	public void setJsonResponseServerErrorOn(BaseResponseJsonModel responseJSONModel){
		this.serverErrorResponseJSONModel = responseJSONModel;}
	public void setJsonResponseServerErrorOff(){this.serverErrorResponseJSONModel = null;}
	//
	public void setMultipartOn(){
		this.multipartFlag = true;
		this.multipartBuilder = new MultipartBuilder();
		this.multipartBuilder.type(MultipartBuilder.FORM);
	}
	public void setMultipartOff() {
		this.multipartFlag = false;
		this.multipartBuilder = null;
	}
	
	public void setURL(String url){this.url = url;} //this.builder.url(url);};
	public void addHeader(String name, String value){this.builder.addHeader(name, value);}

	/************************************************************
	 * Request Parameters Addition Methods
	 ************************************************************/
	public void addUriParam(String name, String value){
		if (name == null) name = new String();
		if (value == null) value = new String();
		
		this.uriBuilder.appendQueryParameter(name, value);
	}
	
	public void addJsonParam(String name, String value){
		if (name == null) name = new String();
		if (value == null) value = new String();
		
		if (this.method != HttpRequestLoader.GET )
			this.parametersJson.addProperty(name, value);
	}
	
	public void addJsonObject(BaseRequestJsonModel value){
		if (this.method != HttpRequestLoader.GET ) {
			//if (name == null) name = new String();
			if (value == null) return;
			
			//this.addJSONParam(name, value.serialize());
			Gson gson = value.serialize();
			for (Field field : value.getClass().getFields()) {
				String fieldName = field.getName();
				
				try { 
					String fieldValue = gson.toJson(field.get(value), field.getType());
					JsonParser parser = new JsonParser();
					JsonElement jsonElement = parser.parse(fieldValue);
					
					this.parametersJson.add(fieldName, jsonElement);
					//this.parametersJson.addProperty(fieldName, fieldValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		}
	}
	
	public void addPart(byte[] data, String mediatype, String name, String filename) {
		if (data == null) return;
		if (mediatype == null) new String();
		if (name == null) name = new String();

		if (!this.multipartFlag)
			this.setMultipartOn();
		
		this.multipartBuilder.addFormDataPart(name, filename, 
			RequestBody.create(MediaType.parse(mediatype), data));
	}
	
	public void addRequestParamsBundle(RequestParamsBundle bundle) {
		if (bundle == null) return;
		
		HashMap<String, String> uriParamsMap = bundle.getURIParamsMap();
		if (uriParamsMap != null) {
			Set<Entry<String, String>> uriParamsSet = uriParamsMap.entrySet();
			for (Entry<String, String> entry : uriParamsSet) {
				this.addUriParam(entry.getKey(), entry.getValue());
			}
		}
		
		HashMap<String, String> jsonParamsMap = bundle.getJSONParamsMap();
		if (jsonParamsMap != null) {
			Set<Entry<String, String>> jsonParamsSet = jsonParamsMap.entrySet();
			for (Entry<String, String> entry : jsonParamsSet) {
				this.addJsonParam(entry.getKey(), entry.getValue());
			}
		}
		
		HashMap<String, BaseRequestJsonModel> jsonModelMap = bundle.getJsonModelMap();
		if (jsonModelMap != null) {
			
			Set<Entry<String, BaseRequestJsonModel>> jsonModelSet = jsonModelMap.entrySet();
			for (Entry<String, BaseRequestJsonModel> entry : jsonModelSet) {
				this.addJsonObject(entry.getValue());
			}
		}
		
		List<RawDataModel> rawDataList = bundle.getRawDataList();
		if (rawDataList != null){
			this.setMultipartOn();
			Iterator<RawDataModel> rawDataIterator = rawDataList.iterator();
			while(rawDataIterator.hasNext()) {
				RawDataModel rawData = rawDataIterator.next();
				this.addPart(rawData.data, rawData.mediatype, rawData.name, rawData.filename);
			}
		}
		
	}
	
	/************************************************************
	 * Cookies Methods
	 ************************************************************/
	private void loadCookies() throws MalformedURLException {
		URL uri = new URL(this.url);
		this.host = uri.getHost();
		CookieManager cookieManager = CookieManager.getInstance();
		String cookie = cookieManager.getCookie(host);
		if (cookie != null) {
			this.getBuilder().addHeader(COOKIE, cookie);
		}
	}
	
	private void saveCookies(Response response) {
		if (response == null) return;
		CookieManager cookieManager = CookieManager.getInstance();
		List<String> cookies = response.headers(COOKIES_HEADER);
		
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
		SharedPreferences sessionSharedPreferences = this.context.getSharedPreferences(csrfSharedPreferencesFilename, Context.MODE_PRIVATE);
		String csrf_token = sessionSharedPreferences.getString(this.csrfSharedPreferencesKeyword, null);
		if (csrf_token != null) {
			this.getBuilder().addHeader(CSRF_TOKEN_HEADER_KEYWORD, csrf_token);
		}
	}
	
	private void csrfSave(String responseBody) {
		if (responseBody == null) return;
		
		CSRFJsonModel csrfJsonModel;
		csrfJsonModel = CSRFJsonModel.deserialize(responseBody);
		if (csrfJsonModel != null) {
			SharedPreferences sessionSharedPreferences = this.context.getSharedPreferences(csrfSharedPreferencesFilename, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sessionSharedPreferences.edit();
			editor.putString(this.csrfSharedPreferencesKeyword, csrfJsonModel.csrf_token);
			editor.commit();
		}
	}
	
	/************************************************************
	 * 
	 ************************************************************/
	public BaseResponseJsonModel deserializeJson(String body, Response response) {	
		if (body == null)
			return null;
		
		if (response.isSuccessful()){
			if (this.responseJSONModel != null)
				return this.responseJSONModel.deserialize(body);
			else
				return null;
		} else {
			if (this.serverErrorResponseJSONModel != null)
				return this.serverErrorResponseJSONModel.deserialize(body);
			else
				return null;
		}
	}

	/************************************************************
	 * Metodo para hacer Override
	 ************************************************************/
	@Override
	public ResponseBundle loadInBackground(){
		return sendHTTPRequest();
	}
	
	/************************************************************
	 * Main Method
	 ************************************************************/	
	public ResponseBundle sendHTTPRequest(){
		Response response = null;
		BaseResponseJsonModel jsonModel = null;
		String responseBody = null;
		
		this.builder = new Request.Builder();
		
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
			// Si se utiliza una peticion GET se mandan solamente parametros en el URI
			// En cambio en una peticion POST se mandan ambios tipos de parametros: se 
			// serializan los parametros JSON y se mandan en el cuerpo del paquete; y 
			// se mandan los parametros espcificados en el URI
			if (this.method == HttpRequestLoader.GET) {
				this.builder.url( this.uriBuilder.build().toString());
				this.builder.method(HttpRequestLoader.GET, null);
			}else{
				this.builder.url( this.uriBuilder.build().toString());
				
				Gson gson = new Gson();
				String json = gson.toJson(this.parametersJson);
				this.builder.url(this.url);

				if (this.multipartFlag) {
					this.multipartBuilder.addFormDataPart("default", null, 
							RequestBody.create(JSON, json));
					this.builder.method(this.method, this.multipartBuilder.build());					
				} else {
					this.builder.method(this.method, RequestBody.create(JSON, json));
				}		  
				
			}			
			Request request = this.builder.build();
			response = client.newCall(request).execute();
			responseBody = response.body().string();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		// Parse the Json to an object if is necessary
		try{
			jsonModel = this.deserializeJson(responseBody, response);
		}catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		
		// Save the response's cookies
		try{
			if (this.cookiesFlag && response != null && response.isSuccessful()) this.saveCookies(response);
		}catch(Exception e){
			e.printStackTrace(); 
		}
		
		// Save de CSRF Token
		if (this.csrfFlag && response != null && response.isSuccessful()) this.csrfSave(responseBody);
		
		// Check if the request has been successful
		//if (!response.isSuccessful())
		//	return new ResponseBundle(null, jsonModel); 
		
		return new ResponseBundle(response, jsonModel, responseBody);
	}
	
	
};
