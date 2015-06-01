package com.maxkalavera.ecoar.logout;

import android.content.Context;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;

public class LogOutHTTPLoader extends HttpRequestLoader {
	
	public LogOutHTTPLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_logout_delete),
				DELETE);
		this.setCookiesOn();
		this.setCSRFOn();
	}
};
