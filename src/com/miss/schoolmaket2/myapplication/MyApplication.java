package com.miss.schoolmaket2.myapplication;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

public class MyApplication extends Application {
	// ����������ɾ������add��remove��LinedList�Ƚ�ռ���ƣ���ΪArrayListʵ���˻��ڶ�̬��������ݽṹ��Ҫ�ƶ����ݡ�LinkedList������������ݽṹ,��������ɾ��
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;

	private MyApplication() {
	}

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
