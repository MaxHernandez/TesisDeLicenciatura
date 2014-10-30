package com.maxkalavera.utils.jsonmodels;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.Gson;
import com.maxkalavera.utils.datamodels.ProductModel;

public class GroceryListModel {
	ArrayList<ProductModel> elements;
	String filename = "grocery_list_model";
	FileOutputStream outputStream;
	FileInputStream inputStream;
	
	private GroceryListModel(){
		this.elements = new ArrayList<ProductModel>();
	}
	
	public void saveData(Context context) {
		try {
			this.outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			this.outputStream.write(this.serialize().getBytes());
			this.outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GroceryListModel loadData(Context context) {
		StringBuffer fileContent = new StringBuffer("");
		byte[] buffer = new byte[1024];
		int temp;
		try {
			this.inputStream = context.openFileInput(filename);
			while ((temp = this.inputStream.read(buffer)) != -1) 
			{ 
			  fileContent.append(new String(buffer, 0, temp)); 
			}
			this.inputStream.close();
			
			return create(this.inputStream.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public GroceryListModel getModel() {
		
		return new GroceryListModel();
	}
		
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	    
	static public GroceryListModel create(String serializedData) {
		Gson gson = new Gson();
		return gson.fromJson(serializedData, GroceryListModel.class);
	}
}
