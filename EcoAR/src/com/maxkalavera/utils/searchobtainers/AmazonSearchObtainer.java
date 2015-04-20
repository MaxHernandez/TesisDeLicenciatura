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
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.models.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class AmazonSearchObtainer {
	String url;
	
	public AmazonSearchObtainer(Context context) {
		this.url = context.getResources().getString(R.string.amazonurl);
	}
	
	public ArrayList<ProductModel> getData(String query, int page){
		String html = makeRequest(query, page);
		ArrayList<ProductModel> data = parseHTML(html);
		return data;
	}
	
	private String makeRequest(String query, int page){
		OkHttpClient client = new OkHttpClient();
		Request.Builder builder = new Request.Builder();
		Uri.Builder uriBuilder = Uri.parse(this.url).buildUpon();
		
		uriBuilder.appendQueryParameter("field-keywords", String.valueOf(query));
		uriBuilder.appendQueryParameter("page", String.valueOf(page));
		
		builder.url( uriBuilder.build().toString());
		builder.method(HttpRequestLoader.GET, null);
		try {	
			Request request = builder.build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
				pdata.name = productNameElement.text();
			
				Element productImageURL = product.select("img").first();
				pdata.imageURL = productImageURL.attr("src");
			
				pdata.image = ImageDownloader.downloadImage(pdata.imageURL);
			
				data.add(pdata);
			}
			Log.i("EcoAR-Search-Data", pdata.name);
			Log.i("EcoAR-Search-Data", pdata.imageURL);
		}
		return data;
	}
	
}
