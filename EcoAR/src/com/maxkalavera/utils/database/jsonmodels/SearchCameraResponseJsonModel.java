package com.maxkalavera.utils.database.jsonmodels;

import com.google.gson.Gson;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorsJsonModel;

public class SearchCameraResponseJsonModel implements BaseResponseJsonModel  {
	
	public String query;
	public Boolean success; 
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, LoginErrorsJsonModel.class);
	}
}
