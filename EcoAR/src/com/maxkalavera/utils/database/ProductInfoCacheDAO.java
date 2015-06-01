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
import com.maxkalavera.utils.database.productmodel.ProductInfoModel;
import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.productmodel.UsersScoreModel;
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

public class ProductInfoCacheDAO {
	private Context context;
	private SQLiteDatabase database;
	private ProductInfoCacheSQLiteHelper dbHelper;
	
	/***
	 * NOTAS:
	 * 
	 */
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	public ProductInfoCacheDAO (Context context) {
		this.context = context;
		this.dbHelper = new ProductInfoCacheSQLiteHelper(context);
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
	public ProductInfoModel addProductInfo(ProductInfoModel productInfo) {	
		ContentValues contentValues = 
				this.getContentValuesFromProduct(productInfo);
		//long productID = 
		this.database.insert(
				ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO, null,
				contentValues);
		//productInfo.setId(productID);			
		
		return productInfo;
	}
	
	/********************************************************
	 * 
	 ********************************************************/	
	public void updateScoreOnProduct(long product_reference, UsersScoreModel usersScore) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_USERS_SCORE, usersScore.usersScore);
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_OWN_SCORE, usersScore.ownScore);

		this.database.update(ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO,
				contentValues,
				ProductInfoCacheSQLiteHelper.PRODUCTINFO_PRODUCT_REFERENCE +
				"="+String.valueOf(product_reference)
				, null);
	}
	
	public void updateScoreOnProduct(ProductModel product, UsersScoreModel usersScore) {
		updateScoreOnProduct(product.getCacheId(), usersScore);
	}
	
	/********************************************************
	 * 
	 ********************************************************/	
	public boolean removeProductInfo(long product_reference) {
		// Elimina el producto de la base de datos
		int temp = this.database.delete(ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO,
				ProductInfoCacheSQLiteHelper.PRODUCTINFO_PRODUCT_REFERENCE +
				"="+String.valueOf(product_reference)
				, null);
		
		if (temp > 0) 
			return true;
		return false;
		
	}
	
	public boolean removeProductInfo(ProductModel product) {
		return removeProductInfo(product.getCacheId());
	}
	
	/********************************************************
	 * Metodo para verificar si existe la informacion del 
	 * producto en el cache
	 ********************************************************/
	public ProductInfoModel getProductInfoFromCache(long product_reference) {
		ProductInfoModel productInfo = null;
		
		// Si se almancenan imagenes del producto se eliminan estas primero
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductInfoCacheSQLiteHelper.TABLE_PRODUCTINFO +
				" where "+ ProductInfoCacheSQLiteHelper.PRODUCTINFO_PRODUCT_REFERENCE +" = ?", 
				new String[] {String.valueOf(product_reference)});
				
		if (cursor .moveToFirst()) {
			productInfo = this.retrieveProductFromCursor(cursor);
		}
		
		return productInfo;		
	}
	
	public ProductInfoModel getProductInfoFromCache(ProductModel product) {
		return getProductInfoFromCache(product.getCacheId());
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
	public ProductInfoModel retrieveProductFromCursor (Cursor cursor, ProductInfoModel productInfo) {
		if (productInfo == null)
			productInfo = new ProductInfoModel();
		if (productInfo.usersScore == null)
			productInfo.usersScore = new UsersScoreModel();
		
		//productInfo.setId(cursor.getLong(0));
		productInfo.setProductReference(cursor.getLong(0));
		productInfo.ecologicalScore = cursor.getFloat(1);
		productInfo.usersScore.usersScore = cursor.getFloat(2);
		productInfo.usersScore.ownScore = cursor.getFloat(3);

		
		return productInfo;
	}
	
	public ProductInfoModel retrieveProductFromCursor (Cursor cursor) {
		return retrieveProductFromCursor(cursor, null);
	}
	
	public ContentValues getContentValuesFromProduct (ProductInfoModel productInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_PRODUCT_REFERENCE, productInfo.getProductReference());
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_ECOLOGICAL_SCORE, productInfo.ecologicalScore);
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_USERS_SCORE, productInfo.usersScore.usersScore);
		contentValues.put(ProductInfoCacheSQLiteHelper.PRODUCTINFO_OWN_SCORE, productInfo.usersScore.ownScore);
		return contentValues;
	}
}
