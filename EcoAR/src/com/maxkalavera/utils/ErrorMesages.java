package com.maxkalavera.utils;

import com.maxkalavera.ecoar.R;

import android.app.AlertDialog;
import android.content.Context;

public class ErrorMesages {

	public static void showMessage(Context context, int title, int message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	    dialog.setTitle(context.getResources().getString(title));
	    dialog.setMessage(context.getResources().getString(message));
	    dialog.setPositiveButton(
	    		context.getResources().getString(R.string.error_afirmative_button),null);
	    dialog.show();

	}
	
	public static void noInternetService(Context context) {	    
		ErrorMesages.showMessage(context, 
				R.string.error_no_internet_service_title,
				R.string.error_no_internet_service_message
				);
	}
	
	public static void errorSendingHttpRequest(Context context) {
		ErrorMesages.showMessage(context, 
				R.string.error_sending_http_request_title,
				R.string.error_sending_http_request_message
				);
	}
	
	public static void errorRetrievingJsonData(Context context) {
		ErrorMesages.showMessage(context, 
				R.string.error_retrieving_json_data_title,
				R.string.error_retrieving_json_data_message
				);
	}
	
	public static void errorRetrievingData(Context context) {
		ErrorMesages.showMessage(context, 
				R.string.error_retrieving_data_title,
				R.string.error_retrieving_data_message
				);
	}	
	
};
