package com.maxkalavera.utils.database.sqlitehelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductCacheSQLiteHelper extends SQLiteOpenHelper {
	// Variables de configuración de la base de datos 
	public static final String DATABASE_NAME = "cacheproducts.db";
	public static final int DATABASE_VERSION = 1;
	
	/********************************************************
	 * Definicion de los campos de la tabla de productos
	 ********************************************************/
	
	public static final String TABLE_PRODUCTS = "product_table";
	
	public static final String PRODUCT_ID = "_id";
	public static final int PRODUCT_ID_INDEX = 0;
	
	public static final String PRODUCT_NAME = "name";
	public static final int PRODUCT_NAME_INDEX = 1;
	
	public static final String PRODUCT_DESCRIPTION = "description";
	public static final int PRODUCT_DESCRIPTION_INDEX = 2;
	
	public static final String PRODUCT_SHOPINGSERVICE = "shopingservice";
	public static final int PRODUCT_SHOPINGSERVICE_INDEX = 3;
	
	public static final String PRODUCT_URL = "url";
	public static final int PRODUCT_URL_INDEX = 4;
	
	public static final String PRODUCT_IMAGE = "image";
	public static final int PRODUCT_IMAGE_INDEX = 5;
	
	public static final String PRODUCT_IMAGEURL = "imageurl";
	public static final int PRODUCT_IMAGEURL_INDEX = 6;
	
	public static final String PRODUCT_GENERALID = "generalid";
	public static final int PRODUCT_GENERALID_INDEX = 7;
	
	public static final String PRODUCT_TIMESTAMP = "date";
	public static final int PRODUCT_TIMESTAMP_INDEX = 8;

	
	
	
	// La instrucción de SQL para crear la tabla se define en 
	// la siguiente variable.
	// El ultimo campo siempre se cierra con ");", si se llega a poner 
	// una coma despues del campo generara un error al crear la tabla.  
	public static final String DATABASE_CREATE = 
			"create table " + TABLE_PRODUCTS + "(" 
			+ PRODUCT_ID + " " + "integer primary key autoincrement, " 
			+ PRODUCT_NAME + " " + "text not null, "
			+ PRODUCT_DESCRIPTION + " " + "text not null, "
			+ PRODUCT_SHOPINGSERVICE + " " + "text not null, "
			+ PRODUCT_URL + " " + "text not null, "
			+ PRODUCT_IMAGE + " " + "text, "
			+ PRODUCT_IMAGEURL + " " + " text,"
			+ PRODUCT_GENERALID + " " + "text not null unique,"
			+ PRODUCT_TIMESTAMP + " " + "integer not null"
			+ ");";   
	
	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	public ProductCacheSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(ProductInfoCacheSQLiteHelper.DATABASE_CREATE);
		database.execSQL(CommentariesCacheSQLiteHelper.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ProductCacheSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	} 
	
}
