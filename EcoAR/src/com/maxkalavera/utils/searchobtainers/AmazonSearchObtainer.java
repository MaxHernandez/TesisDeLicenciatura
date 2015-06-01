package com.maxkalavera.utils.searchobtainers;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.net.Uri;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class AmazonSearchObtainer {
	String url;
	
	private static final String SHOPING_SERVICE = "AMAZON";
	private static final String SHOPING_SERVICE_INDEX = "AMZN";
	private static final int GENERAL_ID_NUMBER_CHARS = 16;
	
	public AmazonSearchObtainer(Context context) {
		this.url = context.getResources().getString(R.string.amazonurl);
	}
	
	public String buildGeneralId(String strId) {
		String output = "";
		if (strId.length() > AmazonSearchObtainer.GENERAL_ID_NUMBER_CHARS) {
			return null;
		}
		
		for (int i=0; i < (AmazonSearchObtainer.GENERAL_ID_NUMBER_CHARS - strId.length()) ;i++){
			output = output + "0";
		}
		output = AmazonSearchObtainer.SHOPING_SERVICE_INDEX + output + strId;
		
		return output;
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
			pdata.shopingService = AmazonSearchObtainer.SHOPING_SERVICE;
			
			Element productNameElementContainer = product.select("a[class=a-link-normal s-access-detail-page a-text-normal]").first();
			if (productNameElementContainer != null) {
				Element aElement = productNameElementContainer.select("a[title]").first();
				pdata.name = aElement.text();
				pdata.url = aElement.attr("href");
				
				String[] urlParts = pdata.url.split("/");
				String shopingServiceId = urlParts[urlParts.length-1];
				pdata.generalId = this.buildGeneralId(shopingServiceId);
				
				Element spanBrother = productNameElementContainer.select("span:contains(Product Features)").first();
				Element spanParent = spanBrother.parent();
				pdata.description = spanParent.children().last().text();
				
				Element productImageURL = product.select("img").first();
				pdata.imageURL = productImageURL.attr("src");
				pdata.image = ImageDownloader.downloadImage(pdata.imageURL);
				
				data.add(pdata);
			}
		}
		return data;
	}
	
};
