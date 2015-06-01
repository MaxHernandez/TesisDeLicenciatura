package com.maxkalavera.utils.database.sqlitehelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CommentariesCacheSQLiteHelper extends SQLiteOpenHelper {
	// Variables de configuración de la base de datos 
	private static final String DATABASE_NAME = "cacheproducts.db";
	private static final int DATABASE_VERSION = 
			ProductCacheSQLiteHelper.DATABASE_VERSION;
	
	/********************************************************
	 * Definicion de los campos de la tabla de productos
	 ********************************************************/
	
	public static final String TABLE_COMMENTERIES = "commentaries_table";
	public static final String COMMENT_ID = "_id";
	public static final String COMMENT_PRODUCT_REFERENCE = "product_id";
	public static final String COMMENT_BODY = "body";
	public static final String COMMENT_USERNAME = "username";
	public static final String COMMENT_POSTING_DATE = "posting_date";	
	public static final String COMMENT_SERVER_ID = "server_id";
	
	// La instrucción de SQL para crear la tabla se define en 
	// la siguiente variable.
	// El ultimo campo siempre se cierra con ");", si se llega a poner 
	// una coma despues del campo generara un error al crear la tabla.  
	private static final String DATABASE_CREATE = 
			"create table " + TABLE_COMMENTERIES + "("
			+ COMMENT_ID + " integer primary key autoincrement, " 
			+ COMMENT_PRODUCT_REFERENCE + "integer not null, "
			+ COMMENT_BODY + "text not null, "
			+ COMMENT_USERNAME + "text not null, "
			+ COMMENT_POSTING_DATE + "text not null, "
			+ COMMENT_SERVER_ID + "integer not null "
			+ ");";   

	/********************************************************
	 * Metodo constructor de la clase
	 ********************************************************/
	public CommentariesCacheSQLiteHelper(Context context) {
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
		Log.w(ProductCacheSQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTERIES);
		onCreate(db);
	} 
	
}
