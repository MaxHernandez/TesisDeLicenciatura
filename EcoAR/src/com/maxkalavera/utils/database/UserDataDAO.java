package com.maxkalavera.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.database.jsonprimitives.CalendarJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;

public class UserDataDAO {
	private Context context;
	private String sharedPreferencesKeyword;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedPreferencesEditor;
	
	private static final String EXISTENCE = "exist";
	
	public static final String USERNAME = "username";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String EMAIL = "email";
	public static final String GENDER = "gender";
	public static final String BIRTHDATE = "birthdate";
	
	/*************************************************************
	 * Constructor de la clase
	 *************************************************************/
	public String getUsername() {
		return this.getUserDataSharedPreferences().
				getString(UserDataDAO.USERNAME , "");
	}

	public String getFirstName() {
		return this.getUserDataSharedPreferences().
				getString(UserDataDAO.FIRST_NAME , "");
	}

	public String getLastName() {
		return this.getUserDataSharedPreferences().
				getString(UserDataDAO.LAST_NAME , "");
	}

	public String getEmail() {
		return this.getUserDataSharedPreferences().
				getString(UserDataDAO.EMAIL , "");
	}

	public String getGender() {
		return this.getUserDataSharedPreferences().
				getString(UserDataDAO.GENDER , "");
	}

	public Calendar getBirthday() {
		CalendarJsonPrimitive calendarJsonPrimitive = CalendarJsonPrimitive.getInstance();
		String temp = this.getUserDataSharedPreferences().
			getString(UserDataDAO.BIRTHDATE , "");
		return calendarJsonPrimitive.deserialize(new JsonPrimitive(temp), null, null);
	}
	
	public String serializeBirthday(Calendar in) {
		CalendarJsonPrimitive calendarJsonPrimitive = CalendarJsonPrimitive.getInstance();
		return calendarJsonPrimitive.serialize(in, null, null).getAsString();
	}
	
	/*************************************************************
	 * Constructor de la clase
	 *************************************************************/
	public UserDataDAO(Context context) {
		this.context = context;
		this.sharedPreferencesKeyword = 
				context.getResources().getString(R.string.userdata_filename);
		
		this.sharedPreferences = this.getContext().getSharedPreferences(
				this.sharedPreferencesKeyword, 
				Context.MODE_PRIVATE);
	}
	
	
	private Context getContext() {
		return this.context;
	}
	
	/*************************************************************
	 * Verificar si existe un perfil de usuario guardado en la
	 * memoria. 
	 *************************************************************/	
	public Boolean existUserDataProfile() {
		if (this.getUserDataSharedPreferences() .contains(UserDataDAO.EXISTENCE)) 
			return true;
		else
			return false;
	}
	
	/*************************************************************
	 *  # it will only create one newProfile if the last one has 
	 *  been deleted
	 *************************************************************/	
	public void createUserDataProfile(UserDataJsonModel userDataJsonModel) {
		if (this.existUserDataProfile())
			return ;
		
		SharedPreferences sharedPreferences = getUserDataSharedPreferences();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(UserDataDAO.EXISTENCE, true);
		editor.commit();
		
		this.saveUserData(userDataJsonModel);
	}
	
	/*************************************************************
	 * 
	 *************************************************************/	
	public void saveUserData(UserDataJsonModel userDataJsonModel) {
		SharedPreferences sharedPreferences = getUserDataSharedPreferences();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putString(UserDataDAO.USERNAME, userDataJsonModel.username);
		editor.putString(UserDataDAO.FIRST_NAME, userDataJsonModel.first_name);
		editor.putString(UserDataDAO.LAST_NAME, userDataJsonModel.last_name);
		editor.putString(UserDataDAO.EMAIL, userDataJsonModel.email);
		editor.putString(UserDataDAO.GENDER, userDataJsonModel.gender);
		editor.putString(UserDataDAO.BIRTHDATE, 
				this.serializeBirthday(userDataJsonModel.birthdate));

		editor.commit();
	}
	
	public UserDataJsonModel getAsUserDataModel() {
		UserDataJsonModel userDataJsonModel = new UserDataJsonModel();
		
		userDataJsonModel.username = this.getUsername();
		userDataJsonModel.first_name = this.getFirstName();
		userDataJsonModel.last_name = this.getLastName();
		userDataJsonModel.email = this.getEmail();
		userDataJsonModel.gender = this.getGender();
		userDataJsonModel.birthdate = this.getBirthday();
		
		return userDataJsonModel;
	}
	
	/*************************************************************
	 * Esta funcion sirve para remover el archivo que contiene 
	 * los datos del usuario
	 *************************************************************/	
	public void removeUserDataProfile() {
		SharedPreferences sharedPreferences = getUserDataSharedPreferences();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	/*************************************************************
	 * Esta funcion sirve para remover el archivo que contiene 
	 * los datos del usuario
	 *************************************************************/	
	public SharedPreferences getUserDataSharedPreferences() {
		return this.sharedPreferences;
	}	
	
};
