package com.maxkalavera.ecoar.home;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.main.MainSetUpAndCheckSessionHTTPLoader;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.database.UserDataDAO;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.InternetStatus;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeWellcomeFragment extends Fragment implements 
LoaderCallbacks<ResponseBundle> {

	UserDataDAO userDM;
	    
	/*************************************************************
	 * 
	 *************************************************************/    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // Get User Profile data Object 
        Activity tempAct = this.getActivity();
        BaseActivity activity = (BaseActivity) tempAct; 
        this.userDM = activity.getUserDataManager();
        
        
        // Si los datos de usuarios se encuentran guardados
        // localmente los carga, de lo contrario los recupera
        // del servidor.
        UserSessionDAO userSession = new UserSessionDAO(this.getActivity());
        if(userSession.checkSessionStatus()) {
        	if (this.userDM.existUserDataProfile()) {
        		loadInfo(this.userDM);
        	} else {
        		getLoaderManager().initLoader(1, null, this);
        	}
        }
        
    }
    
	/*************************************************************
	 * 
	 *************************************************************/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	return inflater.inflate(R.layout.home_wellcome, container, false);
    }

    
	/*************************************************************
	 * 
	 *************************************************************/ 
    public void loadInfo(UserDataDAO userDataDAO) {
    	TextView title = ((TextView) getActivity().findViewById(R.id.home_wellcomeuser_title));
    	if (userDataDAO.getUsername() != null)
    		title.setText(title.getText() +" "+userDataDAO.getUsername());
    	else
    		Log.i("HomeWellcomeFragment", "No hay username en los datos de usuario.");
    }
    
	/*************************************************************
	 * Metodos del Loader para recuperar los datos del usuario del 
	 * servidor.
	 *************************************************************/ 
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle arg1) {
		
		
		switch (loaderID) {
			case 0:
				return null;
			case 1:
				if(!InternetStatusChecker.checkInternetStauts(this.getActivity()))
					return null;
				
				HomeUserDataHTTPLoader loader = new HomeUserDataHTTPLoader(this.getActivity());
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
		if (response != null && response.isSuccessful()){
			if (userDataJM != null) {
				// Esta linea utiliza el objeto administrador de la base de datos
				// para guardar los datos del usuario en la memoria interna.
		        if (this.userDM.existUserDataProfile())
		        	this.userDM.saveUserData(userDataJM);
		        else
		        	this.userDM.createUserDataProfile(userDataJM);
		        
				// Carga los datos necesarios en pantalla
				this.loadInfo(this.userDM);
			}else {
				// Error al parsear la respuesta del servidor
			}
		} else {
			// Error al mandar la peticion 
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> arg0) {		
	}
    
}
