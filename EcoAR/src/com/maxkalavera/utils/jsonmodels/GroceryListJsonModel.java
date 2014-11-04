package com.maxkalavera.utils.jsonmodels;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.Gson;
import com.maxkalavera.utils.datamodels.ProductModel;

public class GroceryListJsonModel {
	public static ArrayList<ProductModel> elements;
		
	private GroceryListJsonModel(){
		this.elements = new ArrayList<ProductModel>();
	}
	
	public void addProduct(ProductModel element){
		this.elements.add(element);
	}
	
	public void saveData(Context context) {
		FileOutputStream outputStream;
		String filename = "grocery_list_model";
		try {
			outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(this.serialize().getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	static public GroceryListJsonModel loadData(Context context) {
		FileInputStream inputStream;
		String filename = "grocery_list_model";
		StringBuffer fileContent = new StringBuffer("");
		byte[] buffer = new byte[1024];
		int temp;
		try {
			inputStream = context.openFileInput(filename);
			while ((temp = inputStream.read(buffer)) != -1) 
			{ 
			  fileContent.append(new String(buffer, 0, temp)); 
			}
			inputStream.close();			
			return create(inputStream.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public GroceryListJsonModel getObject(Context context) {
		GroceryListJsonModel thisObject = loadData(context);
		if (thisObject != null) {
			return thisObject;
		}else{
			return new GroceryListJsonModel();
		}
	}
		
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	    
	static public GroceryListJsonModel create(String serializedData) {
		Gson gson = new Gson();
		return gson.fromJson(serializedData, GroceryListJsonModel.class);
	}
}
