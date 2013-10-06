package com.database;

import android.provider.BaseColumns;

public abstract class EditionTable implements BaseColumns {
	public static final String TABLE_NAME = "edition";
	public static final String COLUMN_NAME_EDITION_ID = "edition_ID";
	public static final String COLUMN_NAME_EDITION_NAME = "edition_name";
	public static final String COLUMN_DATE = "date";
}
/*
	private int _edition_ID="";
	private String _edition_name="";
	private String _date="";
//*/