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
 * DBManager�ǽ�����DBHelper֮�ϣ���װ�˳��õ�ҵ�񷽷�
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
	 * ���������һ��������Ϣ
	 * 
	 * @param memberInfo
	 */
	public void add(List<GoodsInfor> memberInfo) {
		db.beginTransaction();// ��ʼ����
		try {
			for (GoodsInfor info : memberInfo) {
				Log.i(SchoolMarketQA.TAG, "------add memberInfo----------");
				Log.i(SchoolMarketQA.TAG, info.goodsId + "/" + info.goodsName);
				// ���info�в�������
				db.execSQL("INSERT INTO GoodsTable VALUES(null,?,?,?,?,?)",
						new Object[] { info.goodsId, info.goodsName,
								info.goodsDescribe, info.goodsPrice,
								info.goodsPicAD1 });
			}
			db.setTransactionSuccessful();// ����ɹ�
		} finally {
			db.endTransaction();// ��������
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
	 * ͨ��name��ɾ������
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
	 * �������
	 */
	public void clearData() {
		ExecSQL("DELETE FROM GoodsTable");
		Log.i(SchoolMarketQA.TAG, "clear data");
	}

	/**
	 * ��ѯ��Ϣ,�������е�����
	 * 
	 * 
	 */

	public ArrayList<GoodsInfor> searchPublishGoodsData() {
		String sql = "SELECT _id,goodsId,goodsName ,goodsDescribe,goodsPrice,goodsPicAD1 FROM GoodsTable";
		return ExecSQLForMemberInfo(sql);
	}

	/**
	 * ִ��SQL�����list
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
	 * ִ��һ��SQL���
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
	 * ִ��SQL������һ���α�
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
