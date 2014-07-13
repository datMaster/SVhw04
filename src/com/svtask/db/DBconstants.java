package com.svtask.db;

public class DBconstants {
	
	public static final int DB_VERSION = 1;
	public static final boolean INIT_CHECK_STATUS = false;
	public static final String DB_NAME = "DBwords";	

	public static final String TABLE_NAME = "WORDS_INP_GAME";
	public static final String ID_COL = "id";
	public static final String DB_ID = ID_COL + " = ?";
	public static final String WORD_COL = "word";
	public static final String CHECKED_STATUS_COL = "checkStatus";
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" 
								+ ID_COL + " INTEGER PRIMARY KEY autoincrement, " 
								+ WORD_COL + " TEXT NOT NULL, " 
								+ CHECKED_STATUS_COL + " INTEGER NOT NULL)";
	
	public static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
	public static final String ALLOWED_WORDS_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + CHECKED_STATUS_COL + " > 0";
	
}
