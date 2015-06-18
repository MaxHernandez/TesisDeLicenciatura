package com.maxkalavera.utils.database.productmodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;

public class BrandModel implements BaseResponseJsonModel, Parcelable  {
	
	public double id = -1;
	public String name = null;
	public String description = null;
	public String webpage = null;
	
	public Bitmap image = null;
	public String image_url = null;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, BrandModel.class);
	}

	public BrandModel() {
		
	}
	
	/************************************************************
	 * Parte del codigo que sirve para hace parceable
	 ************************************************************/

    protected BrandModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        webpage = in.readString();
        image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        image_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(webpage);
        dest.writeValue(image);
        dest.writeString(image_url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BrandModel> CREATOR = new Parcelable.Creator<BrandModel>() {
        @Override
        public BrandModel createFromParcel(Parcel in) {
            return new BrandModel(in);
        }

        @Override
        public BrandModel[] newArray(int size) {
            return new BrandModel[size];
        }
    };
}
