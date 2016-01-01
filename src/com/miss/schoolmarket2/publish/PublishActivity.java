package com.miss.schoolmarket2.publish;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miss.schoolmaket2.myapplication.MyApplication;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.IndexInforAdapter;
import com.miss.schoolmarket2.db.DBManager;
import com.miss.schoolmarket2.entity.IndexInfor;
import com.miss.schoolmarket2.home.HomeActivity;
import com.miss.schoolmarket2.index.IndexActivity;
import com.miss.schoolmarket2.person.MyPublishActivity;
import com.miss.schoolmarket2.publish.until.Bimp;
import com.miss.schoolmarket2.publish.until.FileUtils;
import com.miss.schoolmarket2.until.HttpUtils;
import com.miss.schoolmarket2.until.IP;
import com.miss.schoolmarket2.until.Preferences;
import com.miss.schoolmarket2.until.Signal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PublishActivity extends Activity implements OnItemClickListener {

	private GridView gridview;
	private GridAdapter adapter;
	private Button publish;
	private EditText goods_name, goods_describe, goods_price, goods_connect;
	private String goodsName, goodsDescribe, goodsPrice, goodsConnect,
			goodsType;
	private String goodsTypeId;
	private Button selectimg_bt_content_type, selectimg_bt_search;
	private ScrollView activity_selectimg_scrollView;
	private HorizontalScrollView selectimg_horizontalScrollView;
	private List<String> categoryList;
	private float dp;
	// Spinner����
	private Spinner sp;
	String[] ls;
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> spadapter;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	private static Handler handler = new Handler();
	// 2015/2/28
	List<String> urList = new ArrayList<String>();
	private String userId;
	private String goodsId;
	private SharedPreferences settings;
	private DBManager dbManager;
	public List<String> goodsPicPith = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		MyApplication.getInstance().addActivity(this);

		SpinnerInit();
		Init();

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PublishActivity.this,
				HomeActivity.class);
		startActivity(intent);
		this.finish();

	}

	public void SpinnerInit() {
		// Spinner����
		sp = (Spinner) findViewById(R.id.sp);
		// ��ȡ��Ӧ����
		ls = getResources().getStringArray(R.array.action);
		// ��ȡXML�ж��������
		for (int i = 0; i < ls.length; i++) {
			list.add(ls[i]);
		}
		// �����鵼�뵽ArrayList��
		spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ���������˵��ķ��
		sp.setAdapter(spadapter);
		// ��������

	}

	public void Init() {
		dp = getResources().getDimension(R.dimen.dp);
		goods_name = (EditText) findViewById(R.id.goods_name);
		goods_name.setFocusable(true);
		goods_name.setFocusableInTouchMode(true);
		goods_price = (EditText) findViewById(R.id.goods_price);
		goods_price.setFocusable(true);
		goods_price.setFocusableInTouchMode(true);
		goods_connect = (EditText) findViewById(R.id.goods_connect);
		goods_connect.setFocusable(true);
		goods_connect.setFocusableInTouchMode(true);
		goods_describe = (EditText) findViewById(R.id.goods_describe);
		goods_describe.setFocusable(true);
		goods_describe.setFocusableInTouchMode(true);
		selectimg_horizontalScrollView = (HorizontalScrollView) findViewById(R.id.selectimg_horizontalScrollView);
		gridview = (GridView) findViewById(R.id.noScrollgridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridviewInit();

		// �ϴ���Ʒ��Ϣ��ͼƬ
		publish = (Button) findViewById(R.id.activity_publish);
		activity_selectimg_scrollView = (ScrollView) findViewById(R.id.activity_selectimg_scrollView);
		activity_selectimg_scrollView.setVerticalScrollBarEnabled(false);

		final View decorView = getWindow().getDecorView();
		final WindowManager wm = this.getWindowManager();

		decorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						@SuppressWarnings("deprecation")
						int displayheight = wm.getDefaultDisplay().getHeight();
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);
						int dynamicHight = rect.bottom - rect.top;
						float ratio = (float) dynamicHight
								/ (float) displayheight;

					}
				});

	}

	public void publish(View source) {

		
		if (bmp.size() == 3){
		goodsName = goods_name.getText().toString();
		goodsDescribe = goods_describe.getText().toString();
		goodsPrice = goods_price.getText().toString();
		goodsConnect = goods_connect.getText().toString();
		goodsType = sp.getSelectedItem().toString();
		if (goodsType.equals("�鼮����")) {
			goodsTypeId = "1";
		}
		if (goodsType.equals("�������")) {
			goodsTypeId = "2";
		}
		if (goodsType.equals("��ͨ����")) {
			goodsTypeId = "3";
		}
		if (goodsType.equals("������Ʒ")) {
			goodsTypeId = "4";
		}
		if (goodsType.equals("������Ʒ")) {
			goodsTypeId = "5";
		}

		if (goodsType.equals("�������")) {
			goodsTypeId = "6";
		}
		if (goodsType.equals("�������")) {
			goodsTypeId = "7";
		}
		if (goodsType.equals("����")) {
			goodsTypeId = "8";
		}

		if (goodsName.equals("") || goodsDescribe.equals("")
				|| goodsPrice.equals("") || goodsConnect.equals("")
				|| goodsType.equals("")) {
			Toast.makeText(getApplicationContext(), "���������ݲ���Ϊ��",
					Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < drr.size(); i++) {
			urList.add(drr.get(i));
		}
		settings = this.getSharedPreferences(Preferences.PREFS_NAME, 0);
		System.out.println("�õ����û�id" + settings.getString("userId", ""));
		userId = settings.getString("userId", "");
		System.out.println("--->������Ʒ��Ϣ" + goodsName + goodsDescribe
				+ goodsPrice + goodsConnect + goodsTypeId + goodsTypeId);
		refresh(userId, goodsName, goodsDescribe, goodsTypeId, goodsPrice,
				goodsConnect);
		// ��ת���ɹ�����
		}else{
			Toast.makeText(getApplicationContext(), "��Ҫ����ͼƬ",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void loadImage(String iconpath, String goodsId, final String path,
			final ProgressDialog pdialog) {
		final String goodsIdtemp = goodsId;
		// urList.get(0)
		final AsyncHttpClient client = new AsyncHttpClient();
		final RequestParams params = new RequestParams();
		try {

			params.put("goodsId", goodsIdtemp);
			File image = new File(iconpath);
			params.put("image", image);
			client.post(path, params, new AsyncHttpResponseHandler() {
				public void onSuccess(String arg0) { // ��ȡ���ݳɹ����������
					System.out.println("������ƷͼƬ1���");
					params.put("goodsId", goodsIdtemp);
					File image = new File(urList.get(1));
					try {
						params.put("image", image);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("������ƷͼƬ2����");
						e.printStackTrace();
					}
					client.post(path, params, new AsyncHttpResponseHandler() {
						public void onSuccess(String arg0) { // ��ȡ���ݳɹ����������
							System.out.println("������ƷͼƬ2���");
							params.put("goodsId", goodsIdtemp);
							File image = new File(urList.get(2));
							try {
								params.put("image", image);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								System.out.println("������ƷͼƬ3����");
								e.printStackTrace();
							}
							client.post(path, params,
									new AsyncHttpResponseHandler() {
										public void onSuccess(String arg0) { // ��ȡ���ݳɹ����������
											System.out.println("������ƷͼƬ3���");
											// ��ӵ����ݿ���
											String picPath = FileUtils.savePic(
													urList.get(0), goodsIdtemp);
											dbManager = new DBManager(
													PublishActivity.this);
											dbManager.add(goodsIdtemp,
													goodsName, goodsDescribe,
													goodsPrice, picPath);
											pdialog.dismiss();

											Intent intent = new Intent(
													PublishActivity.this,
													PublishsuccActivity.class);
											startActivity(intent);
											finish();
										}
									});
						}
					});
				}
			});

		} catch (Exception e) {
			System.out.println("������ƷͼƬ����");
			e.printStackTrace();
		}

	}

	public void refresh(String a, String b, String c, String d, String e,
			String f) {
		PublishGoodsDataTask publishGoodsDataTask = new PublishGoodsDataTask(
				this);
		publishGoodsDataTask.execute(a, b, c, d, e, f);
	}

	class PublishGoodsDataTask extends AsyncTask<String, Void, String> {
		Context context;
		private ProgressDialog pdialog;

		public PublishGoodsDataTask(Activity ctx) {
			this.context = ctx;
			pdialog = ProgressDialog.show(context, "���ڷ���...", "ϵͳ���ڴ�����������");
		}

		@Override
		protected String doInBackground(String... params) {// �����ִ̨�е������ں�̨�߳�ִ��
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/uploadgoodsinfocl?";
			url = url_constant + "userId=" + params[0] + "&&goodsName="
					+ params[1] + "&&goodsDescribe=" + params[2]
					+ "&&goodsType=" + params[3] + "&&goodsPrice=" + params[4]
					+ "&&goodsConnect=" + params[5];
			String result = HttpUtils.connServerForResult(url);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {// ��̨����ִ����֮�󱻵��ã���ui�߳�ִ��

			try {
				String str = HttpUtils.decode(result);
				str = str.trim();
				String str2 = "";
				if (str != null && !"".equals(str)) {
					for (int i = 0; i < str.length(); i++) {
						if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
							str2 += str.charAt(i);
						}
					}

				}
				goodsId = str2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("----->ת������");
				e.printStackTrace();
			}

			System.out.println("ת���goodsId " + goodsId);
			// ͼƬ��ַ urList��
			String path = IP.IpLoad + "XuptMarket/uploadgoodsimagecl";
			// String path =
			// "http://192.168.252.1:8080/myService/UploadFileServlet";
			if (urList != null && goodsId != "") {

				loadImage(urList.get(0), goodsId, path, pdialog);

			}

		}

	}

	public void gridviewInit() {
		adapter = new GridAdapter(this);
		adapter.setSelectedPosition(0);
		int size = 0;
		if (bmp.size() < 3) {
			size = bmp.size() + 1;
		} else {
			size = bmp.size();
		}
		LayoutParams params = gridview.getLayoutParams();
		final int width = size * (int) (dp * 9.4f);
		params.width = width;
		gridview.setLayoutParams(params);
		gridview.setColumnWidth((int) (dp * 9.4f));
		gridview.setStretchMode(GridView.NO_STRETCH);
		gridview.setNumColumns(size);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);

		selectimg_horizontalScrollView.getViewTreeObserver()
				.addOnPreDrawListener(// �������
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								selectimg_horizontalScrollView.scrollTo(width,
										0);
								selectimg_horizontalScrollView
										.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		// temp=comment_content.getText().toString().trim();
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater listContainer;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public class ViewHolder {
			public ImageView image;
			public Button bt;
		}

		public GridAdapter(Context context) {
			listContainer = LayoutInflater.from(context);
		}

		public int getCount() {
			if (bmp.size() < 6) {
				return bmp.size() + 1;
			} else {
				return bmp.size();
			}
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item����
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			// �Զ�����ͼ
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// ��ȡlist_item�����ļ�����ͼ

				convertView = listContainer.inflate(
						R.layout.publish_gridview_item, null);

				// ��ȡ�ؼ�����
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.bt = (Button) convertView
						.findViewById(R.id.item_grida_bt);
				// ���ÿؼ�����convertView
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				holder.bt.setVisibility(View.GONE);
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(bmp.get(position));
				holder.bt.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						PhotoActivity.bitmap.remove(sign);
						bmp.get(sign).recycle();
						bmp.remove(sign);
						drr.remove(sign);

						gridviewInit();
					}
				});
			}

			return convertView;
		}
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			super(mContext);
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent i = new Intent(
							// ���
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private static final int SELECTIMG_SEARCH = 3;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// ��sd�����Ƿ���myImage�ļ���
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// �Ƿ���headImg�ļ�
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (drr.size() < 6 && resultCode == -1) {// ����
				startPhotoZoom(photoUri);
			}
			break;
		case RESULT_LOAD_IMAGE:
			if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {// ��᷵��
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// �ü�����
				Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
				PhotoActivity.bitmap.add(bitmap);
				bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
						(int) (dp * 1.6f));
				bmp.add(bitmap);
				gridviewInit();
			}
			break;
		case SELECTIMG_SEARCH:
			if (resultCode == RESULT_OK && null != data) {
				String text = "#" + data.getStringExtra("topic") + "#";
				text = goods_name.getText().toString() + text;
				goods_name.setText(text);

				goods_name.getViewTreeObserver().addOnPreDrawListener(
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								goods_name.setEnabled(true);
								// ���ù��Ϊĩβ
								CharSequence cs = goods_name.getText();
								if (cs instanceof Spannable) {
									Spannable spanText = (Spannable) cs;
									Selection.setSelection(spanText,
											cs.length());
								}
								goods_name.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});

			}

			break;
		}
	}

	private void startPhotoZoom(Uri uri) {
		try {
			// ��ȡϵͳʱ�� Ȼ�󽫲ü����ͼƬ������ָ�����ļ���
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyyMMddhhmmss");
			final String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");

			}

			// ͼƬ��������

			drr.add(FileUtils.SDPATH + 17 + "-" + address + ".JPEG");
			Uri imageUri = Uri.parse("file:///sdcard/formats/" + 17 + "-"
					+ address + ".JPEG");

			final Intent intent = new Intent("com.android.camera.action.CROP");

			// ��ƬURL��ַ
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 480);
			intent.putExtra("outputY", 480);
			// ���·��
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// �����ʽ
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			// ����������ʶ��
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", false);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onDestroy() {

		FileUtils.deleteDir(FileUtils.SDPATH);
		FileUtils.deleteDir(FileUtils.SDPATH1);
		// ����ͼƬ����
		for (int i = 0; i < bmp.size(); i++) {
			bmp.get(i).recycle();
		}
		for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
			PhotoActivity.bitmap.get(i).recycle();
		}
		PhotoActivity.bitmap.clear();
		bmp.clear();
		drr.clear();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(PublishActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (arg2 == bmp.size()) {
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				new PopupWindows(PublishActivity.this, gridview);
			} else {
				Toast.makeText(getApplicationContext(), "sdcard�Ѱγ�������ѡ����Ƭ",
						Toast.LENGTH_SHORT).show();
			}
		} else {

			Intent intent = new Intent(PublishActivity.this,
					PhotoActivity.class);

			intent.putExtra("ID", arg2);
			startActivity(intent);
		}
	}

}
