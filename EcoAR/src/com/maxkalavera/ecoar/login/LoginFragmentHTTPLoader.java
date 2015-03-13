package com.maxkalavera.ecoar.login;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.Pair;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.login.JSONModels.LoginErrorJsonModel;
import com.maxkalavera.utils.HTTPRequestTemp;
import com.maxkalavera.utils.databasemodels.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.jsonmodels.CSRFJsonModel;

public class LoginFragmentHTTPLoader extends HttpRequestLoader  {

	public LoginFragmentHTTPLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				context.getResources().getString(R.string.webservice_login),
				POST,
				requestBundle); 
		this.setJsonResponseOn(new LoginErrorJsonModel());
	}
	
}
