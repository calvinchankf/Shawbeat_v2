package com.database;

import android.provider.BaseColumns;

public abstract class ArticleTable implements BaseColumns{
	public static final String TABLE_NAME = "article";
	public static final String COLUMN_NAME_ARTICLE_ID = "article_ID";
	public static final String COLUMN_NAME_EDITION_ID = "edition_ID";
	public static final String COLUMN_NAME_POSITION = "position";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_AUTHOR = "author";
	public static final String COLUMN_NAME_CONTENT = "content";
	public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
	public static final String COLUMN_NAME_CATEGORY_NAME = "category_name";
}
/*
	private int _article_ID;
	private int _edition_ID;
	private int _position;
	private String _title="";
	private String _author="";
	private String _content="";
	private String _category_id;
	private String _category_name;
//*/