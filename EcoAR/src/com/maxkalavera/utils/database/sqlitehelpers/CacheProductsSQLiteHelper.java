package com.maxkalavera.utils.database.sqlitehelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CacheProductsSQLiteHelper extends SQLiteOpenHelper {
	// Variables de configuración de la base de datos 
	private static final String DATABASE_NAME = "cacheproducts.db";
	private static final int DATABASE_VERSION = 1;
	
	/********************************************************
	 * Definicion de los campos de la tabla de productos
	 ********************************************************/
	
	public static final String TABLE_PRODUCTS = "productstable";
	public static final String PRODUCTS_ID = "_id";
	public static final String PRODUCTS_NAME = "name";
	public static final String PRODUCTS_DESCRIPTION = "description";
	public static final String PRODUCTS_SHOPINGSERVICE = "shopingService";
	public static final String PRODUCTS_URL = "url";
	public static final String PRODUCTS_IMAGE = "image";
	public static final String PRODUCTS_IMAGEURL = "imageURL";
	public static final String PRODUCTS_TIMESTAMP = "date";
	
	
	
	// La instrucción de SQL para crear la tabla se define en 
	// la siguiente variable.
	// El ultimo campo siempre se cierra con ");", si se llega a poner 
	// una coma despues del campo generara un error al crear la tabla.  
	private static final String DATABASE_CREATE = 
			"create table " + TABLE_PRODUCTS + "(" 
			+ PRODUCTS_ID + " integer primary key autoincrement, " 
			+ PRODUCTS_NAME + "text not null, "
			+ PRODUCTS_DESCRIPTION + "text not null, "
			+ PRODUCTS_SHOPINGSERVICE + "text not null, "
			+ PRODUCTS_URL + "text not null, "
			+ PRODUCTS_IMAGE + "text, "
			+ PRODUCTS_IMAGEURL + " text,"
			+ PRODUCTS_TIMESTAMP + "integer not null"
			+ ");";   

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	public CacheProductsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CacheProductsSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	} 
	
}
