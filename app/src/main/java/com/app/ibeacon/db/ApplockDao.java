package com.app.ibeacon.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ApplockDao {
	private IbeanconDBOpenHelper mHelper;
	private ContentResolver mCr;

	public ApplockDao(Context context) {
		mHelper = new IbeanconDBOpenHelper(context);
		mCr = context.getContentResolver();
	}

	/**
	 * 添加
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean add(String packageName) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AppIbeaconDB.TableAppIbeacon.COLUMN_PACKAGE_NAME, packageName);
		long id = db.insert(AppIbeaconDB.TableAppIbeacon.TABLE_NAME, null, values);

		db.close();

		mCr.notifyChange(Uri.parse("content://applock"), null);

		return id != -1;
	}

	/**
	 * 删除
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean delete(String packageName) {
		SQLiteDatabase db = mHelper.getWritableDatabase();

		String whereClause = AppIbeaconDB.TableAppIbeacon.COLUMN_PACKAGE_NAME + "=?";
		String[] whereArgs = new String[] { packageName };
		int delete = db.delete(AppIbeaconDB.TableAppIbeacon.TABLE_NAME, whereClause,
				whereArgs);
		db.close();

		mCr.notifyChange(Uri.parse("content://applock"), null);

		return delete > 0;
	}

	/**
	 * 判断是否是上锁的
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isLock(String packageName) {

		SQLiteDatabase db = mHelper.getReadableDatabase();

		String sql = "select count(1) from "
				+ AppIbeaconDB.TableAppIbeacon.TABLE_NAME + " where "
				+ AppIbeaconDB.TableAppIbeacon.COLUMN_PACKAGE_NAME + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { packageName });

		int count = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}

			cursor.close();
		}

		db.close();
		return count > 0;
	}

	public List<String> findAll() {

		List<String> list = new ArrayList<String>();

		SQLiteDatabase db = mHelper.getReadableDatabase();

		String sql = "select " + AppIbeaconDB.TableAppIbeacon.COLUMN_PACKAGE_NAME
				+ " from " + AppIbeaconDB.TableAppIbeacon.TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				String string = cursor.getString(0);
				list.add(string);
			}
			cursor.close();
		}

		db.close();

		return list;
	}
}
