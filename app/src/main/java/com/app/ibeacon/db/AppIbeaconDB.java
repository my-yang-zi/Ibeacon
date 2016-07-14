package com.app.ibeacon.db;

public interface AppIbeaconDB {

	String NAME = "ibeacon.db";
	int VERSION = 1;

	public interface TableAppIbeacon {
		String TABLE_NAME = "appibeacon";

		String COLUMN_ID = "_id";
		String COLUMN_PACKAGE_NAME = "package_name";// 上锁的包名

		String CREATE_TABLE_SQL = "create table " + TABLE_NAME + "("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_PACKAGE_NAME + " text unique" + ")";
	}
}
