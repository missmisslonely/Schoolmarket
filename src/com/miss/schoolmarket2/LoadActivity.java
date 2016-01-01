//登陆界面，获取用户填写的学号和密码，和服务器进行交互，发送{"UserNum": "04121093",
//	"UserPassword": "04121093"}并且解析返回的json ,判断登陆是否成功，假如不成功
//提示重新登录，登陆成功跳转到下一界面。
package com.miss.schoolmarket2;

import java.io.IOException;

import com.miss.schoolmarket2.entity.UserInfor;
import com.miss.schoolmarket2.home.HomeActivity;
import com.miss.schoolmarket2.until.HttpUtils;
import com.miss.schoolmarket2.until.IP;
import com.miss.schoolmarket2.until.NetConnect;
import com.miss.schoolmarket2.until.Preferences;
import com.miss.schoolmarket2.view.CircleImageView;
import com.miss.schoolmarket2.widgets.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private AutoCompleteTextView userId;
	private AutoCompleteTextView password;
	private CircleImageView picture;
	private Button load;
	private CheckBox rem_pw;
	private boolean netConnect;
	private String userNum;
	private String userPassWord;
	private String userPicPath;
	private SharedPreferences settings;
	private CustomProgressDialog progressDialog = null;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 设置页面布局
		setContentView(R.layout.activity_load);
		// 网络连接状态
		netConnect = NetConnect.isNetworkAvailable(LoadActivity.this);
		netToast();
		// 设置初始化视图
		initView();
		// 设置事件监听器方法
		setListener();
		// 实现头像动画效果

	}

	private void netToast() {
		if (netConnect == false) {
			Toast.makeText(LoadActivity.this, "当前网络不可用,请连接网络",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 创建初始化视图的方法
	 */
	private void initView() {
		load = (Button) findViewById(R.id.activity_load_bnLoad);
		userId = (AutoCompleteTextView) findViewById(R.id.activity_load_xuehao);
		password = (AutoCompleteTextView) findViewById(R.id.activity_load_mima);
		picture = (CircleImageView) findViewById(R.id.activity_load_picture);
		rem_pw = (CheckBox) findViewById(R.id.activity_load_jizhu_mima);
		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		
		if (settings.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			System.out.println("配置文件" + settings.getString("userNum", ""));
			rem_pw.setChecked(true);
			userId.setText(settings.getString("userNum", ""));
			password.setText(settings.getString("userPassWord", ""));
			if (settings.getString("userPic", "") != null) {
				load.setClickable(true);
				load.setTextColor(Color.WHITE);
				userPicPath = settings.getString("userPic", "");
				Bitmap bit = BitmapFactory.decodeFile(userPicPath);
				((ImageView) picture).setImageBitmap(bit);
			}
		}

	}

	/**
	 * 设置事件的监听器的方法
	 */
	private void setListener() {

		// 监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {
					System.out.println("记住密码已选中");
					userNum = userId.getText().toString();
					userPassWord = password.getText().toString();
					savePreferences(userNum, userPassWord);

				} else {
					System.out.println("记住密码没有选中");
					settings.edit().putBoolean("ISCHECK", false).commit();
				}

			}
		});
		userId.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}

		});
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				load.setClickable(true);
				load.setTextColor(Color.WHITE);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				load.setClickable(true);
				load.setTextColor(Color.WHITE);
			}

		});

	}

	// 登陆按钮监听处理事件
	public void load(View source) {
		System.out.println("登陆");
		// 判断网络连接是否成功
		userNum = userId.getText().toString();
		userPassWord = password.getText().toString();
		if (userNum != "" && userPassWord != "" && netConnect == true) {
			// 登陆
			load(userNum, userPassWord);
			try {
				load(userNum, userPassWord);
			} catch (Exception e) {
				Toast.makeText(LoadActivity.this, "登陆失败", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	// 保存配置文件
	private void savePreferences(String userNum, String userPassWord) {
		// set preference
		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userNum", userNum);
		editor.putString("userPassWord", userPassWord);
		editor.putBoolean("ISCHECK", true);
		editor.commit();
	}

	public void load(String userNum, String userPassWord) {
		LoadTask loadTask = new LoadTask(this);
		loadTask.execute(userNum, userPassWord);
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);

		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	// 判断username与password是否与已经注册的信息吻合
	class LoadTask extends AsyncTask<String, Void, String> {
		Context context;

		public LoadTask(Activity ctx) {
			this.context = ctx;
			startProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {// 处理后台执行的任务，在后台线程执行
			String url;
			String result = null;
			String url_constant = IP.IpLoad + "XuptMarket/userlogincl?";
			url = url_constant + "userNum=" + params[0] + "&&userPassword="
					+ params[1];
		
			String resultBase64 = HttpUtils.connServerForResult(url);
			if(resultBase64!=null){
			
			try {
				result = HttpUtils.decode(resultBase64);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("----->转码有误");
				e.printStackTrace();
			}
			}else{
				result = "";
				
			}
			System.out.println("----->转码后" + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行
			// 配置信息操作
			if(result!=""){
			UserInfor userInfor = HttpUtils.parseJsonTouserInfor(result);
			settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("userId", userInfor.getuserId());
			editor.putString("userName", userInfor.getuserName());
			editor.commit();
			stopProgressDialog();
			Intent intent = new Intent(LoadActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			}
			else{
				Toast toast = Toast.makeText(LoadActivity.this, "密码错误",
						Toast.LENGTH_SHORT);
				toast.show();
				stopProgressDialog();
			}
		}
	}
}
