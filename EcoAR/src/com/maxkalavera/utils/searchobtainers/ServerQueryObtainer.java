package com.maxkalavera.utils.searchobtainers;

import java.util.ArrayList;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.SearchCameraResponseJsonModel;
import com.maxkalavera.utils.database.jsonmodels.ServerProductListJsonModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class ServerQueryObtainer {

	public static final String NAME = "SERVER_QUERY_OBTAINER";
	
	Context context;
	String url;
	
	public ServerQueryObtainer(Context context) {
		this.context = context;
		this.url = context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_search_query);
	}
	
	private Context getContext() {
		return this.context;
	}
	
	public ArrayList<ProductModel> getData(String query, int page){
		RequestParamsBundle requestBundle = new RequestParamsBundle();
		requestBundle.addUriParam("query", query);
		requestBundle.addUriParam("page", String.valueOf(page));
		
		HttpRequestLoader httpRequest = 
				new HttpRequestLoader(getContext(),
						this.url,
						HttpRequestLoader.GET,
						requestBundle);
		httpRequest.setCookiesOn();
		httpRequest.setCSRFOn();
		httpRequest.setJsonResponseOn(new ServerProductListJsonModel());
		
		ResponseBundle responseBundle =  httpRequest.sendHTTPRequest();
		if (responseBundle.getResponse() != null &&
				responseBundle.getResponse().isSuccessful() &&
				responseBundle.getResponseJsonObject() != null) {
			ServerProductListJsonModel serverProductList = 
					(ServerProductListJsonModel) responseBundle.getResponseJsonObject();

			return new ArrayList<ProductModel>(serverProductList.products);
		} else {
			//error al mandar la peticion o al deserializarla
		}
			
		return null;
		
	}
	
}
