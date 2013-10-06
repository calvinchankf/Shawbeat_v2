package com.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shawbeat.Article;

public class ArticleDBHelper extends SQLiteOpenHelper{
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "articleDB.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + ArticleTable.TABLE_NAME + " (" + 
    	    ArticleTable._ID + " INTEGER PRIMARY KEY," +
    	    ArticleTable.COLUMN_NAME_ARTICLE_ID + INT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_EDITION_ID + INT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_POSITION + INT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_CATEGORY_ID + INT_TYPE + COMMA_SEP +
    	    ArticleTable.COLUMN_NAME_CATEGORY_NAME + TEXT_TYPE +
    	    " )";

    private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + EditionTable.TABLE_NAME;
    
	public ArticleDBHelper(Context context) {
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
	
	// Since the number of articles may increase or decrease, here I delete the old articles per edition,category then insert again
	// Actually we can use "insert or replace" for this with pure SQL command, the purpose of below code is just for practice on android db method
	public void insertOrUpdateArticle(List<Article> article, int ed_id, int cat_id){
		Log.i("log_tag", "insertOrUpdateArticle");
		SQLiteDatabase database = this.getWritableDatabase();
		try{
			// Define 'where' part of query.
			String selection = ArticleTable.COLUMN_NAME_EDITION_ID + " =? AND " + ArticleTable.COLUMN_NAME_CATEGORY_ID + " =?";
			// Specify arguments in placeholder order.
			String[] selectionArgs = { String.valueOf(ed_id), String.valueOf(cat_id) };
			// Issue SQL statement.
			int dd = database.delete(ArticleTable.TABLE_NAME, selection, selectionArgs); //delete the article per edition first
			Log.i("log_tag", "dd="+String.valueOf(dd));
			
			int size = article.size();
			
			for(int i=0;i<size;i++){
				ContentValues values = new ContentValues();
				values.put(ArticleTable.COLUMN_NAME_ARTICLE_ID, article.get(i).getArticleID());
				values.put(ArticleTable.COLUMN_NAME_EDITION_ID, article.get(i).getEdition_ID());
				values.put(ArticleTable.COLUMN_NAME_POSITION, article.get(i).getPosition());
				values.put(ArticleTable.COLUMN_NAME_TITLE, article.get(i).getTitle());
				values.put(ArticleTable.COLUMN_NAME_AUTHOR, article.get(i).getAuthor());
				values.put(ArticleTable.COLUMN_NAME_CONTENT, article.get(i).getContent());
				values.put(ArticleTable.COLUMN_NAME_CATEGORY_ID, article.get(i).getCategory_id());
				values.put(ArticleTable.COLUMN_NAME_CATEGORY_NAME, article.get(i).getCategory_name());
				
				//insert return the new row id
				long newRowId = database.insert(ArticleTable.TABLE_NAME, null, values);
				Log.i("log_tag", String.valueOf(newRowId));
			}
		}catch(Exception e){
			Log.i("log_tag", "insertOrUpdateArticle Error : "+e.toString());
		}
		database.close();
	}
	
	public List<Article> getArticlesByEditionID(int ed_id, int cat_id){
		Log.i("log_tag", "getAllArticle");
		SQLiteDatabase database = this.getReadableDatabase();
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			ArticleTable.COLUMN_NAME_ARTICLE_ID,
			ArticleTable.COLUMN_NAME_EDITION_ID,
			ArticleTable.COLUMN_NAME_POSITION,
			ArticleTable.COLUMN_NAME_TITLE,
			ArticleTable.COLUMN_NAME_AUTHOR,
			ArticleTable.COLUMN_NAME_CONTENT,
			ArticleTable.COLUMN_NAME_CATEGORY_ID,
			ArticleTable.COLUMN_NAME_CATEGORY_NAME
		};
		
		// Define 'where' part of query.
		String selection = ArticleTable.COLUMN_NAME_EDITION_ID + " =? AND " + ArticleTable.COLUMN_NAME_CATEGORY_ID + " =?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(ed_id), String.valueOf(cat_id) };
		// Issue SQL statement.
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder = ArticleTable._ID + " ASC"; //ASC|DESC
		
		try{
			Cursor c = database.query(
				ArticleTable.TABLE_NAME,  // The table to query
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
			
			List<Article> result = new ArrayList<Article>();
			do{
				Article temp = new Article();
				temp.setArticleID(c.getInt(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_ARTICLE_ID)));
				temp.setEdition_ID(c.getInt(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_EDITION_ID)));
				temp.setPosition(c.getInt(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_POSITION)));
				temp.setTitle(c.getString(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_TITLE)));
				temp.setAuthor(c.getString(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_AUTHOR)));
				temp.setContent(c.getString(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_CONTENT)));
				temp.setCategory_id(c.getInt(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_CATEGORY_ID)));
				temp.setCategory_name(c.getString(c.getColumnIndexOrThrow(ArticleTable.COLUMN_NAME_CATEGORY_NAME)));
	            result.add(temp);
			}while(c.moveToNext());
			
			c.close();
			database.close();
			return result;
		}catch(Exception e){
			database.close();
			Log.i("log_tag", "getAllArticle Error : "+e.toString());
			return null;
		}
		
	}

}
