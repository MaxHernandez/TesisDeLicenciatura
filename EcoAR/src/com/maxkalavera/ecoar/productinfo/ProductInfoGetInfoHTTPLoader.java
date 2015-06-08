package com.maxkalavera.ecoar.productinfo;

import android.content.Context;
import android.util.Log;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class ProductInfoGetInfoHTTPLoader extends HttpRequestLoader  {
	ProductModel product;
	
	public ProductInfoGetInfoHTTPLoader(Context context, 
			RequestParamsBundle requestBundle,
			ProductModel product) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_product_info_get),
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
		productCache.open();
		
		this.product = productCache.searchProductInCache(this.product);
		
		if (this.product.getCacheId() == -1 ) {
			
			if (this.product.image == null) 
				product.image = ImageDownloader.downloadImage(product.imageURL);
			
			productCache.addProduct(this.product);
			return send();
		}
		productCache.close();
		
		ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
		productInfoCache.open();
		ProductInfoModel productInfo = productInfoCache.getProductInfoFromCache(this.product);
		productInfoCache.close();
		
		if (productInfo != null) {
			return new ResponseBundle(null, productInfo);
		} else {
			return send();
		}
	}
	
	public ResponseBundle send() {		
		ResponseBundle response = sendHTTPRequest();
		
		if ( response.getResponse() != null && response.getResponse().isSuccessful()  && response.getResponseJsonObject() != null) {
			ProductInfoModel productInfo = (ProductInfoModel) response.getResponseJsonObject();
			
			ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
			productInfoCache.open();
			productInfoCache.addProductInfo(productInfo, this.product);
			productInfoCache.close();
		 }

		 return response;
	}
	
};