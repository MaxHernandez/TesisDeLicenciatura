package com.maxkalavera.ecoar.productinfo;

import java.util.List;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorJsonModel;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.jsonmodels.CommentariesListModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class GetCommentListHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	int page;
	
	public GetCommentListHTTPLoader(Context context, RequestParamsBundle requestBundle, ProductModel product, int page) {
		super(context, 
				context.getResources().getString(R.string.webservice_login),// ERROR MODIFICAR
				GET,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new CommentariesListModel());
		this.product = product; 
		this.page = page;
	}
	
	@Override
	public ResponseBundle loadInBackground(){
		ProductCacheDAO productCache = new ProductCacheDAO(this.getContext());
		this.product = productCache.searchProductInCache(this.product);
		
		if (this.product.getCacheID() == -1 ) {
			productCache.addProduct(this.product);
			return send();
		}
		
		CommentariesCacheDAO commentariesCache = new CommentariesCacheDAO(this.getContext());
		List<CommentModel> commentaries = commentariesCache.getCommentariesFromCache(this.product, this.page);
		
		if (commentaries != null) {
			CommentariesListModel commentariesListModel = new CommentariesListModel();
			commentariesListModel.commentaries = commentaries;
			return new ResponseBundle(null, commentariesListModel);
		} else {
			return send();
		}
	}
	
	public ResponseBundle send() {
		ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
		ResponseBundle response = sendHTTPRequest();
		if (response.getResponseJsonObject() != null) {
			ProductInfoModel productInfo = (ProductInfoModel) response.getResponseJsonObject();
			productInfo.setProductReference(this.product);
			productInfoCache.addProductInfo(productInfo);
		 }
		 return response;
	}
};