package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class UsersScoreModelJsonPrimitive implements JsonDeserializer<UsersScoreModel> {
	private static UsersScoreModelJsonPrimitive instance = null;

	private UsersScoreModelJsonPrimitive(){}
	public static UsersScoreModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new UsersScoreModelJsonPrimitive();
		return instance;
	}

	public UsersScoreModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {  
            return new UsersScoreModel().deserialize(json.getAsString());
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
     }
	
}
