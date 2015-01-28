package com.maxkalavera.ecoar.main;

import android.content.Context;
import android.os.Bundle;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.HTTPR.HttpRequestLoader;

public class MainCheckSessionLoader extends HttpRequestLoader {
	
	public MainCheckSessionLoader(Context context, Bundle bundle) {
		super(context, context.getResources().getString(R.string.webservice_csession), GET);
		this.setCookiesOn();
		this.setCSRFOn();
	}
}