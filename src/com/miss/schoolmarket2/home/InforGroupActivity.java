package com.miss.schoolmarket2.home;


import com.miss.schoolmarket2.index.IndexActivity;


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * 
 * 
 *
 */
public class InforGroupActivity extends ActivityGroup {
	
	/**
	 * һ����̬��ActivityGroup���������ڹ���Group�е�Activity
	 */
	public static ActivityGroup group;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		group = this;
		
	}
	
	@Override
	public void onBackPressed() {
		
		//�Ѻ����¼�������Activity����
		group.getLocalActivityManager()
			.getCurrentActivity().onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//�ѽ����л��ŵ�onResume����������Ϊ��������ѡ��л�����ʱ��
		//���ø����onResume����
		
		//Ҫ��ת�Ľ���
		Intent intent = new Intent(this, IndexActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//��һ��Activityת����һ��View
		Window w = group.getLocalActivityManager().startActivity("IndexActivity",intent);
	    View view = w.getDecorView();
	    //��View��Ӵ�ActivityGroup��
	    group.setContentView(view);
	}
	
	
	

}
