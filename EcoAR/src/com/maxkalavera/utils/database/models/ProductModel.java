package com.maxkalavera.utils.database.models;

import android.graphics.Bitmap;
import java.io.Serializable;

public class ProductModel implements Serializable {
	public String name     = "";
	public String description     = ""; 
	public String shopingService  = "";
	public String url             = "";
	public Bitmap image           = null;
	public String imageURL = "";
	private long _id = -1; 
	
	public long getID() {
		return this._id;
	}
	
	public void setID(long id) {
		this._id = id;
	}
	
}
