package com.maxkalavera.utils.searchobtainers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.Product;

public class AmazonSearchObtainer {
	String url = "http://www.amazon.com/s/";
	HTTPRequest requestHandler = new HTTPRequest();
		
	public ArrayList<Product> getData(String query, int page){
		String html = makeRequest(query, page);
		ArrayList<Product> data = parseHTML(html);
		return data;
	}
	
	private String makeRequest(String query, int page){
		String data = null;
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("field-keywords", String.valueOf(query)));
		params.add(new BasicNameValuePair("page", String.valueOf(page)));
		
		try{
			int responseStatusCode = this.requestHandler.sendGetRequest(this.url, params, null);
			
			if (responseStatusCode == HttpStatus.SC_OK )
				data = this.requestHandler.getDataOfGetRequest();
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		
		/*
		// En el codigo siguiente se manda un Get Request utilizando la libreria de Apache
		try{
			HttpResponse response = this.requestHandler.sendGetRequestApache(this.url, params, null);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
				data = EntityUtils.toString(response.getEntity());
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("EcoAR-ERROR", "Exception: "+Log.getStackTraceString(e));
		}
		*/
		
		return data;
	}
	
	private ArrayList<Product> parseHTML(String html){
		ArrayList<Product> data = new ArrayList<Product>();
		Document doc = Jsoup.parse(html);
		Elements products = doc.select("div[class=rslt prod celwidget]");
		for (Element product : products) {
			Product pdata = new Product();
			Element productNameElement = product.select("span[class=lrg bold]").first();
			pdata.productName = productNameElement.text();
			
			Element productImageURL = product.select("img").first();
			pdata.productImageURL = productImageURL.attr("src");
			
			pdata.image = this.requestHandler.downloadImage(pdata.productImageURL);
			
			data.add(pdata);
		}
		return data;
	}
	
}
