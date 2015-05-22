package com.maxkalavera.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.ecoar.productinfo.ProductInfo;
import com.maxkalavera.utils.database.productmodel.CommentModel;
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
import com.maxkalavera.utils.database.sqlitehelpers.CommentariesCacheSQLiteHelper;
import com.maxkalavera.utils.database.sqlitehelpers.ProductCacheSQLiteHelper;
import com.maxkalavera.utils.database.sqlitehelpers.GroceriesListSQLiteHelper;
import com.maxkalavera.utils.database.sqlitehelpers.ProductInfoCacheSQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommentariesCacheDAO {
	private Context context;
	private SQLiteDatabase database;
	private CommentariesCacheSQLiteHelper dbHelper;
	
	private static final int PAGE_SIZE = 6;
	
	/***
	 * NOTAS:
	 * 
	 */
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	public CommentariesCacheDAO (Context context) {
		this.context = context;
		this.dbHelper = new CommentariesCacheSQLiteHelper(context);
	}
	
	/********************************************************
	 * Metodos para abrir y cerrar 
	 ********************************************************/
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
		
	/********************************************************
	 * 
	 ********************************************************/
	public CommentModel addComment(CommentModel comment) {	
		ContentValues contentValues = 
				this.getContentValuesFromProduct(comment);
		//long productID = 
		this.database.insert(
				CommentariesCacheSQLiteHelper.TABLE_COMMENTERIES, null,
				contentValues);
		//productInfo.setId(productID);			
		
		return comment;
	}
	
	/********************************************************
	 * 
	 ********************************************************/	
	public boolean removeCoommentariesOf(long product_reference) {
		// Elimina el producto de la base de datos
		int temp = this.database.delete(CommentariesCacheSQLiteHelper.TABLE_COMMENTERIES,
				CommentariesCacheSQLiteHelper.COMMENT_PRODUCT_REFERENCE +
				"="+String.valueOf(product_reference)
				, null);
		
		if (temp > 0) 
			return true;
		return false;
		
	}
	
	public boolean removeCoommentariesOf(ProductModel product) {
		return removeCoommentariesOf(product.getCacheID());
	}
	
	/********************************************************
	 * Metodo para verificar si existe la informacion del 
	 * producto en el cache
	 ********************************************************/
	public List<CommentModel> getCommentariesFromCache(long product_reference, int offset) {
		ProductInfoModel productInfo = null;
		
		// Si se almancenan imagenes del producto se eliminan estas primero
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO +
				" where "+ ProductInfoCacheSQLiteHelper.PRODUCTINFO_PRODUCT_REFERENCE +" = ? " +
				"limit " + String.valueOf(this.PAGE_SIZE) + " offset " + String.valueOf(this.PAGE_SIZE), 
				new String[] {String.valueOf(product_reference)});		

		if (cursor .moveToFirst()) {
			List<CommentModel> cmmentaries = new ArrayList<CommentModel>();
			
            while (cursor.isAfterLast() == false) {
            	cmmentaries.add(retrieveProductFromCursor(cursor));
                cursor.moveToNext();
            }
            return cmmentaries;
        }
		
		return null;		
	}
	
	public List<CommentModel> getCommentariesFromCache(ProductModel product, int offset) {
		return getCommentariesFromCache(product.getCacheID(), offset);
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public void removeAllProducts() {
		this.database.delete(ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO,
				// Enviar null en este argumento hace que se eliminen todas los elemntos de la base de datos
				null,  
				null);
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public CommentModel retrieveProductFromCursor (Cursor cursor, CommentModel comment) {
		if (comment == null)
			comment = new CommentModel();

		comment.setProductReference(cursor.getLong(1));
		comment.body = cursor.getString(2);
		comment.username = cursor.getString(3);
		comment.setDateFromString(cursor.getString(4));
		
		return comment;
	}
	
	public CommentModel retrieveProductFromCursor (Cursor cursor) {
		return retrieveProductFromCursor(cursor, null);
	}
	
	public ContentValues getContentValuesFromProduct (CommentModel comment) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CommentariesCacheSQLiteHelper.COMMENT_PRODUCT_REFERENCE, comment.getProductReference());
		contentValues.put(CommentariesCacheSQLiteHelper.COMMENT_BODY, comment.body);
		contentValues.put(CommentariesCacheSQLiteHelper.COMMENT_USERNAME, comment.username);
		contentValues.put(CommentariesCacheSQLiteHelper.COMMENT_POSTING_DATE, comment.getDateAsString());
		return contentValues;
	}
}
