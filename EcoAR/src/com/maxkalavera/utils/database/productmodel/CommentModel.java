package com.maxkalavera.utils.database.productmodel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxkalavera.utils.database.jsonmodels.BaseRequestJsonModel;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.database.jsonprimitives.DateJsonPrimitive;

import android.text.format.Time;

public class CommentModel implements BaseRequestJsonModel,  BaseResponseJsonModel {
	public String body;
	public String username;
	public Date postingDate;	
	
	private long product_id = -1;
	
	public long getProductReference() {
		return this.product_id;
	}
	
	public void setProductReference(long product_id) {
			this.product_id = product_id;		
	}
	
	public void setProductReference(ProductModel product) {
		this.product_id = product.getCacheID();
	}
	
	/*************************************************************
	 * 
	 *************************************************************/	
	
	public CommentModel () {
	}
	
	public CommentModel (String body, String username) {
		this.postingDate = Calendar.getInstance().getTime();
		this.body = body;
		this.username = username;
	}

	/*************************************************************
	 * 
	 *************************************************************/	
	public void setCurrentDate() {
		this.postingDate = Calendar.getInstance().getTime();
	}
	
	public String getDateAsString() {
		return DateJsonPrimitive.DATEFORMAT.format(this.postingDate);
	}	
	
	public void setDateFromString(String dateStr) {
		try {
			this.postingDate = DateJsonPrimitive.DATEFORMAT.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*************************************************************
	 * 
	 *************************************************************/
    public String serialize() {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, DateJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
	
	/*************************************************************
	 * 
	 *************************************************************/
	public BaseResponseJsonModel deserialize(String plainJson) {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, DateJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
		return (BaseResponseJsonModel) gson.fromJson(plainJson, UserDataJsonModel.class);
	}
	
};
