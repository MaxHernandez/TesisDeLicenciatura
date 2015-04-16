package com.maxkalavera.utils.database.jsonmodels;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorJsonModel;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;

import android.graphics.Bitmap;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class UserDataJsonModel extends BaseResponseJsonModel {
	public String username;
	public String firstName;
	public String secondName;
	public String email;
	public String gender;
	public Date birthdate;
	public String userPicURL; 
	public Bitmap userPic; // Aun no existe funcionalidad para guardar este parametro

	/*************************************************************
	 * 
	 *************************************************************/
    public String serialize() {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, DateJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
	
	/*************************************************************
	 * 
	 *************************************************************/
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, DateJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
		return gson.fromJson(plainJson, LoginErrorJsonModel.class);
	}
		
}
