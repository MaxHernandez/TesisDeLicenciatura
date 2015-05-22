package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class CommentaryModelJsonPrimitive implements JsonDeserializer<CommentModel> {
	private static CommentaryModelJsonPrimitive instance = null;

	private CommentaryModelJsonPrimitive(){}
	public static CommentaryModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new CommentaryModelJsonPrimitive();
		return instance;
	}
	
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(DateJsonPrimitive.DATEFORMAT.format(date));
	}

	public CommentModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {  
            return (CommentModel) new CommentModel().deserialize(json.getAsString());
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
     }
	
}
