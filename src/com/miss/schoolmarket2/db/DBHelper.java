package com.miss.schoolmarket2.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBHelper�̳���SQLiteOpenHelper����Ϊά���͹������ݿ�Ļ���
 * 
 */
public class DBHelper  extends SQLiteOpenHelper{
	
	public static final String DB_NAME = "SchoolMarketGoods.db";
	public static final String DB_TABLE_NAME = "GoodsTable";
	private static final int DB_VERSION=1;
	public DBHelper(Context context) {
		//Context context, String name, CursorFactory factory, int version
		//factory����null,ʹ��Ĭ��ֵ
		super(context, DB_NAME, null, DB_VERSION);
	}
	//���ݵ�һ�δ�����ʱ������onCreate
	@Override
	public void onCreate(SQLiteDatabase db) {		
		//������
		  db.execSQL("CREATE TABLE IF NOT EXISTS GoodsTable" +  
	                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, goodsId VARCHAR,goodsName STRING," +
	                "goodsDescribe STRING,goodsPrice STRING,goodsPicAD1 STRING)");
		  Log.i(SchoolMarketQA.TAG, "create table");
	}
	//���ݿ��һ�δ���ʱonCreate�����ᱻ���ã����ǿ���ִ�д��������䣬��ϵͳ���ְ汾�仯֮�󣬻����onUpgrade���������ǿ���ִ���޸ı�ṹ�����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//�ڱ�info������һ��other
		//db.execSQL("ALTER TABLE info ADD COLUMN other STRING");  
	    Log.i("WIRELESSQA", "update sqlite "+oldVersion+"---->"+newVersion);
	}
	

}
