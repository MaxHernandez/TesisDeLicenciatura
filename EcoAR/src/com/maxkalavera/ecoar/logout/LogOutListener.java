package com.maxkalavera.ecoar.logout;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.login.LoginFragmentHTTPLoader;
import com.maxkalavera.ecoar.main.Main;
import com.maxkalavera.utils.ErrorMesages;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class LogOutListener implements OnTouchListener, LoaderCallbacks<ResponseBundle> {
	Context context;
	
	private static final int SEND_REQUEST = 1;
	
	public LogOutListener(Context context) {
		this.context = context;
	}
	
	private Context getContext() {
		return this.context;
	}
	
	public boolean onTouch(View item, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	item.setBackgroundResource(R.drawable.someproducts_item_onselect);
        	return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        	item.setBackgroundResource(R.drawable.someproducts_item);
        	
        	((FragmentActivity) this.getContext()).
        		getSupportLoaderManager().restartLoader(SEND_REQUEST, null, this);
        }	
		return false;
	}
	
	/************************************************************
	 * HTTP Request methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case SEND_REQUEST:
				if(!InternetStatusChecker.checkInternetStauts(this.getContext()))
					return null;
				
				LogOutHTTPLoader loader = 
					new LogOutHTTPLoader(this.getContext(), null);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {        
		if (responseBundle.getResponse() != null && responseBundle.getResponse().isSuccessful()) {
        	
        	UserSessionDAO userSessionDAO = new UserSessionDAO(this.getContext());
        	userSessionDAO.setSessionStatus(false);
        	
        	UserDataDAO userDataDAO = new UserDataDAO(this.getContext());
        	userDataDAO.removeUserDataProfile();
        	
        	Intent intent = new Intent(getContext(), Home.class);
        	this.getContext().startActivity(intent);
        	((FragmentActivity) this.getContext()).finish();
        	
        } else {
        	ErrorMesages.errorSendingHttpRequest(this.getContext());
        }
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {		
	}
	
};
