package com.database;

import java.util.ArrayList;
import java.util.List;

import com.shawbeat.Category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryDBHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "categoryDB.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + CategoryTable.TABLE_NAME + " (" + 
    	    CategoryTable._ID + " INTEGER PRIMARY KEY," +
    	    CategoryTable.COLUMN_NAME_EDITION_ID + INT_TYPE + COMMA_SEP +
    	    CategoryTable.COLUMN_NAME_CATEGORY_ID + INT_TYPE + COMMA_SEP + 
    	    CategoryTable.COLUMN_NAME_CATEGORY_NAME + TEXT_TYPE + 
    	    " )";
    private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME;
    
    public CategoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
	}
	
	// Since the number of categories may increase or decrease, here I delete the old categories per edition then insert again
	// Actually we can use "insert or replace" for this with pure SQL command, the purpose of below code is just for practice on android db method
	public void insertOrUpdateCategory(List<Category> category, int ed_id){
		Log.i("log_tag", "insertOrUpdateCategory");
		SQLiteDatabase database = this.getWritableDatabase();
		try{
			// Define 'where' part of query.
			String selection = CategoryTable.COLUMN_NAME_EDITION_ID + " =?";
			// Specify arguments in placeholder order.
			String[] selectionArgs = { String.valueOf(ed_id) };
			// Issue SQL statement.
			int dd = database.delete(CategoryTable.TABLE_NAME, selection, selectionArgs); //delete the Category per edition first
			Log.i("log_tag", "dd="+String.valueOf(dd));
			
			int size = category.size();
			
			for(int i=0;i<size;i++){
				ContentValues values = new ContentValues();
				values.put(CategoryTable.COLUMN_NAME_EDITION_ID, category.get(i).getEdition_ID());
				values.put(CategoryTable.COLUMN_NAME_CATEGORY_ID, category.get(i).getCategory_id());
				values.put(CategoryTable.COLUMN_NAME_CATEGORY_NAME, category.get(i).getCategory_name());
				
				//insert return the new row id
				long newRowId = database.insert(CategoryTable.TABLE_NAME, null, values);
				Log.i("log_tag", String.valueOf(newRowId));
			}
		}catch(Exception e){
			Log.i("log_tag", "insertOrUpdateCategory Error : "+e.toString());
		}
		database.close();
	}
	
	public List<Category> getCategoryByEditionID(int ed_id){
		Log.i("log_tag", "getAllCategory");
		SQLiteDatabase database = this.getReadableDatabase();
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			CategoryTable.COLUMN_NAME_EDITION_ID,
			CategoryTable.COLUMN_NAME_CATEGORY_ID,
			CategoryTable.COLUMN_NAME_CATEGORY_NAME
		};
		
		// Define 'where' part of query.
				String selection = ArticleTable.COLUMN_NAME_EDITION_ID + " =?";
				// Specify arguments in placeholder order.
				String[] selectionArgs = { String.valueOf(ed_id) };
				// Issue SQL statement.
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder = CategoryTable._ID + " ASC"; //ASC|DESC
		
		try{
			Cursor c = database.query(
				CategoryTable.TABLE_NAME,  // The table to query
			    projection,                               // The columns to return
			    selection,                                // The columns for the WHERE clause
			    selectionArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    sortOrder                                 // The sort order
			);
			
			if (c!=null){
				c.moveToFirst();
				Log.i("log_tag", "c.getCount() : "+String.valueOf(c.getCount()));
			}else
				Log.i("log_tag", "cursor null");
			
			List<Category> result = new ArrayList<Category>();
			do{
				Category temp = new Category();
				temp.setEdition_ID(c.getInt(c.getColumnIndexOrThrow(CategoryTable.COLUMN_NAME_EDITION_ID)));
	            temp.setCategory_id(c.getInt(c.getColumnIndexOrThrow(CategoryTable.COLUMN_NAME_CATEGORY_ID)));
	            temp.setCategory_name(c.getString(c.getColumnIndexOrThrow(CategoryTable.COLUMN_NAME_CATEGORY_NAME)));
	            result.add(temp);
			}while(c.moveToNext());
			
			c.close();
			database.close();
			return result;
		}catch(Exception e){
			database.close();
			Log.i("log_tag", "getAllCategory Error : "+e.toString());
			return null;
		}
	}
}
