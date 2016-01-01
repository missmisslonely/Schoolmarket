package com.miss.schoolmarket2.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBHelper继承了SQLiteOpenHelper，作为维护和管理数据库的基类
 * 
 */
public class DBHelper  extends SQLiteOpenHelper{
	
	public static final String DB_NAME = "SchoolMarketGoods.db";
	public static final String DB_TABLE_NAME = "GoodsTable";
	private static final int DB_VERSION=1;
	public DBHelper(Context context) {
		//Context context, String name, CursorFactory factory, int version
		//factory输入null,使用默认值
		super(context, DB_NAME, null, DB_VERSION);
	}
	//数据第一次创建的时候会调用onCreate
	@Override
	public void onCreate(SQLiteDatabase db) {		
		//创建表
		  db.execSQL("CREATE TABLE IF NOT EXISTS GoodsTable" +  
	                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, goodsId VARCHAR,goodsName STRING," +
	                "goodsDescribe STRING,goodsPrice STRING,goodsPicAD1 STRING)");
		  Log.i(SchoolMarketQA.TAG, "create table");
	}
	//数据库第一次创建时onCreate方法会被调用，我们可以执行创建表的语句，当系统发现版本变化之后，会调用onUpgrade方法，我们可以执行修改表结构等语句
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//在表info中增加一列other
		//db.execSQL("ALTER TABLE info ADD COLUMN other STRING");  
	    Log.i("WIRELESSQA", "update sqlite "+oldVersion+"---->"+newVersion);
	}
	

}
