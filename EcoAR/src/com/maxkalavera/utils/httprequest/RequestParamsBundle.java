package com.maxkalavera.utils.httprequest;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.maxkalavera.utils.database.jsonmodels.BaseRequestJsonModel;

public class RequestParamsBundle {
	private HashMap<String, String> uriParamsMap;
	private HashMap<String, String> jsonParamsMap;
	private HashMap<String, BaseRequestJsonModel> jsonModelMap;
	private List<RawDataModel> rawDataParamsList;
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public RequestParamsBundle() {
		this.uriParamsMap = null;
		this.jsonParamsMap = null;
		this.jsonModelMap = null;
		this.rawDataParamsList = null;
	}	

	/************************************************************
	 * URI params
	 ************************************************************/
	public HashMap<String, String> getURIParamsMap() {
		return this.uriParamsMap;
	}
	
	public void addUriParam(String name, String value) {		
		if (this.uriParamsMap == null)
			this.uriParamsMap = new HashMap<String, String>();
		
		this.uriParamsMap.put(name, value);
	}

	/************************************************************
	 * JSON params
	 ************************************************************/

	public HashMap<String, String> getJSONParamsMap() {
		return this.jsonParamsMap;
	}
	
	public void addJsonParam(String name, String value) {		
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
	//public void AddJsonModel(BaseRequestJsonModel value) {		
		if (this.jsonModelMap == null)
			this.jsonModelMap = new HashMap<String, BaseRequestJsonModel>();
		this.jsonModelMap.put(name, value);
		//this.jsonModelMap.put("", value);
	}
	
	/************************************************************
	 * Raw data
	 ************************************************************/
	public List<RawDataModel> getRawDataList() {
		return this.rawDataParamsList;
	}
	
	public void addPart(byte[] data, String mediatype, String name, String filename) {		
		if (this.rawDataParamsList == null)
			this.rawDataParamsList = new ArrayList<RawDataModel>();
		this.rawDataParamsList.add(new RawDataModel(data, mediatype, name, filename));
		
	}
};
