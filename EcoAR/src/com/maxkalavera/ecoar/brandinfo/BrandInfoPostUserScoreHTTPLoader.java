package com.maxkalavera.ecoar.brandinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class BrandInfoPostUserScoreHTTPLoader extends HttpRequestLoader  {
	BrandModel brand;
	
	public BrandInfoPostUserScoreHTTPLoader(Context context, RequestParamsBundle requestBundle, BrandModel brand) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_brand_info_post),
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new UsersScoreModel());
		this.brand = brand;
	}
	
	/*
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();
		UsersScoreModel usersScore = (UsersScoreModel) response.getResponseJsonObject();
		
		if (usersScore != null) {
			
			ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
			productInfoCache.open();
			productInfoCache.updateScoreOnProduct(usersScore, this.product);
			productInfoCache.close();
		}
		
		return response;
	}
	*/
	
};