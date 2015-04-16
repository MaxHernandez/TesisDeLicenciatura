package com.maxkalavera.ecoar.LogOut;

import android.content.Context;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;

public class LogOutLoader extends HttpRequestLoader {
	
	public LogOutLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				context.getResources().getString(R.string.webservice_logout),
				POST);
		this.setCookiesOn();
		this.setCSRFOn();
	}
};
