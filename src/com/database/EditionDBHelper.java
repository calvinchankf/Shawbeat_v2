package com.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shawbeat.Edition;

public class EditionDBHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "editionDB.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + EditionTable.TABLE_NAME + " (" + 
    	    EditionTable._ID + " INTEGER PRIMARY KEY," +
    	    EditionTable.COLUMN_NAME_EDITION_ID + INT_TYPE + COMMA_SEP +
    	    EditionTable.COLUMN_NAME_EDITION_NAME + TEXT_TYPE + COMMA_SEP + 
    	    EditionTable.COLUMN_DATE + TEXT_TYPE +
    	    " )";

    private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + EditionTable.TABLE_NAME;
    
	public EditionDBHelper(Context context) {
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
	
	//public void insertEdition(String edition_ID, String edition_name, String date){ //renew in this case
	public void insertEdition(List<Edition> edition){ //renew in this case
		Log.i("log_tag", "insertEdition");
		SQLiteDatabase database = this.getWritableDatabase();
		try{
			database.delete(EditionTable.TABLE_NAME, null, null); //delete all first
			
			int size = edition.size();
			
			for (int i=0;i<size;i++){
				ContentValues values = new ContentValues();
				values.put(EditionTable.COLUMN_NAME_EDITION_ID, edition.get(i).getEdition_ID());
				values.put(EditionTable.COLUMN_NAME_EDITION_NAME, edition.get(i).getEdition_name());
				values.put(EditionTable.COLUMN_DATE, edition.get(i).getDate());

				//insert return the new row id
				long newRowId = database.insert(EditionTable.TABLE_NAME, null, values);
				Log.i("log_tag", String.valueOf(newRowId));
			}
		}catch(Exception e){
			Log.i("log_tag", "insertEdition Error : "+e.toString());
		}
		database.close();
	}
	
	public List<Edition> getAllEdition(){
		Log.i("log_tag", "getAllEdition");
		SQLiteDatabase database = this.getReadableDatabase();
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			EditionTable.COLUMN_NAME_EDITION_ID,
			EditionTable.COLUMN_NAME_EDITION_NAME,
			EditionTable.COLUMN_DATE
		};
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder = EditionTable._ID + " ASC"; //ASC|DESC
		
		try{
			Cursor c = database.query(
				EditionTable.TABLE_NAME,  // The table to query
			    projection,                               // The columns to return
			    null,                                // The columns for the WHERE clause
			    null,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    sortOrder                                 // The sort order
			);
			
			if (c!=null){
				c.moveToFirst();
				Log.i("log_tag", "c.getCount() : "+String.valueOf(c.getCount()));
			}else
				Log.i("log_tag", "cursor null");
			
			List<Edition> result = new ArrayList<Edition>();
			do{
				Edition temp = new Edition();
				temp.setEdition_ID(c.getInt(c.getColumnIndexOrThrow(EditionTable.COLUMN_NAME_EDITION_ID)));
				Log.i("log_tag", c.getString(c.getColumnIndexOrThrow(EditionTable.COLUMN_NAME_EDITION_ID)));
	            temp.setEdition_name(c.getString(c.getColumnIndexOrThrow(EditionTable.COLUMN_NAME_EDITION_NAME)));
	            temp.setDate(c.getString(c.getColumnIndexOrThrow(EditionTable.COLUMN_DATE)));
	            result.add(temp);
			}while(c.moveToNext());
			
			c.close();
			database.close();
			return result;
		}catch(Exception e){
			database.close();
			Log.i("log_tag", "getAllEdition Error : "+e.toString());
			return null;
		}
	}

}
