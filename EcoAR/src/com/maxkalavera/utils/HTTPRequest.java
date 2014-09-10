package com.maxkalavera.utils;                                                                                                    
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;
import android.util.Log;
 

public class HTTPRequest extends AsyncTask<String, String,String> {
	HttpClient httpclient;
	
	public HTTPRequest(){
		httpclient = new DefaultHttpClient();
	}
	
	public String sendGetRequest(String url) throws IOException {
	    HttpResponse response = this.httpclient.execute(new HttpGet(url));
	    StatusLine statusLine = response.getStatusLine();
	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        response.getEntity().writeTo(out);
	        out.close();
	        String responseString = out.toString();
	        Log.i("EcoAR", responseString);
	        return responseString;
	    } else{
	        response.getEntity().getContent().close();
	        throw new IOException(statusLine.getReasonPhrase());
	    }
	}

	@Override
	protected String doInBackground(String... url) {
		try{
			return this.sendGetRequest(url[0]);
		} catch(IOException ie) {
			ie.printStackTrace();
		}
		return null;
	}
	
	
}

