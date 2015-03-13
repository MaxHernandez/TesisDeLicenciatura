package com.maxkalavera.ecoar.login;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.utils.UserSession;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
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
	TextView errorText;
	UserSession userSession;
	
	/************************************************************
	 * Constructor Method
	 ************************************************************/
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        RequestParamsBundle paramsBundle = null;
        
        this.userSession = new UserSession(getActivity());
		this.usernameEditText = (EditText) getActivity().findViewById(R.id.login_username);
		this.passwordEditText = (EditText) getActivity().findViewById(R.id.login_password);
		
		this.sendButton = (Button) getActivity().findViewById(R.id.login_send);
		this.sendButton.setOnClickListener(this);
		this.progressBar = (ProgressBar) getActivity().findViewById(R.id.login_progressbar);
		this.errorText = (TextView) getActivity().findViewById(R.id.login_errortext);
		
		TextView singuptext = (TextView) getActivity().findViewById(R.id.login_singuptext);
		singuptext.setOnClickListener(this);
		TextView recoverpasstext = (TextView) getActivity().findViewById(R.id.login_recoverpasstext);
		recoverpasstext.setOnClickListener(this);
    }
	
    // Method to set inner layout of the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.login, container, false);
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
        		this.userSession.setSessionStatus(true);        	
        		Intent intent = new Intent();
        		intent.setClass(getActivity(), Home.class);
        		startActivity(intent);
        	}else{
        		
        		this.errorText.setVisibility(View.VISIBLE);
        	}
        } else {
        	// Error sending HTTP request
        }
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {		
	}

	/************************************************************
	 * Method that reacts when the send button is pressed
	 ************************************************************/	
	private void send() {
		this.usernameEditText.setEnabled(false);
		this.passwordEditText.setEnabled(false);
		this.progressBar.setVisibility(View.VISIBLE);
        this.sendButton.setVisibility(View.GONE);
        this.errorText.setVisibility(View.INVISIBLE);
        
		if (this.paramsBundle == null) {
			this.paramsBundle = new RequestParamsBundle();
			this.paramsBundle.addJSONParam("username", this.usernameEditText.getText().toString());
			this.paramsBundle.addJSONParam("password", this.passwordEditText.getText().toString());
			getLoaderManager().initLoader(1, null, this);
		} else {
			this.paramsBundle = new RequestParamsBundle();
			this.paramsBundle.addJSONParam("username", this.usernameEditText.getText().toString());
			this.paramsBundle.addJSONParam("password", this.passwordEditText.getText().toString());
			getLoaderManager().restartLoader(1, null, this);			
		}
		
	}
	
	/************************************************************
	 * Buttons listeners
	 ************************************************************/
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.login_send:
				send();
				break;
			case R.id.login_singuptext:
				final Intent intentSingUp = new Intent(Intent.ACTION_VIEW).setData(
						Uri.parse( getResources().getString(R.string.login_singupurl) ));
				startActivity(intentSingUp);
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
	
};
