package com.maxkalavera.utils.database.jsonmodels;

import java.util.Calendar;

import com.google.gson.Gson;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorsJsonModel;

public class SignUpErrorsJsonModel implements BaseResponseJsonModel {
	public String username;
	public String first_name;
	public String last_name;
	public String email;
	public String gender;
	public String birthdate;
	public String password;
	public String password_confirmation;
	public String non_field_errors;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, LoginErrorsJsonModel.class);
	}

}
