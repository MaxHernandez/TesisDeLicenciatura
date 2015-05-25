package com.maxkalavera.utils.httprequest;

public class RawDataModel {
	
	public byte[] data;
	public String mediatype;
	public String name;
	public String filename;
	
	RawDataModel(byte[] data, String mediatype, String name, String filename) {
		this.data = data;
		this.mediatype = mediatype;
		this.name = name;
		this.filename = filename;
	}
};
