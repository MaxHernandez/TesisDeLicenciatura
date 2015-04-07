package com.maxkalavera.utils.httprequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.maxkalavera.utils.database.jsonmodels.BaseRequestJsonModel;

public class RequestParamsBundle {
	private HashMap<String, String> uriParamsMap;
	private HashMap<String, String> jsonParamsMap;
	private HashMap<String, BaseRequestJsonModel> jsonModelMap;
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public RequestParamsBundle() {
		this.uriParamsMap = null;
		this.jsonParamsMap = null;
		this.jsonModelMap = null;
	}	

	/************************************************************
	 * URI params
	 ************************************************************/
	public HashMap<String, String> getURIParamsMap() {
		return this.uriParamsMap;
	}
	
	public void addURIParam(String name, String value) {
		if (this.uriParamsMap == null)
			this.uriParamsMap = new HashMap<String, String>();
		this.uriParamsMap.put(name, value);
	}

	/************************************************************
	 * JSON params
	 ************************************************************/

	public HashMap<String, String> getJSONParamsMap() {
		return this.uriParamsMap;
	}
	
	public void addJSONParam(String name, String value) {
		if (this.jsonParamsMap == null)
			this.jsonParamsMap = new HashMap<String, String>();
		this.jsonParamsMap.put(name, value);
	}

	/************************************************************
	 * JSON Objects
	 ************************************************************/
	public HashMap<String, BaseRequestJsonModel> getJsonModelMap() {
		return this.jsonModelMap;
	}
		
	public void AddJsonModel(String name, BaseRequestJsonModel value) {
		if (this.jsonModelMap == null)
			this.jsonModelMap = new HashMap<String, BaseRequestJsonModel>();
		this.jsonModelMap.put(name, value);
	}
}
