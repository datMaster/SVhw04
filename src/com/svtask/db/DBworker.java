package com.svtask.db;

import java.util.ArrayList;

import com.svtask.utils.SettingsViewHolder;
import com.svtask2.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBworker extends SQLiteOpenHelper {	
	
	private Context context;
	private SQLiteDatabase dataBase;
	
	public DBworker(Context context) {
		super(context, DBconstants.DB_NAME, null, DBconstants.DB_VERSION);
		this.context = context;
		this.dataBase = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBconstants.CREATE_TABLE);
		CharSequence[] words = context.getResources().getStringArray(R.array.words);		
		for(int i = 0; i < words.length; i ++) {			
			addRecord(words[i].toString(), DBconstants.INIT_CHECK_STATUS);
		}				
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}		
	
	private void insertNewRecord(ContentValues content) {
		dataBase.insert(DBconstants.TABLE_NAME, null, content);
	}
	
	public void closeDB() {
		dataBase.close();
	}
	
	public void addRecord(String word, int checkedStatus) {
		ContentValues content = new ContentValues();
		content.put(DBconstants.WORD_COL, word);
		content.put(DBconstants.CHECKED_STATUS_COL, checkedStatus);
		insertNewRecord(content);
	}
	
	public void editRecord(String word, int checkStatus, int id) {
		
	}
	
	public void updateRecord(ArrayList<Boolean> checkedWords) {				
		for(int i = 0; i < checkedWords.size(); i ++) {
			ContentValues content = new ContentValues();
			content.put(DBconstants.CHECKED_STATUS_COL, (checkedWords.get(i) ? 1 : 0));
			dataBase.update(DBconstants.TABLE_NAME, content, DBconstants.DB_ID_UPDATE + i, null);
		}			
	}
	
	public ArrayList<SettingsViewHolder> getAllRecords() {
		return null;
	}

}
