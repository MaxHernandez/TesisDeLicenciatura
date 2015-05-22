package com.maxkalavera.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.maxkalavera.utils.database.productmodel.ProductModel;
import com.maxkalavera.utils.database.sqlitehelpers.ProductCacheSQLiteHelper;

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

public class ProductCacheDAO {
	private Context context;
	private SQLiteDatabase database;
	private ProductCacheSQLiteHelper dbHelper;
	
	private static final double RETAINING_LAPSE = 604800;
	private static final int MAX_PRODUCTS = 300;
	private static final String TABLE_IMAGE_ABSPATH = 
			"/."+ ProductCacheSQLiteHelper.TABLE_PRODUCTS +
			"/."+ ProductCacheSQLiteHelper.PRODUCT_IMAGE;
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	public ProductCacheDAO (Context context) {
		this.context = context;
		this.dbHelper = new ProductCacheSQLiteHelper(context);
	}
	
	private Context getContext() {
		return this.context;
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
	 * Metodo para agregar un producto al cache
	 ********************************************************/
	public ProductModel addProduct(ProductModel product) {
		if (product.getCacheID() != -1) {
			if (getNumberOfProductsInDB() > ProductCacheDAO.MAX_PRODUCTS) 
				removeOldestProduct();
			
			ContentValues contentValues = 
					this.getContentValuesFromProduct(product);
			long productID = this.database.insert(ProductCacheSQLiteHelper.TABLE_PRODUCTS, null,
					contentValues);
			product.setCacheID(productID);
			
			// Guarda las imagenes en la memoria SD del dispositivo, 
			// utiliza el ID que se genera al insertar la fila por primera vez
			// para generar el nombre de la imagen.
			this.addImagesToProductDatabaseRow(product, productID);
		}
		
		return product;
	}
	
	/********************************************************
	 * Metodo para verificar si existe la informacion del 
	 * producto en el cache
	 ********************************************************/
	public ProductModel searchProductInCache(ProductModel product) {
		if (product.generalID == "")
			return product;
		
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductCacheSQLiteHelper.TABLE_PRODUCTS +
				" where "+ ProductCacheSQLiteHelper.PRODUCT_GENERALID +" = ?", 
				new String[] {product.generalID});
		
		if (cursor .moveToFirst()) {
			this.retrieveProductFromCursor(cursor, product);
			return product;
		}
		
		return product;
	}
	
	/********************************************************
	 * Metodo para eliminar el producto mas antiguo del cache
	 ********************************************************/
	private void removeFromCursor(Cursor cursor) {
		long productId = cursor.getLong(0);
		
		this.removeImagesInDatabaseFromCursor(cursor);			
		this.database.delete(ProductCacheSQLiteHelper.TABLE_PRODUCTS,
				ProductCacheSQLiteHelper.PRODUCT_ID +
				"=" + String.valueOf(productId)
				, null);
		
		ProductInfoCacheDAO productInfoCache = new ProductInfoCacheDAO(this.getContext());
		productInfoCache.removeProductInfo(productId);
	}
	
	public void removeOldestProduct() {
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductCacheSQLiteHelper.TABLE_PRODUCTS +
				"order by "+ ProductCacheSQLiteHelper.PRODUCT_TIMESTAMP  +" asc" +
				"limit 1", null);
		
		if (cursor .moveToFirst()) {
			this.removeFromCursor(cursor);

    		
        }
	}
	
	public double getNumberOfProductsInDB() {
		return DatabaseUtils.queryNumEntries(this.database, ProductCacheSQLiteHelper.TABLE_PRODUCTS);
	}
	
	/********************************************************
	 * Metodo que sirve para remover de la base de datos los 
	 * productos que han estado demasiado tiempo almacenados.
	 ********************************************************/
	public void removeOldProducts() {
		double now = unix_timestamp();
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductCacheSQLiteHelper.TABLE_PRODUCTS, null);
		if (cursor .moveToFirst()) {			
            while (cursor.isAfterLast() == false) {
            	double productInsertionTime = 
            			this.getInsertionTimeFromCursor(cursor);
            	if (now - productInsertionTime >= ProductCacheDAO.RETAINING_LAPSE)
            		this.removeFromCursor(cursor);
                cursor.moveToNext();
            }
        }
	}
	
	/********************************************************
	 * Metodo para obtener los ultimos "n" productos agregados
	 ********************************************************/
	public List<ProductModel> getLastProducts(int n) {
		Cursor cursor = this.dbHelper.getReadableDatabase().
				rawQuery("select * from "+ ProductCacheSQLiteHelper.TABLE_PRODUCTS +
				"order by "+ ProductCacheSQLiteHelper.PRODUCT_TIMESTAMP  +" desc" +
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
	 * 
	 ********************************************************/
	private ProductModel retrieveProductFromCursor (Cursor cursor, ProductModel product) {
		if (product == null)
			product = new ProductModel();
		product.setCacheID(cursor.getLong(0));
		product.name = cursor.getString(1);
		product.description = cursor.getString(2);
		product.shopingService = cursor.getString(3);
		product.url = cursor.getString(4);
		product.image = this.loadBitmapFile(cursor.getString(5));
		product.imageURL = cursor.getString(6);
		product.generalID = cursor.getString(7);
		
		return product;
	}
	
	private double getInsertionTimeFromCursor(Cursor cursor) {
		return cursor.getDouble(7);
		
	}
	
	private ProductModel retrieveProductFromCursor (Cursor cursor) {
		return retrieveProductFromCursor(cursor, null);
	}
	
	
	/********************************************************
	 * 
	 ********************************************************/
	private ContentValues getContentValuesFromProduct (ProductModel product) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_NAME, product.name);
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_DESCRIPTION, product.description);
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_SHOPINGSERVICE, product.shopingService);
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_URL, product.url);
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_IMAGEURL, product.imageURL);
		contentValues.put(ProductCacheSQLiteHelper.PRODUCT_GENERALID, product.generalID);
		contentValues.put(
				ProductCacheSQLiteHelper.PRODUCT_TIMESTAMP, 
				unix_timestamp());
		//contentValues.put(CacheProductsSQLiteHelper.PRODUCTS_IMAGE, 
				//this.saveBitmap(product.image));
    
		return contentValues;
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	private void addImagesToProductDatabaseRow (ProductModel product, long productID) {
		ContentValues updateContentValues = new ContentValues();
		String bitmapSavedPath = "";
		
		bitmapSavedPath = this.saveBitmapFile(product.image,
				ProductCacheDAO.TABLE_IMAGE_ABSPATH,
				ProductCacheSQLiteHelper.PRODUCT_IMAGE,
				productID);
		updateContentValues.put(ProductCacheSQLiteHelper.PRODUCT_IMAGE, 
		bitmapSavedPath);
		
		this.database.update(ProductCacheSQLiteHelper.TABLE_PRODUCTS, 
				updateContentValues, 
				"_id "+"="+productID, 
				null);
	}

	private void removeImagesInDatabaseFromCursor (Cursor cursor) {
		this.removeBitmapFile(cursor.getString(5));
	}
	
	
	/********************************************************
	 * 
	 ********************************************************/
	private double unix_timestamp() {
		return System.currentTimeMillis() / 1000.0;
	}
	
	/********************************************************
	 * Metodos para guardar y cargar una imagen de la memoria
	 ********************************************************/
	private String saveBitmapFile (Bitmap bitmap, String absolutePath, String filename, long _id) {
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
	
	private Bitmap loadBitmapFile (String bitmapPath) {
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
	
	private Boolean removeBitmapFile (String bitmapPath) {
		File file = new File(bitmapPath);
		return file.delete();
	}
	
}
