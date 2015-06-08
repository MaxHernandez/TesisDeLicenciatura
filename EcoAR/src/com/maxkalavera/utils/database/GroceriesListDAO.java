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
import android.util.Log;

public class GroceriesListDAO {
	private Context context;
	private SQLiteDatabase database;
	private GroceriesListSQLiteHelper dbHelper;
	
	private static final String TABLE_IMAGE_ABSPATH = 
			"/."+ GroceriesListSQLiteHelper.TABLE_GROSERIESLIST +
			"/."+ GroceriesListSQLiteHelper.PRODUCT_IMAGE;
	
	
	/********************************************************
	 * Metodo constructor
	 ********************************************************/
	public GroceriesListDAO (Context context) {
		this.context = context;
		this.dbHelper = new GroceriesListSQLiteHelper(context);
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
	public List<ProductModel> getAllProducts() {
		List<ProductModel> resList = null;
		
		// Si se almancenan imagenes del producto se eliminan estas primero
		Cursor cursor =  this.database.rawQuery("select * from "+ 
				GroceriesListSQLiteHelper.TABLE_GROSERIESLIST,
				null);		
		if (cursor != null && cursor.moveToFirst()) {
			resList = new ArrayList<ProductModel>();
			
			while (cursor.isAfterLast() == false) {
				resList.add(retrieveProductFromCursor(cursor));
				cursor.moveToNext();
			}
		}
		
		return resList;
	}	

	/********************************************************
	 * 
	 ********************************************************/
	public boolean updateProduct(ProductModel product) {
		if (product == null || product.getGroceriesId() == -1)
			return false;
		
		ContentValues contentValues = 
				this.getContentValuesFromProduct(product);

		this.database.update(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST,
				contentValues,
				GroceriesListSQLiteHelper.PRODUCT_ID + " = " + String.valueOf(product.getGroceriesId()) 
				, null);
		return true;
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public ProductModel addProduct(ProductModel product) {			
		if (product == null || product.getGroceriesId() != -1)
			return product;
		
		ContentValues contentValues = 
			this.getContentValuesFromProduct(product);
		long productId = this.database.insert(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST, null,
			contentValues);
		if (productId != -1) {
			product.setGroceriesId(productId);
			
			// Guarda las imagenes en la memoria SD del dispositivo, 
			// utiliza el ID que se genera al insertar la fila por primera vez
			// para generar el nombre de la imagen.
			this.addImagesToProductDatabaseRow(product, productId);
			}
			
		return product;
	}
	
	/********************************************************
	 * 
	 ********************************************************/
	public ProductModel removeProduct(ProductModel product) {
		if (product == null || product.getGroceriesId() == -1)
			return product;
		
		Cursor cursor = this.database.
				rawQuery("select * from "+ GroceriesListSQLiteHelper.TABLE_GROSERIESLIST +
				" where "+ GroceriesListSQLiteHelper.PRODUCT_ID + " = ?", 
				new String[] {String.valueOf(product.getGroceriesId())});

		if (cursor != null && cursor.moveToFirst()) {		
			this.removeImagesInDatabaseFromCursor(cursor);
		
			// Elimina el producto de la base de datos
			this.database.delete(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST,
				GroceriesListSQLiteHelper.PRODUCT_ID + 
				" = " + String.valueOf(product.getGroceriesId())
				, null);
		
			product.setGroceriesId(-1);
		}
		
		return product;
	}
	
	/********************************************************
	 * Metodo para verificar si existe la informacion del 
	 * producto en el cache
	 ********************************************************/
	public ProductModel isProductInGroceries(ProductModel product) {
		if (product == null || product.generalId == null)
			return product;
		
		// Si se almancenan imagenes del producto se eliminan estas primero
		Cursor cursor = this.database.
				rawQuery("select * from "+ GroceriesListSQLiteHelper.TABLE_GROSERIESLIST +
				" where "+ GroceriesListSQLiteHelper.PRODUCT_GENERALID +" = ?", 
				new String[] {product.generalId});
				
		if (cursor != null && cursor.moveToFirst()) {
			this.retrieveProductFromCursor(cursor, product);
		}
		
		return product;		
	}
	
	/********************************************************
	 * 
	 ********************************************************/
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
		product.setGroceriesId(cursor.getLong(0));
		product.name = cursor.getString(1);
		product.description = cursor.getString(2);
		product.shopingService = cursor.getString(3);
		product.url = cursor.getString(4);
		product.image = this.loadBitmapFile(cursor.getString(5));
		product.imageURL = cursor.getString(6);
		product.generalId = cursor.getString(7);
		product.setChecked( (cursor.getInt(8) == 1) ? true:false );
		product.number_of_products = cursor.getInt(9);
		
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
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_NAME, product.name);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_DESCRIPTION, product.description);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_SHOPINGSERVICE, product.shopingService);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_URL, product.url);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_IMAGEURL, product.imageURL);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_GENERALID, product.generalId);
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_CHECK, (product.isChecked()) ? 1:0 );
		contentValues.put(GroceriesListSQLiteHelper.PRODUCT_NUMBER_OF, product.number_of_products);
		
		return contentValues;
	}

	/********************************************************
	 * 
	 ********************************************************/
	public void addImagesToProductDatabaseRow (ProductModel product, long productID) {
		if (product.image == null) return;
		
		ContentValues updateContentValues = new ContentValues();
		String bitmapSavedPath = "";
		
		bitmapSavedPath = this.saveBitmapFile(product.image,
				GroceriesListDAO.TABLE_IMAGE_ABSPATH,
				GroceriesListSQLiteHelper.PRODUCT_IMAGE,
				productID);
		updateContentValues.put(GroceriesListSQLiteHelper.PRODUCT_IMAGE, 
		bitmapSavedPath);
		
		this.database.update(GroceriesListSQLiteHelper.TABLE_GROSERIESLIST, 
				updateContentValues, 
				"_id "+" = "+productID, 
				null);
	}

	public void removeImagesInDatabaseFromCursor (Cursor cursor) {
		if (cursor.getString(5) == null) return;
		
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
		if (bitmapPath == null) return null;
		
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
