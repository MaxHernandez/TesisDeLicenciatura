package com.maxkalavera.utils.database.productmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonmodels.BaseRequestJsonModel;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.UsersScoreModelJsonPrimitive;

public class ProductInfoModel implements BaseResponseJsonModel {
	public UsersScoreModel usersScore = null;
	public float ecological_score = (float) 0.0;
	
	//ProductModel product; referencia a product ID
	private long product_id = -1;
	
	public long getProductReference() {
		return this.product_id;
	}
	
	public void setProductReference(long product_id) {
			this.product_id = product_id;		
	}
	
	public void setProductReference(ProductModel product) {
		this.product_id = product.getCacheId();
}
	
	
	// identificador en la base de datos		
	private long _id    = -1;  
	
	public long getId() {
		return this._id;
	}
	
	public void setId(long id) {
		this._id = id;
	}

	/********************************************************
	 * 
	 ********************************************************/
	public ProductInfoModel() {
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UsersScoreModel.class, UsersScoreModelJsonPrimitive.getInstance());
		Gson gson = gsonBuilder.create();
		return (BaseResponseJsonModel) gson.fromJson(plainJson, ProductInfoModel.class);
	}
	
};
