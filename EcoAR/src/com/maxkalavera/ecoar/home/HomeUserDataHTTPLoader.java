package com.maxkalavera.ecoar.home;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class HomeUserDataHTTPLoader extends HttpRequestLoader {
	
	public HomeUserDataHTTPLoader(Context context) {
		super(context,
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_userdata_get),
				GET);
		setCookiesOn();
		setCSRFOn();
		setJsonResponseOn(new UserDataJsonModel());
	}
	
	
	/*@Override
	public ResponseBundle loadInBackground(){
		return sendHTTPRequest();
	}
	*/
	
}
