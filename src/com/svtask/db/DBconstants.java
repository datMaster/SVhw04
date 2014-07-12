package com.svtask.db;

public class DBconstants {
	
	public static final int DB_VERSION = 1;
	public static final int INIT_CHECK_STATUS = 0;
	public static final String DB_NAME = "DBwords";
	public static final String DB_ID = "_id =";

	public static final String TABLE_NAME = "WORDS_INP_GAME";
	public static final String ID_COL = "_id";
	public static final String WORD_COL = "word";
	public static final String CHECKED_STATUS_COL = "checkStatus";
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" 
								+ ID_COL + " INTEGER PRIMARY KEY, " 
								+ WORD_COL + " TEXT, " 
								+ CHECKED_STATUS_COL + " INTEGER)";
	
	public static final String SELECT_ALL_QUERY = "SELECT  * FROM " + TABLE_NAME;
	
}
