package com.maxkalavera.utils.database.productmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;

public class UsersScoreModel implements BaseResponseJsonModel  {
	public float usersScore  = (float) 0.0;
	public Float ownScore  = null;	
	
	@Override
	public UsersScoreModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return (UsersScoreModel) gson.fromJson(plainJson, UsersScoreModel.class);
	}
	
}
