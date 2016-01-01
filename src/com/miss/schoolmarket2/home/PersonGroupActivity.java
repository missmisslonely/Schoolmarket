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
	 * 一个静态的ActivityGroup变量，用于管理本Group中的Activity
	 */
	public static ActivityGroup group;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		group = this;
		
	}
	/*//退出
		@Override
		 public boolean onKeyDown(int keyCode, KeyEvent event) {
		    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
		    		
		        	
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
		//把后退事件交给子Activity处理
		group.getLocalActivityManager()
			.getCurrentActivity().onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//把界面切换放到onResume方法中是因为，从其他选项卡切换回来时，
		//调用搞得是onResume方法
		
		//要跳转的界面
		Intent intent = new Intent(this, PersonActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//把一个Activity转换成一个View
		Window w = group.getLocalActivityManager().startActivity("PersonActivity",intent);
	    View view = w.getDecorView();
	    //把View添加大ActivityGroup中
	    group.setContentView(view);
	}
	
	

}
