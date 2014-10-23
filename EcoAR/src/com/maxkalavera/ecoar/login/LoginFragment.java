package com.maxkalavera.ecoar.login;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.utils.UserSession;

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


public class LoginFragment extends Fragment implements LoaderCallbacks<Boolean>, OnClickListener {
	String usernameBuff;
	String passwordBuff;
	Button sendButton;
	ProgressBar progressBar;
	EditText usernameEditText;
	EditText passwordEditText;
	TextView errorText;
	UserSession userSession;
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        this.userSession = new UserSession(getActivity());
        
		this.usernameEditText = (EditText) getActivity()
				.findViewById(R.id.login_username);
		this.passwordEditText = (EditText) getActivity()
				.findViewById(R.id.login_password);
		this.sendButton = (Button) getActivity().findViewById(R.id.login_send);
		this.sendButton.setOnClickListener(this);
		this.progressBar = (ProgressBar) getActivity().findViewById(R.id.login_progressbar);
		this.errorText = (TextView) getActivity().findViewById(R.id.login_errortext);
		
		TextView singuptext = (TextView) getActivity().findViewById(R.id.login_singuptext);
		singuptext.setOnClickListener(this);
		TextView recoverpasstext = (TextView) getActivity().findViewById(R.id.login_recoverpasstext);
		recoverpasstext.setOnClickListener(this);
		
		this.usernameBuff = "";
		this.passwordBuff = "";
    }
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.login, container, false);
    }
    
	@Override
	public Loader<Boolean> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				LoginFragmentLoginLoader loader = 
					new LoginFragmentLoginLoader(getActivity(), this.usernameBuff, this.passwordBuff);
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
		this.usernameEditText.setEnabled(true);
		this.passwordEditText.setEnabled(true);
		this.progressBar.setVisibility(View.GONE);
        this.sendButton.setVisibility(View.VISIBLE);
        if (data != null) {
        	if (data) {
        		this.userSession.setSessionStatus(true);        	
        		Intent intent = new Intent();
        		intent.setClass(getActivity(), Home.class);
        		startActivity(intent);
        	}else{
                this.errorText.setVisibility(View.VISIBLE);
        	}
        }
	}

	@Override
	public void onLoaderReset(Loader<Boolean> loader) {		
	}

	private void send() {
		this.usernameBuff = this.usernameEditText.getText().toString();
		this.passwordBuff = this.passwordEditText.getText().toString();
		this.usernameEditText.setEnabled(false);
		this.passwordEditText.setEnabled(false);
		this.progressBar.setVisibility(View.VISIBLE);
        this.sendButton.setVisibility(View.GONE);
        this.errorText.setVisibility(View.INVISIBLE);
		
		getLoaderManager().restartLoader(1, null, this);
	}
	
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
