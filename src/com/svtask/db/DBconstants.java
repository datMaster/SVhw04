package com.svtask.db;

public class DBconstants {
	
	public static final int DB_VERSION = 1;
	public static final int INIT_CHECK_STATUS = 0;
	public static final String DB_NAME = "DBwords";
	public static final String DB_ID_UPDATE = "_id =";

	public static final String TABLE_NAME = "words";
	public static final String WORD_COL = "word";
	public static final String CHECKED_STATUS_COL = "wpassw";
	public static final String CREATE_TABLE = "create table " + TABLE_NAME
			+ " ( _id integer primary key autoincrement, " 
			+ WORD_COL + " TEXT, " + CHECKED_STATUS_COL + " INTEGER)";
	
}
