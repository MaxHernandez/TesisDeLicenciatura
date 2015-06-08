package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.util.Date;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class CommentModelJsonPrimitive implements JsonDeserializer<CommentModel> {
	private static CommentModelJsonPrimitive instance = null;

	private CommentModelJsonPrimitive(){}
	public static CommentModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new CommentModelJsonPrimitive();
		return instance;
	}
	
	public CommentModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		try {
			JsonObject jsonObject = json.getAsJsonObject();
			
			CommentModel comment = new CommentModel();
        	if (jsonObject.has("username")) 
        		comment.username = jsonObject.get("username").getAsString();
        	
        	if (jsonObject.has("posting_date")) {
        		JsonObject postingDateJsonObject = jsonObject.getAsJsonObject("posting_date");
        		if (postingDateJsonObject.has("value"))
        			comment.setDateFromString(postingDateJsonObject.get("value").getAsString());
        		
        		//comment.posting_date = jsonObject.get("posting_date").getAsString();
        		//comment.setDateFromString(jsonObject.get("posting_date").getAsString());
        	}
        		
        	if (jsonObject.has("body")) 
        		comment.body = jsonObject.get("body").getAsString();

        	if (jsonObject.has("id")) 
        		comment.id = jsonObject.get("id").getAsLong();

        	
        	return comment;
        } catch (JsonParseException e) {  
            e.printStackTrace();  
            return null;  
        }
     }
	
}
