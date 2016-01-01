package com.miss.schoolmarket2.person;

import java.util.ArrayList;

import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.DeletableAdapter;
import com.miss.schoolmarket2.db.DBManager;
import com.miss.schoolmarket2.entity.GoodsInfor;
import com.miss.schoolmarket2.publish.PublishActivity;
import com.miss.schoolmarket2.until.Preferences;
import com.miss.schoolmarket2.until.Signal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyPublishActivity extends FragmentActivity {

	private Fragment myPublishFragment;
	private MyPublishFragmentA myPublishFragmentA;
	private MyPublishFragmentB myPublishFragmentB;
	private SharedPreferences settings;
	ArrayList<GoodsInfor> infoList;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypublish);
		// 得到信号量如果为0显示fragmentb
		// 信号量如果为1显示fragmenta
		refresh();

	}

	public void Back(View source) {
		this.onBackPressed();
		this.finish();
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	public void mypublish(View source) {
		Intent intent = new Intent(MyPublishActivity.this,
				PublishActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void sendDataAndShowFragmentA() {
		/* 获取manager */
		manager = this.getSupportFragmentManager();
		/* 创建事物 */
		transaction = manager.beginTransaction();

		/* 创建LeftFragment */
		myPublishFragmentA = new MyPublishFragmentA();

		/* 创建一个Bundle用来存储数据，传递到Fragment中 */
		Bundle bundle = new Bundle();
		/* 往bundle中添加数据 */
		bundle.putSerializable("infoList", infoList);
		/* 把数据设置到Fragment中 */
		myPublishFragmentA.setArguments(bundle);
		/* 把Fragment添加到对应的位置 */
		transaction.replace(R.id.id_content, myPublishFragmentA);
		/* 提交事物 */
		transaction.commit();
	}

	private void setFragmentB() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		myPublishFragmentB = new MyPublishFragmentB();
		transaction.replace(R.id.id_content, myPublishFragmentB);
		transaction.commit();
	}

	class getPublishDataFromSqliteTask extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... i)

		{// 处理后台执行的任务，在后台线程执行
			infoList = new ArrayList<GoodsInfor>();
			dbManager = new DBManager(MyPublishActivity.this);
			infoList = dbManager.searchPublishGoodsData();
			// 初始化数据结束
			if (infoList.size() > 0) {
				sendDataAndShowFragmentA();
			} else {
				setFragmentB();
			}

			return 0;
		}

		@Override
		protected void onPostExecute(Integer result)

		{

		}
	}

	private void refresh() {
		getPublishDataFromSqliteTask task = new getPublishDataFromSqliteTask();
		task.execute(0);
	}
}
