package com.maxkalavera.ecoar.home;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class HomeUserDataLoader extends HttpRequestLoader {
	
	public HomeUserDataLoader(Context context) {
		super(context,
				context.getResources().getString(R.string.webservice_csession), // CAMBIAR por el url para recuperar datos del servidor.
				GET);
		//UserDataRequestJsonModel.class
	}
	
	@Override
	public ResponseBundle loadInBackground(){
		if (true) { // DEAD CODE En caso de no existir un perfil de usuario lo descarga del servidor
			return null;
		} else {
			return sendHTTPRequest();
		}
	}
	
	
}
