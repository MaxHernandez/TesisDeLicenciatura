package com.maxkalavera.utils;                                                                                                    
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.maxkalavera.utils.jsonmodels.CSRFJsonModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


/**
 * @author max
 *
 */
public class HTTPRequest {
	HttpClient httpclient;
	HttpURLConnection getConnection;
	HttpURLConnection postConnection;
	String host = "";
	String prefsSession = "Session_prefs";
	
	public HTTPRequest(){
		this.httpclient = new DefaultHttpClient();
	}

	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptCookiesFlag
	 * @return
	 * @throws IOException
	 */
	public Integer connectGetRequest(String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers,
			boolean acceptCookiesFlag) throws IOException {
		if(params != null)
			url += "?"+URLEncodedUtils.format(params, "utf-8");

		URL uri = new URL(url);
		this.host = uri.getHost();
		this.getConnection = (HttpURLConnection) uri.openConnection();
		getConnection.setRequestMethod("GET");

		if(acceptCookiesFlag){
			CookieManager cookieManager = CookieManager.getInstance();
			String cookie = cookieManager.getCookie(this.host);
			if (cookie != null) {
				this.getConnection.setRequestProperty("Cookie", cookie);
			}
		}
		
		if (headers != null)
			for (int i = 0; i < headers.size(); i++)
				getConnection.setRequestProperty( headers.get(i).getName(), headers.get(i).getValue());
		
		return this.getConnection.getResponseCode();
	}
	
	/**
	 * @param acceptCookiesFlag
	 * @return
	 * @throws IOException
	 */
	public String getDataOfGetRequest(boolean acceptCookiesFlag) throws IOException {
		if(acceptCookiesFlag){
			CookieManager cookieManager = CookieManager.getInstance();
			List<String> cookieList = this.getConnection.getHeaderFields().get("Set-Cookie");
			if (cookieList != null) {
				for (String cookieTemp : cookieList) {
					cookieManager.setCookie(this.host, cookieTemp);
				}
			}
		}
		
		BufferedReader in;
		try {
			in = new BufferedReader(
					new InputStreamReader(this.getConnection.getInputStream()));
		} catch(Exception e) {
			in = new BufferedReader(
					new InputStreamReader(this.getConnection.getErrorStream ()));
		}

		String temp;
		StringBuffer responseData = new StringBuffer();
		
		while ((temp = in.readLine()) != null)
			responseData.append(temp);
		in.close();
		return responseData.toString();
	}
	
	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptanceStatusCode
	 * @return
	 */
	public String sendGetRequest(String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers, 
			int acceptanceStatusCode) {
		String data = null;	
		try{
			int responseStatusCode = connectGetRequest(url, params, headers, false);
			if (responseStatusCode == acceptanceStatusCode )
				data = this.getDataOfGetRequest(false);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		return data;
	}
	
	/**
	 * @param context
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptanceStatusCode
	 * @return
	 */
	public Pair<String, Integer> sendSessionGetRequest(Context context, 
			String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers, 
			List<Integer> acceptanceStatusCodes) {
		Log.d("EcoAR-DEBG", "CERO");
		SharedPreferences sessionSharedPreferences = context.getSharedPreferences(prefsSession, Context.MODE_PRIVATE);
		Log.d("EcoAR-DEBG", "UNO");
		String csrf_token = sessionSharedPreferences.getString("csrf_token", null);
		Log.d("EcoAR-DEBG", "UNO y un CUARTO");
		if (csrf_token != null) {
			if ( params == null)
				params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("csrf_token", csrf_token));
		}
		Log.d("EcoAR-DEBG", "UNO y MEDIO");
		try{
			String data = null;
			int responseStatusCode = connectGetRequest(url, params, headers, true);	
			Log.d("EcoAR-DEBG", "DOS");
			if (acceptanceStatusCodes != null) {
				for (Integer acceptanceStatusCode  : acceptanceStatusCodes) {
					if (acceptanceStatusCode.equals(responseStatusCode) ){
						data = this.getDataOfGetRequest(true);
						Log.d("EcoAR-DEBG", "TRES");
						//CookieSyncManager.getInstance().sync();
				
						CSRFJsonModel csrfJsonModel = CSRFJsonModel.create(data);
						if (csrfJsonModel != null) {
							SharedPreferences.Editor editor = sessionSharedPreferences.edit();
							editor.putString("csrf_token", csrfJsonModel.csrf_token);
							editor.commit();
						}
						
						return new Pair<String, Integer>(data, acceptanceStatusCode);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		return null;
	}
	
	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public HttpResponse sendGetRequestApache(String url, List<BasicNameValuePair> params, List<BasicNameValuePair> headers) throws IOException {
		if(params != null)
			url += "?"+URLEncodedUtils.format(params, "utf-8");
		
		HttpGet httpget = new HttpGet(url);

		if (headers != null)
			for (int i = 0; i < headers.size(); i++)
				httpget.setHeader( headers.get(i).getName(), headers.get(i).getValue());

	    HttpResponse response = this.httpclient.execute(httpget);
	    return response;
	}
	
	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptCookiesFlag
	 * @return
	 * @throws IOException
	 */
	public Integer connectPostRequest(String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers,
			boolean acceptCookiesFlag) throws IOException {
		
		String paramsURL = "";
		if(params != null)
			paramsURL += URLEncodedUtils.format(params, "utf-8");
		
		URL uri = new URL(url);
		this.host = uri.getHost();
		this.postConnection = (HttpURLConnection) uri.openConnection(); 
		this.postConnection.setRequestMethod("POST");
		
		if (acceptCookiesFlag) {
			CookieManager cookieManager = CookieManager.getInstance();
			String cookie = cookieManager.getCookie(this.host);
			if (cookie != null) {
				this.postConnection.setRequestProperty("Cookie", cookie);
			}
		}

		if (headers != null)
			for (int i = 0; i < headers.size(); i++)
				this.postConnection.setRequestProperty( headers.get(i).getName(), headers.get(i).getValue());

		this.postConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(this.postConnection.getOutputStream());
		wr.writeBytes(paramsURL);
		wr.flush();
		wr.close();
		
		return this.postConnection.getResponseCode();
	}
	
	/**
	 * @param acceptCookiesFlag
	 * @return
	 * @throws IOException
	 */
	
	public String getDataOfPostRequest(boolean acceptCookiesFlag) throws IOException {
		if (acceptCookiesFlag){
			CookieManager cookieManager = CookieManager.getInstance();
			List<String> cookieList = this.postConnection.getHeaderFields().get("Set-Cookie");
			boolean sessionCookieFlag = false;
			if (cookieList != null) {
				for (String cookieTemp : cookieList) {
					cookieManager.setCookie(this.host, cookieTemp);
					if (cookieTemp.split("=")[0].equals("sessionid") )
						sessionCookieFlag = true;
				}
				
				if (sessionCookieFlag) {
					CookieSyncManager.getInstance().sync();
				}
			}
		}
		BufferedReader in;
		try {
			in = new BufferedReader(
					new InputStreamReader(this.postConnection.getInputStream()));
		} catch(Exception e) {
			in = new BufferedReader(
					new InputStreamReader(this.postConnection.getErrorStream ()));
		}
		
		String temp;
		StringBuffer responseData = new StringBuffer();
		
		while ((temp = in.readLine()) != null)
			responseData.append(temp);
		in.close();
		return responseData.toString();
	}
	
	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptanceStatusCode
	 * @return
	 */
	public String sendPostRequest(String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers, 
			int acceptanceStatusCode) {
		String data = null;	
		try{
			int responseStatusCode = this.connectPostRequest(url, params, headers, false);
			if (responseStatusCode == acceptanceStatusCode )
				data = this.getDataOfPostRequest(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * @param context
	 * @param url
	 * @param params
	 * @param headers
	 * @param acceptanceStatusCodes
	 * @return
	 */
	public Pair<String, Integer> sendSessionPostRequest(Context context,
			String url, 
			List<BasicNameValuePair> params, 
			List<BasicNameValuePair> headers, 
			List<Integer> acceptanceStatusCodes) {
		String data = null;
	
		SharedPreferences sessionSharedPreferences = context.getSharedPreferences(prefsSession, Context.MODE_PRIVATE);
		String csrf_token = sessionSharedPreferences.getString("csrf_token", null);
		if (csrf_token != null) {
			if ( params == null)
				params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("csrf_token", csrf_token));
		}
		
		try{
			int responseStatusCode = this.connectPostRequest(url, params, headers, true);
			
			if (acceptanceStatusCodes != null) {
				for (Integer acceptanceStatusCode  : acceptanceStatusCodes) {
					if (acceptanceStatusCode.equals(responseStatusCode) ){
						data = this.getDataOfPostRequest(true);

						CSRFJsonModel csrfJsonModel = CSRFJsonModel.create(data);
						if (csrfJsonModel != null) {
							SharedPreferences.Editor editor = sessionSharedPreferences.edit();
							editor.putString("csrf_token", csrfJsonModel.csrf_token);
							editor.commit();
						}
					
						return new Pair<String, Integer>(data, acceptanceStatusCode);
					}	
				}
			}
		
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
	
		return null;
	}
	
	
	/**
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public HttpResponse sendPostRequestApache(String url, List<BasicNameValuePair> params, List<BasicNameValuePair> headers) throws IOException {
		HttpPost httpPost = new HttpPost(url);

		if (params != null)
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		
		if (headers != null)
			for (int i = 0; i < headers.size(); i++)
				httpPost.setHeader( headers.get(i).getName(), headers.get(i).getValue());

	    HttpResponse response = this.httpclient.execute(httpPost);
	    return response;
	}
	
	/**
	 * @param imageURL
	 * @return
	 */
	public Bitmap downloadImage(String imageURL) {
	    try {
	    	InputStream in = new java.net.URL(imageURL).openStream();
	    	 return BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	    	Log.e("Error", e.getMessage());
	    	e.printStackTrace();
	    }
	    return null;
	}
	
};
