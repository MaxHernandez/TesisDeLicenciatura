package com.maxkalavera.utils.datamanagers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;

import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.jsonmodels.UserDataJsonModel;

public class UserDataManager {
	
	private Activity activity;
	private String filepath;
	
	/*************************************************************
	 * Constructor de la clase
	 *************************************************************/
	public UserDataManager(Activity activity) {
		this.activity = activity;
		this.filepath = 
				activity.getResources().getString(R.string.userdata_filepath);
	}
	
	/*************************************************************
	 * Verificar si existe un perfil de usuario guardado en la
	 * memoria. 
	 *************************************************************/	
	public boolean existUserDataProfile() {
		File file = new File(this.filepath);
		if(file.exists())
			return true;
		return false;
	}
	
	// it will only create one newProfile if the last was deleted
	public boolean createUserDataProfile(UserDataJsonModel userDataJsonModel) {
		FileOutputStream fos;
		if (this.existUserDataProfile())
			return false;
		try {
			fos = this.activity.openFileOutput(this.filepath, Context.MODE_PRIVATE);
			fos.write(userDataJsonModel.serialize().getBytes());
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*************************************************************
	 * Esta funcion sirve para remover el archivo que contiene 
	 * los datos del usuario
	 *************************************************************/	
	public boolean removeUserDataProfile() {
		File file = new File(this.filepath);
		return file.delete();
	}
	
	/*************************************************************
	 * Esta funcion sirve para remover el archivo que contiene 
	 * los datos del usuario
	 *************************************************************/	
	public UserDataJsonModel getUserData() {
		try {
			FileInputStream fin = this.activity.openFileInput(this.filepath);
			int c;
			String temp="";
				while( (c = fin.read()) != -1){
				   temp = temp + Character.toString((char)c);
				}
			fin.close();
			return (UserDataJsonModel) new UserDataJsonModel().deserialize(temp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
