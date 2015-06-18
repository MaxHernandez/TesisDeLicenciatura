package com.maxkalavera.utils.database.jsonmodels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonprimitives.BrandModelJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.CommentModelJsonPrimitive;
import com.maxkalavera.utils.database.productmodel.BrandModel;
import com.maxkalavera.utils.database.productmodel.CommentModel;

public class SearchCameraResponseJsonModel implements BaseResponseJsonModel  {
	
	public String query = null;
	public BrandModel brand = null;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(BrandModel.class, BrandModelJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, SearchCameraResponseJsonModel.class);
	}
}
