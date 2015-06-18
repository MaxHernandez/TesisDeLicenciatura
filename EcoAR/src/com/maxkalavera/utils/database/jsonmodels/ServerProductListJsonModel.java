package com.maxkalavera.utils.database.jsonmodels;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonprimitives.CommentModelJsonPrimitive;
import com.maxkalavera.utils.database.jsonprimitives.ProductModelJsonPrimitive;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;

public class ServerProductListJsonModel  implements BaseResponseJsonModel {
	public List<ProductModel> products;
	
	public ServerProductListJsonModel () {
		this.products = null;
	}
	
	
	/*************************************************************
	 * 
	 *************************************************************/
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ProductModel.class, 
				ProductModelJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
		return (BaseResponseJsonModel) gson.fromJson(plainJson, ServerProductListJsonModel.class);
	}
}
