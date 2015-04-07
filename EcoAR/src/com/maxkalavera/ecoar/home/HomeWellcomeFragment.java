package com.maxkalavera.ecoar.home;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.main.MainCheckSessionHTTPLoader;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.InternetStatus;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeWellcomeFragment extends Fragment implements LoaderCallbacks<ResponseBundle>{

	UserDataDAO userDM;
	
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
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // Get User Profile data 
        Activity tempAct = this.getActivity();
        BaseActivity activity = (BaseActivity) tempAct; 
        this.userDM = activity.getUserDataManager();
        
        // Si los datos de usuarios se encuentran guardados
        // localmente los carga, de lo contrario los recupera
        // del servidor.
        if (this.userDM.existUserDataProfile()) {
        	UserDataJsonModel userDataJM = this.userDM.getUserData();
        	loadInfo(userDataJM);
        } else {
        	getLoaderManager().initLoader(1, null, this);
        }
        
    }
    
	/*************************************************************
	 * 
	 *************************************************************/ 
    public void loadInfo(UserDataJsonModel userDataJM) {
    	((TextView) getActivity().findViewById(R.id.home_wellcomeuser_name)). 
    			setText(userDataJM.firstName);
    }
    
	/*************************************************************
	 * Metodos del Loader para recuperar los datos del usuario del 
	 * servidor.
	 *************************************************************/ 
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle arg1) {
		InternetStatus internetStatus = new InternetStatus(this.getActivity());
		if (!internetStatus.isOnline()){
			// Aqui hay que imprimir un aviso en la pantalla de que es necesaria conexion a internet
			return null;
		}
		
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				HomeUserDataLoader loader = new HomeUserDataLoader(this.getActivity());
				loader.forceLoad();
				return loader;
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		Response response = responseBundle.getResponse();
		UserDataJsonModel userDataJM = (UserDataJsonModel)responseBundle.getResponseJsonObject();
		if (response == null){
			// Hubo un error al mandar la petición al servidor
		}
		if (userDataJM == null) {
			// Hubo un error al deserializar el objeto JSON
		}
		this.userDM.createUserDataProfile(userDataJM);
		loadInfo(userDataJM);
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> arg0) {		
	}
    
}
