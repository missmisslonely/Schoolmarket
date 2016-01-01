/**
 * 
 */
package com.miss.schoolmarket2.home;



import com.miss.schoolmarket2.person.PersonActivity;

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
public class PersonGroupActivity extends ActivityGroup {
	
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
	/*//�˳�
		@Override
		 public boolean onKeyDown(int keyCode, KeyEvent event) {
		    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //��ȡ back��
		    		
		        	
		        		Intent intent = new Intent();
		            	intent.setClass(PersonGroupActivity.this,Exit.class);
		            	startActivity(intent);
		        	
		    	}
				return false;
		    	
		 }*/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
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
		Intent intent = new Intent(this, PersonActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//��һ��Activityת����һ��View
		Window w = group.getLocalActivityManager().startActivity("PersonActivity",intent);
	    View view = w.getDecorView();
	    //��View��Ӵ�ActivityGroup��
	    group.setContentView(view);
	}
	
	

}
