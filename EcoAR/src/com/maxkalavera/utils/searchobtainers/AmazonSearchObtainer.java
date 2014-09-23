package com.maxkalavera.utils.searchobtainers;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.maxkalavera.utils.HTTPRequest;

public class AmazonSearchObtainer {
	String url = "http://www.amazon.com/s/";
		
	public ArrayList<String[]> getData(String query){
		String html = makeRequest(query);
		ArrayList<String[]> data = parseHTML(html);
		return data;
	}
	
	private String makeRequest(String query){
		HTTPRequest requestHandler = new HTTPRequest();
		try{
			ArrayList<String[]> params = new ArrayList<String[]>();
			params.add(new String[]{"field-keywords", query});
			String result = requestHandler.sendGetRequest(this.url, params);
			return result;
		} catch(IOException ie) {
			ie.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<String[]> parseHTML(String html){
		ArrayList<String[]> data = new ArrayList<String[]>();
		Log.i("ecoar-html", html);
		Document doc = Jsoup.parse(html);
		Elements products = doc.select("div[class=rslt prod celwidget]");
		for (Element product : products) {
			//doc.select("span[class=lrg bold]");
			Element productNameElement = product.select("span[class=lrg bold]").first();
			String productName = productNameElement.text();
			//Log.i("ecoar", productName);
			
			Element productImageURL = product.select("img").first();
			String imgURL = productImageURL.attr("src");
			//Log.i("ecoar", imgURL);
			
			data.add(new String[] {productName, imgURL});
		}
		return data;
	}
	
}
