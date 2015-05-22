package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class DateJsonPrimitive implements JsonSerializer<Date>, JsonDeserializer<Date> {
	private static DateJsonPrimitive instance = null;
	public static final DateFormat DATEFORMAT = DateFormat.getDateTimeInstance();

	private DateJsonPrimitive(){}
	public static DateJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new DateJsonPrimitive();
		return instance;
	}
	
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(DateJsonPrimitive.DATEFORMAT.format(date));
	}

	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {  
            return DateJsonPrimitive.DATEFORMAT.parse(json.getAsString());  
        } catch (final java.text.ParseException e) {  
            e.printStackTrace();  
            return null;  
        }  
     }
	
};