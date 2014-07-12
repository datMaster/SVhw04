package com.svtask.db;

import java.util.ArrayList;

import com.svtask.utils.SettingsViewHolder;
import com.svtask2.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBworker extends SQLiteOpenHelper {	
	
	private Context context;
	private SQLiteDatabase dataBase;
	
	public DBworker(Context context) {
		super(context, DBconstants.DB_NAME, null, DBconstants.DB_VERSION);
		this.context = context;
//		this.dataBase = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(DBconstants.CREATE_TABLE);
//		dataBase = getWritableDatabase();
		CharSequence[] words = context.getResources().getStringArray(R.array.words);		
		for(int i = 0; i < words.length; i ++) {
			DBitem dbItem = new DBitem();
			dbItem.word = words[i].toString();
			dbItem.id = i;
			dbItem.status = DBconstants.INIT_CHECK_STATUS;
			addRecord(dbItem);
		}				
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}		
	
	private void insertNewRecord(ContentValues content) {
		dataBase = getWritableDatabase();
		dataBase.insert(DBconstants.TABLE_NAME, null, content);
		dataBase.close();
	}		
	
	public void addRecord(DBitem newItem) {		
		ContentValues content = new ContentValues();
		content.put(DBconstants.ID_COL, newItem.id);
		content.put(DBconstants.WORD_COL, newItem.word);
		content.put(DBconstants.CHECKED_STATUS_COL, newItem.status);
		insertNewRecord(content);		
	}
	
	public void editRecord(DBitem editedItem) {
		dataBase = getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(DBconstants.WORD_COL, editedItem.word);
		content.put(DBconstants.CHECKED_STATUS_COL, editedItem.status);
		dataBase.update(DBconstants.TABLE_NAME, content, DBconstants.DB_ID + editedItem.id, null);
		dataBase.close();
	}
	
	public void removeRecord(int position) {
		dataBase = getWritableDatabase();
		dataBase.delete(DBconstants.TABLE_NAME, DBconstants.DB_ID + position, null);
		dataBase.close();
	}
	
	public void updateCheckedStatus(ArrayList<Boolean> checkedWords) {				
		for(int i = 0; i < checkedWords.size(); i ++) {
			ContentValues content = new ContentValues();
			content.put(DBconstants.CHECKED_STATUS_COL, (checkedWords.get(i) ? 1 : 0));
			dataBase.update(DBconstants.TABLE_NAME, content, DBconstants.DB_ID + i, null);
		}			
	}
	
	public ArrayList<DBitem> getAllConvertedRecords() {
		dataBase = getReadableDatabase();
		ArrayList<DBitem> records = new ArrayList<DBitem>();
		Cursor cursor = dataBase.rawQuery(DBconstants.SELECT_ALL_QUERY, null);
		try {
			if(cursor.moveToFirst()) {
				do {
					DBitem dbItem = new DBitem();
					dbItem.id = cursor.getInt(cursor.getColumnIndex(DBconstants.ID_COL));
					dbItem.word = cursor.getString(cursor.getColumnIndex(DBconstants.WORD_COL));
					dbItem.status = cursor.getInt(cursor.getColumnIndex(DBconstants.CHECKED_STATUS_COL));
					records.add(dbItem);
				} while(cursor.moveToNext());
			}
		}
		finally {
			try {
				cursor.close();
			} catch (Exception ex) {}
		}
		dataBase.close();
		return records;
	}

}
