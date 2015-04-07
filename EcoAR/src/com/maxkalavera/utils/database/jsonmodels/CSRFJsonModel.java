package com.maxkalavera.utils.database.jsonmodels;

import com.google.gson.Gson;

public class CSRFJsonModel {
	public String csrf_token;
	
	public CSRFJsonModel(String csrfToken){
		this.csrf_token = csrfToken;
	}
	
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    static public CSRFJsonModel deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CSRFJsonModel.class);
    }
	
}
