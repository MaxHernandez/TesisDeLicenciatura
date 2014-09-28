package com.maxkalavera.ecoar.login;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.searchbar.SearchBarResultsListFragmentLoader;

import android.database.Cursor;
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


public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor>, OnClickListener {
	String username = "";
	String password = "";
	
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        
		Button startSearchButton = (Button) getActivity().findViewById(R.id.login_send);
		startSearchButton.setOnClickListener(this);
        
    }
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.login, container, false);
    }
    
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
		case 0:
			return null;
		case 1:
			LoginFragmentLoginLoader loader = new LoginFragmentLoginLoader(getActivity(), this.username, this.password);
			loader.forceLoad();
			return loader;
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {		
	}

	@Override
	public void onClick(View v) {
		
		EditText usernameEditText = (EditText) getActivity()
				.findViewById(R.id.login_username);
		this.username = usernameEditText.getText().toString();
		
		EditText passwordEditText = (EditText) getActivity()
				.findViewById(R.id.login_password);
		this.password = passwordEditText.getText().toString();

		getLoaderManager().restartLoader(1, null, this);
		
	}
    
}
