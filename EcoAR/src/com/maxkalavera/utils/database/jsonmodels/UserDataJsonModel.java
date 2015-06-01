package com.maxkalavera.utils.database.jsonmodels;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.maxkalavera.utils.database.jsonprimitives.CalendarJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;

import android.graphics.Bitmap;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class UserDataJsonModel implements BaseResponseJsonModel, BaseRequestJsonModel {
	public String username;
	public String first_name;
	public String last_name;
	public String email;
	public String gender;
	public Calendar birthdate;
	//public String userPicURL; 
	//public Bitmap userPic; // Aun no existe funcionalidad para guardar este parametro

	// This two fields are just here to 
	public String password;
	public String password_confirmation;
	
	/*************************************************************
	 * 
	 *************************************************************/
    public Gson serialize() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Calendar.class, CalendarJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
        return gson;
    }
	
	/*************************************************************
	 * 
	 *************************************************************/
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Calendar.class, CalendarJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
		return (BaseResponseJsonModel) gson.fromJson(plainJson, UserDataJsonModel.class);
	}
		
}
