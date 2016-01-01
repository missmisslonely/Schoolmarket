package com.miss.schoolmarket2.person;

import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.until.Preferences;
import com.miss.schoolmarket2.view.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyInformationActivity extends Activity {

	private TextView userNumT;
	private TextView userNickT;
	private TextView userSexT;
	private TextView userGradeT;
	private CircleImageView userPic;
	private String userNumS;
	private String userNickS;
	private String userSexS;
	private String userGradeS;
	private String userPicPith;
	private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_myinformation);
		initView();
		initData() ;

	}

	private void initView() {
		userNumT = (TextView) findViewById(R.id.userSchoolNumText);
		userNickT = (TextView) findViewById(R.id.userNikeText);
		userSexT = (TextView) findViewById(R.id.userSexText);
		userGradeT = (TextView) findViewById(R.id.userGradeText);
		userPic = (CircleImageView) findViewById(R.id.activity_myinfor_userPic);
	}

	private void initData() {
		
		
		settings = this.getSharedPreferences(Preferences.PREFS_NAME, 0);
		System.out.println("得到的用户id"+settings.getString("userId", ""));
		System.out.println("得到的用户pic"+settings.getString("userPic", ""));
		System.out.println("得到的用户sex"+settings.getString("userSex", ""));
		if (settings.getString("userId", "") != "") {
			userNumS = settings.getString("userNum", "");
			userNickS = settings.getString("userName", "");
			
			userNumT.setText(userNumS);
			userNickT.setText(userNickS);
	}
		if (settings.getString("userPic", "") != "") {
			userSexS = settings.getString("userSex", "");
			userGradeS = settings.getString("userGrade", "");
			userPicPith = settings.getString("userPic", "");
			Bitmap bit = BitmapFactory.decodeFile(userPicPith);
			((ImageView) userPic).setImageBitmap(bit);
			userSexT.setText(userSexS);
			userGradeT.setText(userGradeS);
		}
		
		
		
	}
	public void changeBack(View source) {
		this.onBackPressed();
		this.finish();
	}
	public void change(View source) {
		Intent intent = new Intent();
		intent.setClass(MyInformationActivity.this, ChangeMyInforActivity.class);
		startActivity(intent);
		this.finish();
	}

	

}
