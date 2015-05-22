package com.maxkalavera.utils.database.productmodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import com.maxkalavera.utils.BitmapStringConverter;

public class ProductModel implements Parcelable {//implements Serializable {
	public String name            = "";
	public String description     = ""; 
	public String shopingService  = "";
	public String url             = "";
	public Bitmap image           = null;
	public String imageURL 	       = "";
	public String generalID       = "";
	
	private long cache_id        = -1;
	private long groceries_id    = -1;  
	
	public long getCacheID() {
		return this.cache_id;
	}
	
	public void setCacheID(long id) {
		this.cache_id = id;
	}
	
	public long getGroceriesID() {
		return this.groceries_id;
	}
	
	public void setGroceriesID(long id) {
		this.groceries_id = id;
	}
	
	public ProductModel() {
	}
	/************************************************************
	 * Parte del codigo que sirve para hace parceable
	 ************************************************************/
	public ProductModel(Parcel in) {		
		this.name            = in.readString();
		this.description     = in.readString(); 
		this.shopingService  = in.readString();
		this.url             = in.readString();
		this.image           = BitmapStringConverter.StringToBitMap(in.readString());
		this.imageURL 	      = in.readString();
		this.generalID       = in.readString();
		
		this.cache_id        = in.readLong();
		this.groceries_id    = in.readLong();  
	};
	
	@Override
	public int describeContents() {
	return 0;
	}
	 
	@Override
	public void writeToParcel(Parcel dest, int flags) {	 
		dest.writeStringArray(
				new String[]{this.name,
						this.description, 
						this.shopingService,
						this.url,
						BitmapStringConverter.BitMapToString(this.image),
						this.imageURL,
						this.generalID,
						String.valueOf(this.cache_id),
						String.valueOf(this.groceries_id)
						}
				);
	}
	
	public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
		 
		@Override
		public ProductModel createFromParcel(Parcel source) {
			return new ProductModel(source);
		}
		 
		@Override
		public ProductModel[] newArray(int size) {
			return new ProductModel[size];
		}
	};
	
};
