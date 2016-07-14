package com.app.ibeacon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IbeanconDBOpenHelper extends SQLiteOpenHelper {

	public IbeanconDBOpenHelper(Context context) {
		super(context, AppIbeaconDB.NAME, null, AppIbeaconDB.VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AppIbeaconDB.TableAppIbeacon.CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
