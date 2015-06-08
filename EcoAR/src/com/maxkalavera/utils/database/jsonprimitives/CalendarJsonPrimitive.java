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

public final class CalendarJsonPrimitive implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {
	private static CalendarJsonPrimitive instance = null;
	// In simpleDateFormat i want MMM witch means month if i write mm it means minutes
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-DD"); 
	//public static final DateFormat DATEFORMAT = DateFormat.getDateTimeInstance();

	private CalendarJsonPrimitive(){}
	public static CalendarJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new CalendarJsonPrimitive();
		return instance;
	}
	
	public JsonElement serialize(Calendar calendar, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(CalendarJsonPrimitive.DATEFORMAT.format(calendar.getTime()));
	}

	public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {  
        	Date date = CalendarJsonPrimitive.DATEFORMAT.parse(json.getAsString());
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(date);
            return calendar;  
        } catch (final java.text.ParseException e) {  
            e.printStackTrace();  
            return null;  
        }  
     }
	
};