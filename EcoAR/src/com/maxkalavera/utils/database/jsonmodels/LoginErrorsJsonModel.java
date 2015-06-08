package com.maxkalavera.utils.database.jsonmodels;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;

public class LoginErrorsJsonModel implements BaseResponseJsonModel {
	public List<String> username;
	public List<String> password;
	public List<String> non_field_errors;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, LoginErrorsJsonModel.class);
	}
	
	
}
