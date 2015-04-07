package com.maxkalavera.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.utils.database.models.ProductModel;
import com.maxkalavera.utils.database.sqlitehelpers.CacheProductsSQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

public class CacheProductDAO {
	private Context context;
	private SQLiteDatabase database;
	private CacheProductsSQLiteHelper dbHelper;
	private double retainingLapse;
	private int maxProducts;
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	public CacheProductDAO (Context context) {
		this.context = context;
		this.dbHelper = new CacheProductsSQLiteHelper(context);
		this.retainingLapse = 604800; // one weeek in seconds
		this.maxProducts = 300;
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
	 * Metodos para guardar y cargar una imagen de la memoria
	 ********************************************************/
	public String saveBitmap (Bitmap bitmap, String absolutePath, String filename, long _id) {
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
	
	public Bitmap loadBitmap (String bitmapPath) {
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
	
	/********************************************************
	 * Metodo para agregar un producto al cache
	 ********************************************************/
	public ProductModel addProduct(ProductModel product) {
		if (product.getID() == -1) {
			
			if (getNumberOfProductsInDB() > this.maxProducts) 
				removeOldestProduct();
			
			ContentValues contentValues = 
					this.getContentValuesFromProduct(product);
			long tempID = this.database.insert(CacheProductsSQLiteHelper.TABLE_PRODUCTS, null,
					contentValues);
			product.setID(tempID);
			
			// Guarda las imagenes en la memoria SD del dispositivo, 
			// utiliza el ID que se genera al insertar la fila por primera vez
			// para generar el nombre de la imagen.
			ContentValues updateContentValues = new ContentValues();
			String absolutePath = "";
			String bitmapPath = "";
			
			// CacheProductsSQLiteHelper.PRODUCTS_IMAGE
			absolutePath =
					"/."+ CacheProductsSQLiteHelper.TABLE_PRODUCTS +
					"/."+ CacheProductsSQLiteHelper.PRODUCTS_IMAGE;
			bitmapPath = this.saveBitmap(product.image,
					absolutePath,
					CacheProductsSQLiteHelper.PRODUCTS_IMAGE,
					tempID);
			updateContentValues.put(CacheProductsSQLiteHelper.PRODUCTS_IMAGE, 
			bitmapPath);
			
			this.database.update(CacheProductsSQLiteHelper.TABLE_PRODUCTS, 
					updateContentValues, 
					"_id "+"="+tempID, 
					null);
			
		}
		
		return product;
	}
	
	public void removeOldestProduct() {
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ CacheProductsSQLiteHelper.TABLE_PRODUCTS +
				"order by "+ CacheProductsSQLiteHelper.PRODUCTS_TIMESTAMP  +" asc" +
				"limit 1", null);
		if (cursor .moveToFirst()) {
    		this.database.delete(CacheProductsSQLiteHelper.TABLE_PRODUCTS,
    				CacheProductsSQLiteHelper.PRODUCTS_ID+
    				"="+String.valueOf(cursor.getLong(0))
    				, null);
        }
	}
	
	public double getNumberOfProductsInDB() {
		return DatabaseUtils.queryNumEntries(this.database, CacheProductsSQLiteHelper.TABLE_PRODUCTS);
	}
	
	/********************************************************
	 * Metodo para obtener los ultimos "n" productos agregados
	 ********************************************************/
	public List<ProductModel> getLastProducts(int n) {
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ CacheProductsSQLiteHelper.TABLE_PRODUCTS +
				"order by "+ CacheProductsSQLiteHelper.PRODUCTS_TIMESTAMP  +" desc" +
				"limit "+ String.valueOf(n), null);

		if (cursor .moveToFirst()) {
			List<ProductModel> resList = new ArrayList<ProductModel>();
			
            while (cursor.isAfterLast() == false) {
                resList.add(retrieveProductFromCursor(cursor));
                cursor.moveToNext();
            }
            return resList;
        }

		return null;
	}
	
	
	/********************************************************
	 * Metodo para verificar si existe la informacion del 
	 * producto en el cache
	 ********************************************************/
	public boolean isProductInCache(ProductModel product) {
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ CacheProductsSQLiteHelper.TABLE_PRODUCTS +
				" where "+ CacheProductsSQLiteHelper.PRODUCTS_NAME +" = ? and "+ 
				CacheProductsSQLiteHelper.PRODUCTS_SHOPINGSERVICE +" = ? ", 
				new String[] { product.name, product.shopingService });
		
		if (cursor .moveToFirst()) {
			this.retrieveProductFromCursor(cursor, product);
			return true;
		}
		
		return false;		
	}
	
	/********************************************************
	 * Metodo que sirve para remover de la base de datos los 
	 * productos que han estado demasiado tiempo almacenados.
	 ********************************************************/
	public void removeOldProducts() {
		double now = unix_timestamp();
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ CacheProductsSQLiteHelper.TABLE_PRODUCTS, null);
		if (cursor .moveToFirst()) {			
            while (cursor.isAfterLast() == false) {
            	double productInsertionTime = 
            			this.getInsertionTimeFromCursor(cursor);
            	if (now - productInsertionTime >= this.retainingLapse)
            		this.database.delete(CacheProductsSQLiteHelper.TABLE_PRODUCTS,
            				CacheProductsSQLiteHelper.PRODUCTS_ID+
            				"="+String.valueOf(cursor.getLong(0))
            				, null);
                cursor.moveToNext();
            }
        }
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
		product.image = this.loadBitmap(cursor.getString(5));
		product.imageURL = cursor.getString(6);
		
		return product;
	}
	
	public double getInsertionTimeFromCursor(Cursor cursor) {
		return cursor.getDouble(7);
		
	}
	
	public ProductModel retrieveProductFromCursor (Cursor cursor) {
		return retrieveProductFromCursor(cursor, null);
	}
	
	
	/********************************************************
	 * 
	 ********************************************************/
	public ContentValues getContentValuesFromProduct (ProductModel product) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_NAME, product.name);
		contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_DESCRIPTION, product.description);
		contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_SHOPINGSERVICE, product.shopingService);
		contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_URL, product.url);
		contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_IMAGEURL, product.imageURL);
		contentValues.put(
				CacheProductsSQLiteHelper.PRODUCTS_TIMESTAMP, 
				unix_timestamp());
		//contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_IMAGE, 
				//this.saveBitmap(product.image));
    
		return contentValues;
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public double unix_timestamp() {
		return System.currentTimeMillis() / 1000.0;
	}
	
}
