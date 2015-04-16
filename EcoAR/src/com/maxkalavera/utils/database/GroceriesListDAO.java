package com.maxkalavera.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.maxkalavera.utils.database.models.ProductModel;
import com.maxkalavera.utils.database.sqlitehelpers.CacheProductsSQLiteHelper;
import com.maxkalavera.utils.database.sqlitehelpers.GroceriesListSQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GroceriesListDAO {
	private Context context;
	private SQLiteDatabase database;
	private CacheProductsSQLiteHelper dbHelper;
	
	private static final String TABLE_IMAGE_ABSPATH = 
			"/."+ GroceriesListSQLiteHelper.TABLE_GROSERIESLIST +
			"/."+ GroceriesListSQLiteHelper.PRODUCTS_IMAGE;
	
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	GroceriesListDAO (Context context) {
		this.context = context;
		this.dbHelper = new CacheProductsSQLiteHelper(context);
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
	
	public ProductModel addProduct(ProductModel product) {			
		if (product.getID() != -1) {			
			ContentValues contentValues = 
					this.getContentValuesFromProduct(product);
			long productID = this.database.insert(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST, null,
					contentValues);
			product.setID(productID);
			
			// Guarda las imagenes en la memoria SD del dispositivo, 
			// utiliza el ID que se genera al insertar la fila por primera vez
			// para generar el nombre de la imagen.
			this.addImagesToProductDatabaseRow(product, productID);
		}
		
		return product;
	}
	
	public Boolean removeProduct(ProductModel product) {
		if (product.getID() == -1)
			return false;
		
		// Si se almancenan imagenes del producto se eliminan estas primero
		Cursor cursor =  this.database.rawQuery("select * from "+ 
				GroceriesListSQLiteHelper.TABLE_GROSERIESLIST + 
				" where " + GroceriesListSQLiteHelper.PRODUCTS_ID + "=" + product.getID()  ,
				null);		
		if (cursor.moveToFirst()) {
			this.removeImagesInDatabaseFromCursor(cursor);
		}else {
			return false;
		}
		
		// Elimina el producto de la base de datos
		this.database.delete(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST,
				GroceriesListSQLiteHelper.PRODUCTS_ID+
				"="+String.valueOf(product.getID())
				, null);
		
		return false;
	}
	
	public void removeAllProducts() {
		this.database.delete(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST,
				// Enviar null en este argumento hace que se eliminen todas los elemntos de la base de datos
				null,  
				null);
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public ProductModel retrieveProductFromCursor (Cursor cursor, ProductModel product) {
		if (product == null)
			product = new ProductModel();
		product.setID(cursor.getLong(0));
		product.name = cursor.getString(1);
		product.description = cursor.getString(2);
		product.shopingService = cursor.getString(3);
		product.url = cursor.getString(4);
		product.image = this.loadBitmapFile(cursor.getString(5));
		product.imageURL = cursor.getString(6);
		
		return product;
	}
	
	public ProductModel retrieveProductFromCursor (Cursor cursor) {
		return retrieveProductFromCursor(cursor, null);
	}
	
	
	/********************************************************
	 * 
	 ********************************************************/
	public ContentValues getContentValuesFromProduct (ProductModel product) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(GroceriesListSQLiteHelper.PRODUCTS_NAME, product.name);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCTS_DESCRIPTION, product.description);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCTS_SHOPINGSERVICE, product.shopingService);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCTS_URL, product.url);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCTS_IMAGEURL, product.imageURL);    
		return contentValues;
	}

	/********************************************************
	 * 
	 ********************************************************/
	public void addImagesToProductDatabaseRow (ProductModel product, long productID) {
		ContentValues updateContentValues = new ContentValues();
		String bitmapSavedPath = "";
		
		bitmapSavedPath = this.saveBitmapFile(product.image,
				GroceriesListDAO.TABLE_IMAGE_ABSPATH,
				GroceriesListSQLiteHelper.PRODUCTS_IMAGE,
				productID);
		updateContentValues.put(GroceriesListSQLiteHelper.PRODUCTS_IMAGE, 
		bitmapSavedPath);
		
		this.database.update(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST, 
				updateContentValues, 
				"_id "+"="+productID, 
				null);
	}

	public void removeImagesInDatabaseFromCursor (Cursor cursor) {
		this.removeBitmapFile(cursor.getString(5));
	}
	
	/********************************************************
	 * Metodos para guardar y cargar una imagen de la memoria
	 ********************************************************/
	public String saveBitmapFile (Bitmap bitmap, String absolutePath, String filename, long _id) {
		String rootDirectory = this.context.getExternalFilesDir(null).getAbsolutePath();
		String filename_extension = ".jpeg";
		String bitmapPath = rootDirectory+absolutePath+"/"+filename+String.valueOf(_id)+"."+filename_extension;
		
		File dir = new File(rootDirectory+absolutePath+absolutePath+"/");
		if(!dir.exists()) {
		    dir.mkdirs();
		}
		File file = new File(bitmapPath);
		
		 try {
			 OutputStream fileOutputStream = new FileOutputStream(file);
			 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			 fileOutputStream.flush();
	         fileOutputStream.close();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
			 return null;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		
		return bitmapPath;
	}
	
	public Bitmap loadBitmapFile (String bitmapPath) {
		Bitmap bitmap = null;
		File file = new File(bitmapPath);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fileInputStream); //This gets the image
				fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
	
	public Boolean removeBitmapFile (String bitmapPath) {
		File file = new File(bitmapPath);
		return file.delete();
	}
}
