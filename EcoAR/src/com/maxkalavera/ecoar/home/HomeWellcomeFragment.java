package com.maxkalavera.ecoar.home;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.datamanagers.UserDataManager;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.maxkalavera.utils.jsonmodels.UserDataJsonModel;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeWellcomeFragment extends Fragment implements LoaderCallbacks<ResponseBundle>{

	UserDataManager userDM;
	UserDataJsonModel userDataJM;
	
	/*************************************************************
	 * 
	 *************************************************************/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.home_wellcome, container, false);
    }
    
	/*************************************************************
	 * 
	 *************************************************************/    
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // Get User Profile data 
        Activity tempAct = this.getActivity();
        BaseActivity activity = (BaseActivity) tempAct; 
        this.userDM = activity.getUserDataManager();
        
        // Si los datos de usuarios se encuentran guardados
        // localmente los carga, de lo contrario los recupera
        // del servidor.
        if (this.userDM.existUserDataProfile())
        	this.userDataJM = this.userDM.getUserData();
        else
        	getLoaderManager().initLoader(1, null, this);
        
        
    }
    
	/*************************************************************
	 * 
	 *************************************************************/ 
	@Override
	public Loader<ResponseBundle> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> arg0, ResponseBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> arg0) {		
	}
    
}
