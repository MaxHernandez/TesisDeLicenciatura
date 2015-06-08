package com.maxkalavera.utils.database.jsonmodels;

import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

public class SignUpErrorsJsonModel implements BaseResponseJsonModel {
	public List<String> username;
	public List<String> first_name;
	public List<String> last_name;
	public List<String> email;
	public List<String> gender;
	public List<String> birthdate;
	public List<String> password;
	public List<String> password_confirmation;
	public List<String> non_field_errors;
	
	@Override
	public BaseResponseJsonModel deserialize(String plainJson) {
        Gson gson = new Gson();
        return (BaseResponseJsonModel) gson.fromJson(plainJson, SignUpErrorsJsonModel.class);
	}

}
