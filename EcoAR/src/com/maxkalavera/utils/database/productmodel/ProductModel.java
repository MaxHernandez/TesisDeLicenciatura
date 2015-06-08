package com.maxkalavera.utils.database.productmodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import com.maxkalavera.utils.ImageStringConverter;

public class ProductModel implements Parcelable {//implements Serializable {
	public String name            = "";
	public String description     = "";
	public String shopingService  = "";
	public String url             = "";
	public Bitmap image           = null;
	public String imageURL 	       = "";
	public String generalId       = "";
	
	private long cache_id        = -1;
	private long groceries_id    = -1;
	
	// For groceries only
	private boolean groceries_checked = false;
	public int number_of_products = 0;
	
	public long getCacheId() {
		return this.cache_id;
	}
	
	public void setCacheId(long id) {
		this.cache_id = id;
	}
	
	public long getGroceriesId() {
		return this.groceries_id;
	}
	
	public void setGroceriesId(long id) {
		this.groceries_id = id;
	}

	public boolean isChecked() {
		return this.groceries_checked;
	}
	
	public void setChecked(boolean checked) {
		this.groceries_checked = checked;
	}
	
	public ProductModel() {
	}
	
	
	/************************************************************
	 * Parte del codigo que sirve para hace parceable
	 ************************************************************/
	
    protected ProductModel(Parcel in) {
    	name = in.readString();
        description = in.readString();
        shopingService = in.readString();
        url = in.readString();
        image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imageURL = in.readString();
        generalId = in.readString();
        cache_id = in.readLong();
        groceries_id = in.readLong();
        groceries_checked = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(name);
        dest.writeString(description);
        dest.writeString(shopingService);
        dest.writeString(url);
        dest.writeValue(image);
        dest.writeString(imageURL);
        dest.writeString(generalId);
        dest.writeLong(cache_id);
        dest.writeLong(groceries_id);
        dest.writeByte((byte) (groceries_checked ? 0x01 : 0x00));
    }
    
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
};
