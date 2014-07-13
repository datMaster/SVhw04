package com.svtask.db;

import java.util.ArrayList;

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
		if (getCount() == 0) {
			fillDB();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBconstants.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private void insertNewRecord(ContentValues content) {
		long result = dataBase.insert(DBconstants.TABLE_NAME, null, content);
		int cc = 2;
	}

	public void addRecord(DBitem newItem) {
		dataBase = getWritableDatabase();
		ContentValues content = new ContentValues();
//		content.put(DBconstants.ID_COL, newItem.id);
		content.put(DBconstants.WORD_COL, newItem.word);
		content.put(DBconstants.CHECKED_STATUS_COL, newItem.status);
		insertNewRecord(content);
		dataBase.close();
	}

	public void removeRecord(int position) {
		dataBase = getWritableDatabase();
		dataBase.delete(DBconstants.TABLE_NAME, DBconstants.DB_ID,
				new String[] { String.valueOf(position) });
		dataBase.close();
	}

	public void updateCheckedStatus(ArrayList<DBitem> dbItems) {
		dataBase = getWritableDatabase();
		for (DBitem item : dbItems) {
			ContentValues content = new ContentValues();
			content.put(DBconstants.WORD_COL, item.word);
			content.put(DBconstants.CHECKED_STATUS_COL, item.status);
			dataBase.update(DBconstants.TABLE_NAME, content, DBconstants.DB_ID,
					new String[] { String.valueOf(item.id) });
		}
		dataBase.close();
	}

	public ArrayList<DBitem> getAllConvertedRecords() {

		dataBase = getReadableDatabase();
		ArrayList<DBitem> records = new ArrayList<DBitem>();

		Cursor cursor = dataBase.rawQuery(DBconstants.SELECT_ALL_QUERY, null);

		if (cursor.moveToFirst()) {
			do {
				DBitem dbItem = new DBitem();
				dbItem.id = cursor.getInt(cursor
						.getColumnIndex(DBconstants.ID_COL));
				dbItem.word = cursor.getString(cursor
						.getColumnIndex(DBconstants.WORD_COL));
				dbItem.status = (cursor.getInt(cursor
						.getColumnIndex(DBconstants.CHECKED_STATUS_COL))) > 0;
				records.add(dbItem);
			} while (cursor.moveToNext());
		}

		cursor.close();
		dataBase.close();
		return records;
	}

	public ArrayList<String> getAllowedWords() {
		ArrayList<String> allowedWords = new ArrayList<String>();
		dataBase = getReadableDatabase();
		Cursor cursor = dataBase
				.rawQuery(DBconstants.ALLOWED_WORDS_QUERY, null);
		try {
			if (cursor.moveToFirst()) {
				do {
					String word = cursor.getString(cursor
							.getColumnIndex(DBconstants.WORD_COL));
					allowedWords.add(word);
				} while (cursor.moveToNext());
			}
		} finally {
			try {
				cursor.close();
			} catch (Exception ex) {
			}
		}
		dataBase.close();
		return allowedWords;
	}

	private int getCount() {
		dataBase = getReadableDatabase();
		Cursor cursor = dataBase.rawQuery(DBconstants.SELECT_ALL_QUERY, null);
		int count = cursor.getCount();
		cursor.close();
		dataBase.close();
		return count;
	}

	private void fillDB() {
		dataBase = getWritableDatabase();
		CharSequence[] words = context.getResources().getStringArray(
				R.array.words);
		for (int i = 0; i < words.length; i++) {
			DBitem dbItem = new DBitem();
			dbItem.word = words[i].toString();
//			dbItem.id = i;
			dbItem.status = DBconstants.INIT_CHECK_STATUS;
			addRecord(dbItem);
		}
		dataBase.close();
	}
}
