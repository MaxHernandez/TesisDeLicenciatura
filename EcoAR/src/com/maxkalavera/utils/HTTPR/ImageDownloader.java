package com.maxkalavera.utils.HTTPR;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageDownloader {
	public Bitmap downloadImage(String imageURL) {
	    try {
	    	InputStream in = new java.net.URL(imageURL).openStream();
	    	 return BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	    	Log.e("Error", e.getMessage());
	    	e.printStackTrace();
	    }
	    return null;
	}
}
