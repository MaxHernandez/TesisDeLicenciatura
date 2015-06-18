package com.maxkalavera.ecoar.searchcamera;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.SearchCameraResponseJsonModel;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ImageDownloader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.content.AsyncTaskLoader;

public class SendImageHTTPLoader extends HttpRequestLoader {

	public SendImageHTTPLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_searchcam_post),
				POST,
				requestBundle);
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new SearchCameraResponseJsonModel());
	}

	public ResponseBundle loadInBackground(){
		ResponseBundle responseBundle = sendHTTPRequest();
		
		if (responseBundle.getResponseJsonObject() != null) {
			BrandModel brand = ( (SearchCameraResponseJsonModel) responseBundle.getResponseJsonObject()).brand;
			if (brand != null) {
				brand.image = ImageDownloader.downloadImage(brand.image_url);
			}
		}
		
		return responseBundle;
	}
	
}
