package com.maxkalavera.utils.searchobtainers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.BitmapFactory;
import android.util.Log;

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
		try{
			ArrayList<String[]> params = new ArrayList<String[]>();
			params.add(new String[]{"field-keywords", query});
			params.add(new String[]{"page", String.valueOf(page)});
			String result = this.requestHandler.sendGetRequest(this.url, params);
			return result;
		} catch(IOException ie) {
			ie.printStackTrace();
		}
		return null;
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
