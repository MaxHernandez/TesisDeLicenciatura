package com.maxkalavera.ecoar.login.jsonmodels;
import com.google.gson.Gson;
import com.maxkalavera.utils.database.jsonmodels.BaseResponseJsonModel;

public class LoginErrorJsonModel extends BaseResponseJsonModel {
	public String username;
	public String password;
	public String non_field_errors;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return gson.fromJson(plainJson, LoginErrorJsonModel.class);
	}
	
	
}
