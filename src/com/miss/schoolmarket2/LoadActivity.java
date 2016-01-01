//��½���棬��ȡ�û���д��ѧ�ź����룬�ͷ��������н���������{"UserNum": "04121093",
//	"UserPassword": "04121093"}���ҽ������ص�json ,�жϵ�½�Ƿ�ɹ������粻�ɹ�
//��ʾ���µ�¼����½�ɹ���ת����һ���档
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
		// ����ҳ�沼��
		setContentView(R.layout.activity_load);
		// ��������״̬
		netConnect = NetConnect.isNetworkAvailable(LoadActivity.this);
		netToast();
		// ���ó�ʼ����ͼ
		initView();
		// �����¼�����������
		setListener();
		// ʵ��ͷ�񶯻�Ч��

	}

	private void netToast() {
		if (netConnect == false) {
			Toast.makeText(LoadActivity.this, "��ǰ���粻����,����������",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ������ʼ����ͼ�ķ���
	 */
	private void initView() {
		load = (Button) findViewById(R.id.activity_load_bnLoad);
		userId = (AutoCompleteTextView) findViewById(R.id.activity_load_xuehao);
		password = (AutoCompleteTextView) findViewById(R.id.activity_load_mima);
		picture = (CircleImageView) findViewById(R.id.activity_load_picture);
		rem_pw = (CheckBox) findViewById(R.id.activity_load_jizhu_mima);
		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		
		if (settings.getBoolean("ISCHECK", false)) {
			// ����Ĭ���Ǽ�¼����״̬
			System.out.println("�����ļ�" + settings.getString("userNum", ""));
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
	 * �����¼��ļ������ķ���
	 */
	private void setListener() {

		// ������ס�����ѡ��ť�¼�
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {
					System.out.println("��ס������ѡ��");
					userNum = userId.getText().toString();
					userPassWord = password.getText().toString();
					savePreferences(userNum, userPassWord);

				} else {
					System.out.println("��ס����û��ѡ��");
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

	// ��½��ť���������¼�
	public void load(View source) {
		System.out.println("��½");
		// �ж����������Ƿ�ɹ�
		userNum = userId.getText().toString();
		userPassWord = password.getText().toString();
		if (userNum != "" && userPassWord != "" && netConnect == true) {
			// ��½
			load(userNum, userPassWord);
			try {
				load(userNum, userPassWord);
			} catch (Exception e) {
				Toast.makeText(LoadActivity.this, "��½ʧ��", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	// ���������ļ�
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

	// �ж�username��password�Ƿ����Ѿ�ע�����Ϣ�Ǻ�
	class LoadTask extends AsyncTask<String, Void, String> {
		Context context;

		public LoadTask(Activity ctx) {
			this.context = ctx;
			startProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {// �����ִ̨�е������ں�̨�߳�ִ��
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
				System.out.println("----->ת������");
				e.printStackTrace();
			}
			}else{
				result = "";
				
			}
			System.out.println("----->ת���" + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {// ��̨����ִ����֮�󱻵��ã���ui�߳�ִ��
			// ������Ϣ����
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
				Toast toast = Toast.makeText(LoadActivity.this, "�������",
						Toast.LENGTH_SHORT);
				toast.show();
				stopProgressDialog();
			}
		}
	}
}
