package com.maxkalavera.utils.databasemodels;

import android.graphics.Bitmap;
import java.io.Serializable;

public class ProductModel implements Serializable {
	public String productName     = "";
	public String productImageURL = "";
	public String description     = ""; 
	public String url             = "";
	public String shopingService  = "";
	public Bitmap image           = null;
	
}
