package com.maxkalavera.utils.database.jsonmodels;

import java.lang.reflect.Method;

import com.google.gson.Gson;

public abstract class BaseResponseJsonModel {

	public abstract BaseResponseJsonModel deserialize( String plainJson);
	
};