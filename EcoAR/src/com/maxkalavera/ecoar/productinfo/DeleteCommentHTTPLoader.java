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

public class DeleteCommentHTTPLoader extends HttpRequestLoader  {
	
	CommentModel comment;
	
	public DeleteCommentHTTPLoader(Context context, RequestParamsBundle requestBundle, CommentModel comment) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_product_commentaries_delete),
				DELETE,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.comment = comment;
	}
	
	public ResponseBundle loadInBackground(){
		ResponseBundle response = sendHTTPRequest();		
		if (response.getResponse().isSuccessful()) {
			CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
			commentariesCache.removeComment(comment);
			return new ResponseBundle(response.getResponse(), comment);
		}
		
		return response;
	}
	
};