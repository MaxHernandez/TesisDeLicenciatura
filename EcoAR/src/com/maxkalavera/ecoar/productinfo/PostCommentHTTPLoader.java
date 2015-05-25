package com.maxkalavera.ecoar.productinfo;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class PostCommentHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	
	public PostCommentHTTPLoader(Context context, RequestParamsBundle requestBundle, ProductModel product) {
		super(context, 
				context.getResources().getString(R.string.webservice_login),// ERROR MODIFICAR
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new CommentModel());
		this.product = product;
	}
	
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();
		CommentModel comment = (CommentModel) response.getResponseJsonObject();
		
		if (comment != null) {
			CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
			comment.setProductReference(this.product);
			commentariesCache.addComment(comment);
		}
		
		return response;
	}
	
};