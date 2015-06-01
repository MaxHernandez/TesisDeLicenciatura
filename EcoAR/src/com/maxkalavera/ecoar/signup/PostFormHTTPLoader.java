package com.maxkalavera.ecoar.signup;

import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.CommentariesCacheDAO;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.httprequest.HttpRequestLoader;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

public class PostFormHTTPLoader extends HttpRequestLoader {

	public PostFormHTTPLoader(Context context, RequestParamsBundle requestBundle) {
		super(context, 
				context.getResources().getString(R.string.webservice) +
				context.getResources().getString(R.string.webservice_singup_post),// ERROR MODIFICAR
				POST,
				requestBundle); 
		this.setCookiesOn();
		this.setCSRFOn();
	}
	
}
