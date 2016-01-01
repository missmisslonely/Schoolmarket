package com.miss.schoolmarket2.person;

import com.miss.schoolmarket2.LoadActivity;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.home.HomeActivity;
import com.miss.schoolmarket2.until.Preferences;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeMyInforActivity extends Activity {
	private EditText userNick;
	private EditText userSex;
	private EditText userGrade;
	private ImageView back;
	private TextView save;
	private TextView userNum;
	private ImageView addPic;
	private String userNumS;
	private String changeuserSex;
	private String changeuserNick;
	private String changeuserGrade;
	private Uri iconUri;
	private SharedPreferences settings;
	private static String iconpath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_changmyinfor);
		initView();
		initData();
	}

	public void initView() {
		back = (ImageView) findViewById(R.id.title_change_back);
		save = (TextView) findViewById(R.id.title_change_save);
		userNick = (EditText) findViewById(R.id.changeuserNikeEdit);
		userSex = (EditText) findViewById(R.id.changeuserSexEdit);
		userGrade = (EditText) findViewById(R.id.changeuserGradeEdit);
		userNum = (TextView) findViewById(R.id.changeuserSchoolNumText);
		addPic = (ImageView) findViewById(R.id.changeaddpic);

	}

	public void initData() {
		settings = this.getSharedPreferences(Preferences.PREFS_NAME, 0);
		if (settings.getString("userId", "") != "") {
			userNumS = settings.getString("userNum", "");
			changeuserNick = settings.getString("userName", "");
			userNum.setText(userNumS);
			userNick.setText(changeuserNick);
		}
	}

	public void sortBack(View source) {
		Intent intent = new Intent(ChangeMyInforActivity.this,
				MyInformationActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void addPic(View source) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 2);
	}

	public void saveData(View source) {
		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString("userNick", userNick.getText().toString());
		editor.putString("userSex", userSex.getText().toString());
		editor.putString("userGrade", userGrade.getText().toString());
		editor.putString("userPic", iconpath);
		editor.commit();
		Toast.makeText(ChangeMyInforActivity.this, "上传成功", Toast.LENGTH_SHORT)
				.show();
		Intent intent = new Intent(ChangeMyInforActivity.this,
				MyInformationActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			ShowImage(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ShowImage(int requestCode, Intent data) {
		iconUri = data.getData();
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(iconUri, pojo, null, null,
				null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			iconpath = cursor.getString(columnIndex);
			// Log.i(TAG, "iconpath:" + iconpath);
			System.out.println("----->相册" + iconpath);
			cursor.close();
		}

		Bitmap bit = BitmapFactory.decodeFile(iconpath);
		addPic.setImageBitmap(bit);
	}

}
