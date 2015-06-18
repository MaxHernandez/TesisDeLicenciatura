package com.maxkalavera.ecoar.brandinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class CommentariesListPostCommentHTTPLoader extends HttpRequestLoader {
	BrandModel brand;
	
	public CommentariesListPostCommentHTTPLoader(Context context, RequestParamsBundle requestBundle, BrandModel brand) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_brand_commentaries_post),
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new CommentModel());
		this.brand = brand;
	}
	
	/*
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();
		
		if ( response.getResponse() != null && response.getResponse().isSuccessful() && response.getResponseJsonObject() != null ) { 
			CommentModel comment = (CommentModel) response.getResponseJsonObject();

			CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
			commentariesCache.open();
			commentariesCache.addComment(comment, this.product);
			commentariesCache.close();
		}
		
		return response;
	}
	*/
	
};