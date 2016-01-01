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
	// Spinner设置
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
		// Spinner设置
		sp = (Spinner) findViewById(R.id.sp);
		// 获取相应对象
		ls = getResources().getStringArray(R.array.action);
		// 获取XML中定义的数组
		for (int i = 0; i < ls.length; i++) {
			list.add(ls[i]);
		}
		// 把数组导入到ArrayList中
		spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉菜单的风格
		sp.setAdapter(spadapter);
		// 绑定适配器

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

		// 上传物品信息和图片
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
		if (goodsType.equals("书籍报刊")) {
			goodsTypeId = "1";
		}
		if (goodsType.equals("数码电子")) {
			goodsTypeId = "2";
		}
		if (goodsType.equals("交通工具")) {
			goodsTypeId = "3";
		}
		if (goodsType.equals("生活用品")) {
			goodsTypeId = "4";
		}
		if (goodsType.equals("体育用品")) {
			goodsTypeId = "5";
		}

		if (goodsType.equals("衣物服饰")) {
			goodsTypeId = "6";
		}
		if (goodsType.equals("免费赠送")) {
			goodsTypeId = "7";
		}
		if (goodsType.equals("其他")) {
			goodsTypeId = "8";
		}

		if (goodsName.equals("") || goodsDescribe.equals("")
				|| goodsPrice.equals("") || goodsConnect.equals("")
				|| goodsType.equals("")) {
			Toast.makeText(getApplicationContext(), "发布的内容不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < drr.size(); i++) {
			urList.add(drr.get(i));
		}
		settings = this.getSharedPreferences(Preferences.PREFS_NAME, 0);
		System.out.println("得到的用户id" + settings.getString("userId", ""));
		userId = settings.getString("userId", "");
		System.out.println("--->发布物品信息" + goodsName + goodsDescribe
				+ goodsPrice + goodsConnect + goodsTypeId + goodsTypeId);
		refresh(userId, goodsName, goodsDescribe, goodsTypeId, goodsPrice,
				goodsConnect);
		// 跳转到成功界面
		}else{
			Toast.makeText(getApplicationContext(), "需要三张图片",
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
				public void onSuccess(String arg0) { // 获取数据成功会调用这里
					System.out.println("发布物品图片1完成");
					params.put("goodsId", goodsIdtemp);
					File image = new File(urList.get(1));
					try {
						params.put("image", image);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("发布物品图片2有误");
						e.printStackTrace();
					}
					client.post(path, params, new AsyncHttpResponseHandler() {
						public void onSuccess(String arg0) { // 获取数据成功会调用这里
							System.out.println("发布物品图片2完成");
							params.put("goodsId", goodsIdtemp);
							File image = new File(urList.get(2));
							try {
								params.put("image", image);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								System.out.println("发布物品图片3有误");
								e.printStackTrace();
							}
							client.post(path, params,
									new AsyncHttpResponseHandler() {
										public void onSuccess(String arg0) { // 获取数据成功会调用这里
											System.out.println("发布物品图片3完成");
											// 添加到数据库中
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
			System.out.println("发布物品图片有误");
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
			pdialog = ProgressDialog.show(context, "正在发布...", "系统正在处理您的请求");
		}

		@Override
		protected String doInBackground(String... params) {// 处理后台执行的任务，在后台线程执行
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
		protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行

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
				System.out.println("----->转码有误");
				e.printStackTrace();
			}

			System.out.println("转码后goodsId " + goodsId);
			// 图片地址 urList；
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
				.addOnPreDrawListener(// 绘制完毕
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
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 获取list_item布局文件的视图

				convertView = listContainer.inflate(
						R.layout.publish_gridview_item, null);

				// 获取控件对象
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.bt = (Button) convertView
						.findViewById(R.id.item_grida_bt);
				// 设置控件集到convertView
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
							// 相册
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
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
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
			if (drr.size() < 6 && resultCode == -1) {// 拍照
				startPhotoZoom(photoUri);
			}
			break;
		case RESULT_LOAD_IMAGE:
			if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {// 相册返回
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
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
								// 设置光标为末尾
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
			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyyMMddhhmmss");
			final String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");

			}

			// 图片名字设置

			drr.add(FileUtils.SDPATH + 17 + "-" + address + ".JPEG");
			Uri imageUri = Uri.parse("file:///sdcard/formats/" + 17 + "-"
					+ address + ".JPEG");

			final Intent intent = new Intent("com.android.camera.action.CROP");

			// 照片URL地址
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 480);
			intent.putExtra("outputY", 480);
			// 输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// 输出格式
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			// 不启用人脸识别
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
		// 清理图片缓存
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
				Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片",
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
