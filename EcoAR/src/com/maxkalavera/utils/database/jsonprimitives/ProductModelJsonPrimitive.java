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
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;

public final class ProductModelJsonPrimitive implements JsonDeserializer<ProductModel> {
	private static ProductModelJsonPrimitive instance = null;

	private ProductModelJsonPrimitive(){}
	public static ProductModelJsonPrimitive getInstance() {
		if(instance == null) 
			instance = new ProductModelJsonPrimitive();
		return instance;
	}
	
	public ProductModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		try {
			JsonObject jsonObject = json.getAsJsonObject();
			
			ProductModel product = new ProductModel();
        	
			if (jsonObject.has("name")) 
				product.name = jsonObject.get("name").getAsString();
        	
			if (jsonObject.has("description")) 
				product.description = jsonObject.get("description").getAsString();

			if (jsonObject.has("image_url")) 
				product.imageURL = jsonObject.get("image_url").getAsString();

			if (jsonObject.has("general_id")) 
				product.generalId = jsonObject.get("general_id").getAsString();
        	
			product.shopingService ="EcoAR";
			
        	return product;
        } catch (JsonParseException e) {  
            e.printStackTrace();  
            return null;  
        }
     }
	
}
