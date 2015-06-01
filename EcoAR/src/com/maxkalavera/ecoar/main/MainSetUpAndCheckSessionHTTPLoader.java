package com.maxkalavera.ecoar.main;

import android.content.Context;
import android.os.Bundle;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.ProductCacheDAO;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class MainSetUpAndCheckSessionHTTPLoader extends HttpRequestLoader {
	
	public MainSetUpAndCheckSessionHTTPLoader(Context context, RequestParamsBundle bundle) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_session_get),
				GET,
				bundle);
		this.setCookiesOn();
		this.setCSRFOn();
	}

	// Esta clase es utilizada para correr código extra ademas del que se usa para 
	// hacer la peticion http.
	@Override
	public ResponseBundle loadInBackground(){
		// Codigo para eliminar los productos caducos en la memoria cache
		ProductCacheDAO productCacheDAO = new ProductCacheDAO(super.getContext());
		productCacheDAO.open();
		productCacheDAO.removeOldProducts();
		productCacheDAO.close();
		
		return sendHTTPRequest();
	}

};