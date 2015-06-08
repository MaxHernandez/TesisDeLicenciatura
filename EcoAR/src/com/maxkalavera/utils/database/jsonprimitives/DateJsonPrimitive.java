package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.maxkalavera.utils.database.productmodel.CommentModel;

public final class DateJsonPrimitive implements JsonSerializer<Date>, JsonDeserializer<Date> {
	private static DateJsonPrimitive instance = null;
	//public static final DateFormat DATEFORMAT = DateFormat.getDateTimeInstance();
	// 																	'14:30 25/10/06'
	
	private static DateFormat DATEFORMAT = null;
	public static final DateFormat DATEFORMAT_SHOW = new SimpleDateFormat("HH:mm yyyy/MM/dd");

	public static DateFormat getDateFormat() {
		if (DateJsonPrimitive.DATEFORMAT == null) {
			DateJsonPrimitive.DATEFORMAT = 
					new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
			DateJsonPrimitive.DATEFORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));			
		}
		return DateJsonPrimitive.DATEFORMAT;
	}
	
	private DateJsonPrimitive(){}
	public static DateJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new DateJsonPrimitive();
		return instance;
	}
	
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(DateJsonPrimitive.getDateFormat().format(date));
	}

	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {        
		try {
			
			JsonObject jsonObject = json.getAsJsonObject();
        	if (jsonObject.has("value")) {        		
				return DateJsonPrimitive.getDateFormat().parse(jsonObject.get("value").getAsString());
        	} else
        		return null;
        	
        } catch (JsonParseException e) {  
            e.printStackTrace();  
            return null;  
        } catch (ParseException e) {
            e.printStackTrace();  
            return null;  
        }
     }
	
};