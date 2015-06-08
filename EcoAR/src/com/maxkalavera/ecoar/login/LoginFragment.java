package com.maxkalavera.ecoar.login;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.ecoar.signup.SignUp;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LoginFragment extends Fragment implements LoaderCallbacks<ResponseBundle>, OnClickListener {
	RequestParamsBundle paramsBundle;
	Button sendButton;
	ProgressBar progressBar;
	EditText usernameEditText;
	EditText passwordEditText;
	
	static final int SEND_REQUEST = 1;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		this.usernameEditText = (EditText) getActivity().findViewById(R.id.login_username);
		this.passwordEditText = (EditText) getActivity().findViewById(R.id.login_password);
		
		this.sendButton = (Button) getActivity().findViewById(R.id.login_send);
		this.sendButton.setOnClickListener(this);
		
		this.progressBar = (ProgressBar) getActivity().findViewById(R.id.login_progressbar);
		
		TextView singuptext = (TextView) getActivity().findViewById(R.id.login_singuptext);
		singuptext.setOnClickListener(this);
		TextView recoverpasstext = (TextView) getActivity().findViewById(R.id.login_recoverpasstext);
		recoverpasstext.setOnClickListener(this);
    }
	
    // Method to set inner layout of the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	return inflater.inflate(R.layout.login, container, false);
    }
    
	/************************************************************
	 * Method used to save the data needed to make the login 
	 ************************************************************/
    
    private void saveLogin(UserDataJsonModel userDataJM) {
        // Get User Profile data Object 
        Activity tempAct = this.getActivity();
        BaseActivity activity = (BaseActivity) tempAct; 
        UserDataDAO userDM = activity.getUserDataManager();
        
        if (userDM.existUserDataProfile())
        	userDM.saveUserData(userDataJM);
        else
        	userDM.createUserDataProfile(userDataJM);
    	
    	UserSessionDAO userSession = new UserSessionDAO(getActivity()); 
    	userSession.setSessionStatus(true);
    }

	/************************************************************
	 * Method that reacts when the send button is pressed
	 ************************************************************/	
	private void send() {
		this.usernameEditText.setEnabled(false);
		this.passwordEditText.setEnabled(false);
		this.progressBar.setVisibility(View.VISIBLE);
        this.sendButton.setVisibility(View.GONE);
        
        getActivity().findViewById(R.id.login_error_username).setVisibility(View.GONE);
        getActivity().findViewById(R.id.login_error_password).setVisibility(View.GONE);
        getActivity().findViewById(R.id.login_non_field_errors).setVisibility(View.GONE);
        
		this.paramsBundle = new RequestParamsBundle();
		this.paramsBundle.addJsonParam("username", this.usernameEditText.getText().toString());
		this.paramsBundle.addJsonParam("password", this.passwordEditText.getText().toString());
		
		getLoaderManager().restartLoader(SEND_REQUEST, null, this);			
		
	}
	
	/************************************************************
	 * Buttons listeners
	 ************************************************************/
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.login_send:
				this.send();
				break;
			case R.id.login_singuptext:
				//final Intent intentSingUp = new Intent(Intent.ACTION_VIEW).setData(
				//		Uri.parse( getResources().getString(R.string.login_singupurl) ));
				//startActivity(intentSingUp);
		        Intent intent = new Intent();
		        intent.setClass(this.getActivity(), SignUp.class);
		        this.getActivity().startActivity(intent);
		        
				break;
			case R.id.login_recoverpasstext:
				final Intent intentRecoverPass = new Intent(Intent.ACTION_VIEW).setData(
						Uri.parse( getResources().getString(R.string.login_recoverpassurl) ));
				startActivity(intentRecoverPass);
				break;
			default:
				break;
		}		
	}
	
	/************************************************************
	 * HTTP Request methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				LoginFragmentHTTPLoader loader = 
					new LoginFragmentHTTPLoader(getActivity(), this.paramsBundle);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		this.usernameEditText.setEnabled(true);
		this.passwordEditText.setEnabled(true);	
		this.progressBar.setVisibility(View.GONE);
        this.sendButton.setVisibility(View.VISIBLE);
        
        if (responseBundle.getResponse() != null) {
        	if (responseBundle.getResponse().isSuccessful()) {
        		if (responseBundle.getResponseJsonObject() != null) {
        			
        			UserDataJsonModel userDataJM = 
        					(UserDataJsonModel)responseBundle.getResponseJsonObject();
            		this.saveLogin(userDataJM);
            		
            		Intent intent = new Intent();
            		intent.setClass(getActivity(), Home.class);
            		startActivity(intent);
            		this.getActivity().finish();	
        		} else {
        			// Error al deserializar el Json
        		}
        		
        	}else{
        		LoginErrorsJsonModel loginErrorsJsonModel = 
        				(LoginErrorsJsonModel) responseBundle.getResponseJsonObject();
        		
        		if (loginErrorsJsonModel != null) {
        			if (loginErrorsJsonModel.username != null) {
        				TextView errorUsername = 
        						(TextView) getActivity().findViewById(R.id.login_error_username);
        				errorUsername.setVisibility(View.VISIBLE);
        				errorUsername.setText(this.getResources().getText(R.string.login_error_username)+" "+
    							loginErrorsJsonModel.username.get(0));
        			} 
        			if (loginErrorsJsonModel.password != null) {
        				TextView errorPassword = 
        						(TextView) getActivity().findViewById(R.id.login_error_password);
        				errorPassword.setVisibility(View.VISIBLE);
        				errorPassword.setText(this.getResources().getText(R.string.login_error_password)+" "+
        						loginErrorsJsonModel.password.get(0));
        			}
        			if (loginErrorsJsonModel.non_field_errors != null) {
        				TextView errorNonFieldErrors = 
        						(TextView) getActivity().findViewById(R.id.login_non_field_errors);
        				errorNonFieldErrors.setVisibility(View.VISIBLE);
        				errorNonFieldErrors.setText(loginErrorsJsonModel.non_field_errors.get(0));
        			}
        		} else {
        			// Error al deserializar el Json
        		}
        	}
        } else {
        	// Error sending HTTP request
        }
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {		
	}
	
};
