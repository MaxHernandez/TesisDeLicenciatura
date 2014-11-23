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

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.HTTPRequest;
import com.maxkalavera.utils.datamodels.ProductModel;

public class AmazonSearchObtainer {
	String url;
	HTTPRequest requestHandler;
	
	public AmazonSearchObtainer(Context context) {
		this.requestHandler = new HTTPRequest(context);
		this.url = context.getResources().getString(R.string.amazonurl);
	}
	
	public ArrayList<ProductModel> getData(String query, int page){
		String html = makeRequest(query, page);
		ArrayList<ProductModel> data = parseHTML(html);
		return data;
	}
	
	private String makeRequest(String query, int page){
		Log.i("EcoAR-Search-URL", url);
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("field-keywords", String.valueOf(query)));
		params.add(new BasicNameValuePair("page", String.valueOf(page)));
		
		String data = this.requestHandler.sendGetRequest(this.url, params, null, HttpStatus.SC_OK);
		Log.i("EcoAR-Search-Data", data);
		return data;
	}
	
	private ArrayList<ProductModel> parseHTML(String html){
		ArrayList<ProductModel> data = new ArrayList<ProductModel>();
		Document doc = Jsoup.parse(html);
		Elements products = doc.select("div[class=s-item-container]");
		for (Element product : products) {
			ProductModel pdata = new ProductModel();
			
			Element productNameElementContainer = product.select("a[class=a-link-normal s-access-detail-page a-text-normal]").first();
			if (productNameElementContainer != null) {
				Element productNameElement = productNameElementContainer .select("h2").first();
				pdata.productName = productNameElement.text();
			
				Element productImageURL = product.select("img").first();
				pdata.productImageURL = productImageURL.attr("src");
			
				pdata.image = this.requestHandler.downloadImage(pdata.productImageURL);
			
				data.add(pdata);
			}
			Log.i("EcoAR-Search-Data", pdata.productName);
			Log.i("EcoAR-Search-Data", pdata.productImageURL);
		}
		return data;
	}
	
}
