package com.maxkalavera.ecoar.main;

import android.content.Context;
import android.os.Bundle;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;

public class MainCheckSessionHTTPLoader extends HttpRequestLoader {
	
	public MainCheckSessionHTTPLoader(Context context, RequestParamsBundle bundle) {
		super(context, context.getResources().getString(R.string.webservice_csession), GET, bundle);
		this.setCookiesOn();
		this.setCSRFOn();
	}

};