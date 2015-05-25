package com.maxkalavera.ecoar.productinfo;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class GetProductInfoHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	
	public GetProductInfoHTTPLoader(Context context, 
			RequestParamsBundle requestBundle,
			ProductModel product) {
		super(context, 
				context.getResources().getString(R.string.webservice_login),// ERROR MODIFICAR
				GET,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new ProductInfoModel());
		this.product = product;
	}
	
	@Override
	public ResponseBundle loadInBackground(){
		ProductCacheDAO productCache = new ProductCacheDAO(this.getContext());
		this.product = productCache.searchProductInCache(this.product);
		
		if (this.product.getCacheID() == -1 ) {
			productCache.addProduct(this.product);
			return send();
		}
		
		ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
		ProductInfoModel productInfo = productInfoCache.getProductInfoFromCache(this.product);
		
		if (productInfo != null) {
			return new ResponseBundle(null, productInfo);
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