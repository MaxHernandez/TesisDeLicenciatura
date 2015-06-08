package com.maxkalavera.utils.database.jsonprimitives;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class UsersScoreModelJsonPrimitive implements JsonDeserializer<UsersScoreModel> {
	private static UsersScoreModelJsonPrimitive instance = null;

	private UsersScoreModelJsonPrimitive(){}
	public static UsersScoreModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new UsersScoreModelJsonPrimitive();
		return instance;
	}

	public UsersScoreModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)  {
		try {
			JsonObject jsonObject = json.getAsJsonObject();
			
        	UsersScoreModel usersScoreModel = new UsersScoreModel();
        	if (jsonObject.has("users_score")) 
        		usersScoreModel.users_score = jsonObject.get("users_score").getAsFloat();
        	if (jsonObject.has("own_score"))
        		usersScoreModel.own_score = jsonObject.get("own_score").getAsInt();
        	
        	return usersScoreModel;
        } catch (JsonParseException e) {  
            e.printStackTrace();  
            return null;  
        }
     }
	
}
