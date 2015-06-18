package com.maxkalavera.ecoar.productinfo;

import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.jsonmodels.CommentariesListJsonModel;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class CommentariesListGetCommentListHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	int page;
	
	private static final int PAGE_SIZE = 6;
	
	public CommentariesListGetCommentListHTTPLoader(Context context, RequestParamsBundle requestBundle, ProductModel product, int page) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_product_commentaries_get),
				GET,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new CommentariesListJsonModel());
		this.product = product; 
		this.page = page;
	}
	
	@Override
	public ResponseBundle loadInBackground(){
		ProductCacheDAO productCache = new ProductCacheDAO(this.getContext());
		productCache.open();
		this.product = productCache.searchProductInCache(this.product);
		
		if (this.product.getCacheId() == -1 ) {
			productCache.addProduct(this.product);
			//return send();
		}
		productCache.close();
		
		return sendHTTPRequest();
		
		/*
		CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
		commentariesCache.open();
		List<CommentModel> commentaries = commentariesCache.getCommentariesFromCache(this.product, this.page);
		commentariesCache.close();
		
		if (commentaries != null) {
			if ( commentaries.size() > 0 && commentaries.size() < PAGE_SIZE) {
				return send(commentaries.size());
			} else {
				CommentariesListModel commentariesListModel = new CommentariesListModel();
				commentariesListModel.commentaries = commentaries;
				return new ResponseBundle(null, commentariesListModel);
			}
		} else {
			return send();
		}
		*/
	}
	
	/*
	public ResponseBundle send(int offset) {
		CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
		commentariesCache.open();
		ResponseBundle response = sendHTTPRequest();
		
		if (response.getResponse() != null && response.getResponse().isSuccessful() && response.getResponseJsonObject() != null) {
			CommentariesListModel commentariesListModel  = (CommentariesListModel) response.getResponseJsonObject();
			
			if (offset > 0 && offset < PAGE_SIZE)
				commentariesListModel.commentaries = 
						commentariesListModel.commentaries.
							subList(offset, commentariesListModel.commentaries.size());

			Iterator<CommentModel> it = commentariesListModel.commentaries.iterator();
			while (it.hasNext()) {
				CommentModel comment = it.next();
				commentariesCache.addComment(comment, this.product);
			}
			
		}
		commentariesCache.close();
		return response;
	}
	
	public ResponseBundle send() {
		return send(0);
	}
	*/
	
};