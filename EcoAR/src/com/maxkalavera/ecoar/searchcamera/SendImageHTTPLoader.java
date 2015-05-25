package com.maxkalavera.ecoar.searchcamera;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.SearchCameraResponseJsonModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.content.AsyncTaskLoader;

public class SendImageHTTPLoader extends HttpRequestLoader {

	public SendImageHTTPLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				// ERROR: Modificar el resource correcto
				context.getResources().getString(R.string.webservice_login),
				POST,
				requestBundle);
		this.setMultipartOn();
		this.setCookiesOn();
		this.setCSRFOn();
		this.setJsonResponseOn(new SearchCameraResponseJsonModel());
	}

}
