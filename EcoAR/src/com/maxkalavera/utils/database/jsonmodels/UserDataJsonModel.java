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

import android.graphics.Bitmap;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class DateSerializer implements JsonSerializer {
	  public JsonElement serialize(Object date, Type typeOfSrc, JsonSerializationContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");  
	    return new JsonPrimitive(sdf.format((Date) date));
	  }
} 

class DateDeserializer implements JsonDeserializer<Date> {
	  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		    Date date = null;
		    try {
			  date = sdf.parse(json.getAsJsonPrimitive().getAsString());
			  return date;
		    } catch (ParseException e) {
				e.printStackTrace();
		    }
		    return date;
       }
} 

public class UserDataJsonModel extends BaseResponseJsonModel {
	public String username;
	public String firstName;
	public String secondName;
	public String email;
	public String gender;
	public Date birthdate;
	public String userPicURL; 
	public Bitmap userPic;

    public String serialize() {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer()); 
		Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer()); 
		Gson gson = gsonBuilder.create();
		return gson.fromJson(plainJson, LoginErrorJsonModel.class);
	}
		
}
