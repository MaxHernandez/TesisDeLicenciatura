package com.maxkalavera.ecoar.brandinfo;

import android.content.Context;
import android.util.Log;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.database.ProductInfoCacheDAO;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.ExtraInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class BrandInfoGetInfoHTTPLoader extends HttpRequestLoader  {
	BrandModel brand;
	
	public BrandInfoGetInfoHTTPLoader(Context context, 
			RequestParamsBundle requestBundle,
			BrandModel brand) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_brand_info_get),
				GET,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new ExtraInfoModel());
		
		this.brand = brand;
	}
	
	/*
	@Override
	public ResponseBundle loadInBackground(){
		ProductCacheDAO productCache = new ProductCacheDAO(this.getContext());
		productCache.open();
		
		this.brand = productCache.searchProductInCache(this.brand);
		
		if (this.brand.getCacheId() == -1 ) {
			
			//if (this.brand.image == null) 
			//	product.image = ImageDownloader.downloadImage(product.imageURL);
			
			productCache.addProduct(this.brand);
			return send();
		}
		productCache.close();
		
		ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
		productInfoCache.open();
		ExtraInfoModel productInfo = productInfoCache.getProductInfoFromCache(this.brand);
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
			ExtraInfoModel productInfo = (ExtraInfoModel) response.getResponseJsonObject();
			
			ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
			productInfoCache.open();
			productInfoCache.addProductInfo(productInfo, this.product);
			productInfoCache.close();
		 }

		 return response;
	}
	*/
};