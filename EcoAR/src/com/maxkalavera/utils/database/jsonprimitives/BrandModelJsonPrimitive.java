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
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class BrandModelJsonPrimitive implements JsonDeserializer<BrandModel> {
	private static BrandModelJsonPrimitive instance = null;

	private BrandModelJsonPrimitive(){}
	public static BrandModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new BrandModelJsonPrimitive();
		return instance;
	}
	
	public BrandModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		try {
			JsonObject jsonObject = json.getAsJsonObject();
			
			BrandModel brand = new BrandModel();
			
        	if (jsonObject.has("name")) 
        		brand.name = jsonObject.get("name").getAsString();
        	
        	if (jsonObject.has("description")) 
        		brand.description = jsonObject.get("description").getAsString();
        	
        	if (jsonObject.has("webpage")) 
        		brand.webpage = jsonObject.get("webpage").getAsString();
        	
        	if (jsonObject.has("image_url")) 
        		brand.image_url = jsonObject.get("image_url").getAsString();
        	
        	return brand;
        } catch (JsonParseException e) {  
            e.printStackTrace();  
            return null;  
        }
     }
	
}
