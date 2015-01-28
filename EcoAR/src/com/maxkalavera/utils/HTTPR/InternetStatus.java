package com.maxkalavera.utils.HTTPR;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetStatus {
	Context context;
	
	public InternetStatus(Context context) {
		this.context = context;
	}
	
	public boolean isOnline() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return (connectivityManager.getActiveNetworkInfo() != null 
	    		&& connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
	}
}
