//�������ģ���ʾ�û���Ϣ��ͷ�����**��������ת��ť��1��Ԥ������ 2������Ϣ3��������
package com.miss.schoolmarket2.person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miss.schoolmaket2.myapplication.MyApplication;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.home.HomeActivity;
import com.miss.schoolmarket2.publish.PublishActivity;

public class PersonActivity extends Activity {

	String string = "����Ա���ڿ����У��������ע~~";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_person);
		MyApplication.getInstance().addActivity(this);
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PersonActivity.this,
				HomeActivity.class);
		startActivity(intent);
		this.finish();
		
	}
	public void wo1(View source) {
		Intent intent = new Intent();
		intent.setClass(PersonActivity.this, MyInformationActivity.class);
		startActivity(intent);

	}

	public void wo2(View source) {
		Intent intent = new Intent();
		intent.setClass(PersonActivity.this,

		MyPublishActivity.class);
		startActivity(intent);

	}

	public void wo3(View source) {
		/*
		 * Intent intent = new Intent(); intent.setClass(PersonActivity.this,
		 * 
		 * MainActivity.class); startActivity(intent);
		 */
		Toast toast = Toast.makeText(getApplicationContext(), string,
				Toast.LENGTH_SHORT);
		toast.show();

	}

	public void wo4(View source) {
		/*
		 * Intent intent = new Intent(); intent.setClass(PersonActivity.this,
		 * MainActivity.class); startActivity(intent);
		 */
		Toast toast = Toast.makeText(getApplicationContext(), string,
				Toast.LENGTH_SHORT);
		toast.show();

	}

	public void wo5(View source) {
		Intent intent = new Intent();
		intent.setClass(PersonActivity.this,

		AboutActivity.class);
		startActivity(intent);

	}

	public void wo6(View source) {
		/*
		 * Intent intent = new Intent(); intent.setClass(PersonActivity.this,
		 * MainActivity.class); startActivity(intent);
		 */
		File file = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"SchoolMarket2");
		DeleteFile(file);
		mHandler.sendEmptyMessage(1);
		/*
		 * Toast toast=Toast.makeText(getApplicationContext(), string,
		 * Toast.LENGTH_SHORT); toast.show();
		 */

	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "�ļ����ļ��в�����",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "���������",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * �ݹ�ɾ���ļ����ļ���
	 * 
	 * @param file
	 *            Ҫɾ���ĸ�Ŀ¼
	 */
	public void DeleteFile(File file) {
		if (file.exists() == false) {
			mHandler.sendEmptyMessage(0);
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}

	public void wo7(View source) {
		/*
		 * Intent intent = new Intent(); intent.setClass(PersonActivity.this,
		 * MainActivity.class); startActivity(intent);
		 */
		Toast toast = Toast.makeText(getApplicationContext(), string,
				Toast.LENGTH_SHORT);
		toast.show();

	}

}
