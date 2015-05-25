package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class PostUserScoreHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	
	public PostUserScoreHTTPLoader(Context context, RequestParamsBundle requestBundle, ProductModel product) {
		super(context, 
				context.getResources().getString(R.string.webservice_login),// ERROR MODIFICAR
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new UsersScoreModel());
		this.product = product;
	}
	
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();
		UsersScoreModel usersScore = (UsersScoreModel) response.getResponseJsonObject();
		
		if (usersScore != null) {
			ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
			productInfoCache.updateScoreOnProduct(this.product, usersScore);
		}
		
		return response;
	}
	
};