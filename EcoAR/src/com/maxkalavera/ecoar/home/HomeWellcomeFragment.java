package com.maxkalavera.ecoar.home;

import com.maxkalavera.ecoar.R;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeWellcomeFragment extends Fragment implements LoaderCallbacks<Cursor>{
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.home_wellcome, container, false);
    }
    
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        getLoaderManager().initLoader(0, null, this);
    }
    
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		//TextView usernameText = (TextView)this.findViewById(R.id.home_wellcomeuser_usernametext);
		//usernameText.setText("Pablo");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
