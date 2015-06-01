package com.maxkalavera.utils.database.jsonmodels;
import com.google.gson.Gson;

public class LoginErrorsJsonModel implements BaseResponseJsonModel {
	public String username;
	public String password;
	public String non_field_errors;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, LoginErrorsJsonModel.class);
	}
	
	
}
