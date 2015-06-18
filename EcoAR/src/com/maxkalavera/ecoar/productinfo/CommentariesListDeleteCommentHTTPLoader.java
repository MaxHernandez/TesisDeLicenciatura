package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class CommentariesListDeleteCommentHTTPLoader extends HttpRequestLoader  {
	
	CommentModel comment;
	
	public CommentariesListDeleteCommentHTTPLoader(Context context, RequestParamsBundle requestBundle, CommentModel comment) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_product_commentaries_delete),
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.comment = comment;
	}
	
	
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();		
		return new ResponseBundle(response.getResponse(), this.comment);
	}
	
	/*
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();		
		if (response.getResponse() != null && response.getResponse().isSuccessful()) {
			CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
			commentariesCache.open();
			commentariesCache.removeComment(comment);
			commentariesCache.close();
			return new ResponseBundle(response.getResponse(), comment);
		}
		
		return response;
	}
	*/
	
};