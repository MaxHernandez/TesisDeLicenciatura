package com.maxkalavera.utils.database.jsonmodels;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonprimitives.CommentaryModelJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;
import com.maxkalavera.utils.database.productmodel.CommentModel;

public class CommentariesListModel implements BaseResponseJsonModel {
	public List<CommentModel> commentaries;
	
	public CommentariesListModel() {
		this.commentaries = null;
	}
	
	/*************************************************************
	 * 
	 *************************************************************/
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, CommentaryModelJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
		return (BaseResponseJsonModel) gson.fromJson(plainJson, UserDataJsonModel.class);
	}
}
