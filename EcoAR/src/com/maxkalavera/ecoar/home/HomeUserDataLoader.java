package com.maxkalavera.ecoar.home;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class HomeUserDataLoader extends HttpRequestLoader {
	
	public HomeUserDataLoader(Context context) {
		super(context,
				context.getResources().getString(R.string.webservice_userdata), // CAMBIAR por el url para recuperar datos del servidor.
				GET);
		setCookiesOn();
		setCSRFOn();
		setJsonResponseOn(new UserDataJsonModel());
		//UserDataRequestJsonModel.class
	}
	
	
	/*@Override
	public ResponseBundle loadInBackground(){
		return sendHTTPRequest();
	}
	*/
	
}
