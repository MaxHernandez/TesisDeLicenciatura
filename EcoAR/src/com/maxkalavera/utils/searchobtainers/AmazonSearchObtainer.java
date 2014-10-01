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
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("field-keywords", String.valueOf(query)));
		params.add(new BasicNameValuePair("page", String.valueOf(page)));
		
		String data = this.requestHandler.sendGetRequest(this.url, params, null, HttpStatus.SC_OK);
		
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
