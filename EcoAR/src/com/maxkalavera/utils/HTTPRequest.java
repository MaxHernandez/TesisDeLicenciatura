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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.webkit.CookieManager;
 
public class HTTPRequest {
	HttpClient httpclient;
	HttpURLConnection getConnection;
	HttpURLConnection postConnection;
	String host = "";
	
	public HTTPRequest(){
		httpclient = new DefaultHttpClient();
	}

	public Integer sendGetRequest(String url, List<BasicNameValuePair> params, List<BasicNameValuePair> headers) throws IOException {
		if(params != null)
			url += "?"+URLEncodedUtils.format(params, "utf-8");

		URL uri = new URL(url);
		this.host = uri.getHost();
		this.getConnection = (HttpURLConnection) uri.openConnection();
		getConnection.setRequestMethod("GET");

		CookieManager cookieManager = CookieManager.getInstance();
		String cookie = cookieManager.getCookie(this.host);
	    if (cookie != null) {
	    	this.getConnection.setRequestProperty("Cookie", cookie);
	    }
		
		if (headers != null)
			for (int i = 0; i < headers.size(); i++)
				getConnection.setRequestProperty( headers.get(i).getName(), headers.get(i).getValue());
		
		return this.getConnection.getResponseCode();
	}
	
	public String getDataOfGetRequest() throws IOException {
		CookieManager cookieManager = CookieManager.getInstance();
	    List<String> cookieList = this.getConnection.getHeaderFields().get("Set-Cookie");
	    if (cookieList != null) {
	        for (String cookieTemp : cookieList) {
	            cookieManager.setCookie(this.host, cookieTemp);
	        }
	    }
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(this.getConnection.getInputStream()));
		String temp;
		StringBuffer responseData = new StringBuffer();
		
		while ((temp = in.readLine()) != null)
			responseData.append(temp);
		in.close();
		return responseData.toString();
	}
	
	
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
	
	public Integer sendPostRequest(String url, List<BasicNameValuePair> params, List<BasicNameValuePair> headers) throws IOException {
		
		String paramsURL = "";
		if(params != null)
			paramsURL += URLEncodedUtils.format(params, "utf-8");
		
		URL uri = new URL(url);
		this.host = uri.getHost();
		this.postConnection = (HttpURLConnection) uri.openConnection(); 
		this.postConnection.setRequestMethod("POST");
		
		CookieManager cookieManager = CookieManager.getInstance();
		String cookie = cookieManager.getCookie(this.host);
	    if (cookie != null) {
	    	this.postConnection.setRequestProperty("Cookie", cookie);
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
	
	public String getDataOfPostRequest() throws IOException {
		
		CookieManager cookieManager = CookieManager.getInstance();
	    List<String> cookieList = this.postConnection.getHeaderFields().get("Set-Cookie");
	    if (cookieList != null) {
	        for (String cookieTemp : cookieList) {
	            cookieManager.setCookie(this.host, cookieTemp);
	        }
	    }
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(this.postConnection.getInputStream()));
		String temp;
		StringBuffer responseData = new StringBuffer();
		
		while ((temp = in.readLine()) != null)
			responseData.append(temp);
		in.close();
		return responseData.toString();
	}
	
	
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
	
}
