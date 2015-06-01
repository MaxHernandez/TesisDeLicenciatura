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
	private long _id = -1;
	private long server_id = -1;

	/*************************************************************
	 * Inner ID
	 *************************************************************/	
	public long getId(){
		return this._id;
	}
	
	public void setId(long id) {
		this._id = id;
	}
	
	/*************************************************************
	 * Server ID 
	 *************************************************************/	
	public long getServerId(){
		return this.server_id; 
	}
	
	public void setServerId(long id) {
		this.server_id = id;
	}
	/*************************************************************
	 * Product reference ID
	 *************************************************************/	
	public long getProductReference() {
		return this.product_id;
	}
	
	public void setProductReference(long product_id) {
			this.product_id = product_id;		
	}
	
	public void setProductReference(ProductModel product) {
		this.product_id = product.getCacheId();
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
		return DateJsonPrimitive.getDateFormat().format(this.postingDate);
	}	
	
	public String getDateAsToShow() {
		return DateJsonPrimitive.DATEFORMAT_SHOW.format(this.postingDate);
	}	
	
	
	public void setDateFromString(String dateStr) {
		try {
			this.postingDate = DateJsonPrimitive.getDateFormat().parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*************************************************************
	 * 
	 *************************************************************/
    public Gson serialize() {
		GsonBuilder gsonBuilder=new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, DateJsonPrimitive.getInstance()); 
		Gson gson = gsonBuilder.create();
        return gson;
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
