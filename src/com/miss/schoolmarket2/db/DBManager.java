package com.miss.schoolmarket2.db;

import java.util.ArrayList;
import java.util.List;

import com.miss.schoolmarket2.entity.GoodsInfor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DBManager是建立在DBHelper之上，封装了常用的业务方法
 * 
 * @author
 */
public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 向表中增加一个本地信息
	 * 
	 * @param memberInfo
	 */
	public void add(List<GoodsInfor> memberInfo) {
		db.beginTransaction();// 开始事务
		try {
			for (GoodsInfor info : memberInfo) {
				Log.i(SchoolMarketQA.TAG, "------add memberInfo----------");
				Log.i(SchoolMarketQA.TAG, info.goodsId + "/" + info.goodsName);
				// 向表info中插入数据
				db.execSQL("INSERT INTO GoodsTable VALUES(null,?,?,?,?,?)",
						new Object[] { info.goodsId, info.goodsName,
								info.goodsDescribe, info.goodsPrice,
								info.goodsPicAD1 });
			}
			db.setTransactionSuccessful();// 事务成功
		} finally {
			db.endTransaction();// 结束事务
		}
	}

	/**
	 * 
	 */
	public void add(String goodsId, String goodsName, String goodsDescribe,
			String goodsPrice, String goodsPicAD1) {
		Log.i(SchoolMarketQA.TAG, "------add data----------");
		ContentValues cv = new ContentValues();
		cv.put("goodsId", goodsId);
		cv.put("goodsName", goodsName);
		cv.put("goodsDescribe", goodsDescribe);
		cv.put("goodsPrice", goodsPrice);
		cv.put("goodsPicAD1", goodsPicAD1);
		db.insert(DBHelper.DB_TABLE_NAME, null, cv);
		Log.i(SchoolMarketQA.TAG, goodsName + "/" + goodsId);
	}

	/**
	 * 通过name来删除数据
	 * 
	 * @param name
	 */
	public void delData(String goodsName) {
		// ExecSQL("DELETE FROM info WHERE name ="+"'"+name+"'");
		String[] args = { goodsName };
		db.delete(DBHelper.DB_TABLE_NAME, "goodsName=?", args);
		Log.i(SchoolMarketQA.TAG, "delete data by " + goodsName);

	}

	/**
	 * 清空数据
	 */
	public void clearData() {
		ExecSQL("DELETE FROM GoodsTable");
		Log.i(SchoolMarketQA.TAG, "clear data");
	}

	/**
	 * 查询信息,返回所有的数据
	 * 
	 * 
	 */

	public ArrayList<GoodsInfor> searchPublishGoodsData() {
		String sql = "SELECT _id,goodsId,goodsName ,goodsDescribe,goodsPrice,goodsPicAD1 FROM GoodsTable";
		return ExecSQLForMemberInfo(sql);
	}

	/**
	 * 执行SQL命令返回list
	 * 
	 * @param sql
	 * @return
	 */
	private ArrayList<GoodsInfor> ExecSQLForMemberInfo(String sql) {
		ArrayList<GoodsInfor> list = new ArrayList<GoodsInfor>();
		Cursor c = ExecSQLForCursor(sql);
		while (c.moveToNext()) {
			GoodsInfor info = new GoodsInfor();

			info._id = c.getInt(c.getColumnIndex("_id"));
			info.goodsId = c.getString(c.getColumnIndex("goodsId"));
			info.goodsName = c.getString(c.getColumnIndex("goodsName"));
			info.goodsDescribe = c.getString(c.getColumnIndex("goodsDescribe"));
			info.goodsPrice = c.getString(c.getColumnIndex("goodsPrice"));
			info.goodsPicAD1 = c.getString(c.getColumnIndex("goodsPicAD1"));

			list.add(info);
		}
		c.close();
		return list;
	}

	/**
	 * 执行一个SQL语句
	 * 
	 * @param sql
	 */
	private void ExecSQL(String sql) {
		try {
			db.execSQL(sql);
			Log.i("execSql: ", sql);
		} catch (Exception e) {
			Log.e("ExecSQL Exception", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 执行SQL，返回一个游标
	 * 
	 * @param sql
	 * @return
	 */
	private Cursor ExecSQLForCursor(String sql) {
		Cursor c = db.rawQuery(sql, null);
		return c;
	}

	public void closeDB() {
		db.close();
	}

}
