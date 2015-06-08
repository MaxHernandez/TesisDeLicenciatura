package com.maxkalavera.utils.database.sqlitehelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductInfoCacheSQLiteHelper extends SQLiteOpenHelper {
	// Variables de configuración de la base de datos 
	private static final String DATABASE_NAME = 
			ProductCacheSQLiteHelper.DATABASE_NAME;
	private static final int DATABASE_VERSION = 
			ProductCacheSQLiteHelper.DATABASE_VERSION;
	
	/********************************************************
	 * Definicion de los campos de la tabla de productos
	 ********************************************************/
	
	public static final String TABLE_PRODUCTINFO = "productinfo_table";
	//public static final String PRODUCTINFO_ID = "_id";
	public static final String PRODUCTINFO_PRODUCT_REFERENCE = "product_id";
	public static final String PRODUCTINFO_ECOLOGICAL_SCORE = "ecological_score";
	public static final String PRODUCTINFO_USERS_SCORE = "users_score";
	public static final String PRODUCTINFO_OWN_SCORE = "own_score";
	
	// La instrucción de SQL para crear la tabla se define en 
	// la siguiente variable.
	// El ultimo campo siempre se cierra con ");", si se llega a poner 
	// una coma despues del campo generara un error al crear la tabla.  
	public static final String DATABASE_CREATE = 
			"create table " + TABLE_PRODUCTINFO + "(" 
			//+ PRODUCTINFO_ID + " integer primary key autoincrement, " 
			+ PRODUCTINFO_PRODUCT_REFERENCE + " " + "integer not null, "
			+ PRODUCTINFO_ECOLOGICAL_SCORE + " " + "real not null, "
			+ PRODUCTINFO_USERS_SCORE + " " + "real not null, "
			+ PRODUCTINFO_OWN_SCORE + " " + "integer not null"
			+ ");";   

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	public ProductInfoCacheSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(ProductCacheSQLiteHelper.DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE);
		database.execSQL(CommentariesCacheSQLiteHelper.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ProductCacheSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTINFO);
		onCreate(db);
	} 
	
}
