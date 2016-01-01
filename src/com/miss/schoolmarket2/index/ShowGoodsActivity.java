package com.miss.schoolmarket2.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.ImageAdapter;
import com.miss.schoolmarket2.index.until.GuideGallery;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

public class ShowGoodsActivity extends Activity {
	public List<String> urls;
	public GuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	Uri uri;
	Intent intent;
	int gallerypisition = 0;
	private PopupWindow mPopupWindow;
	public Button bn;
	private TextView goodsNameT;
	private TextView goodsPriceT;
	private TextView goodsDescribeT;
	public ArrayList<HashMap<String, String>> data;
	private int listNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showgoods);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		data = (ArrayList<HashMap<String, String>>) bundle
				.getSerializable("goodslist");
		listNum = bundle.getInt("listNum");
		init();
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
		popwindow();
	}
public void ShowGoodsBack (View source) {
	this.onBackPressed();
	this.finish();
}
	public void Connect(View source) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		System.out.println("电话号码"
				+ data.get(listNum).get("goodsConnect").toString());
		intent.setData(Uri.parse("tel:"
				+ data.get(listNum).get("goodsConnect").toString()));

		ShowGoodsActivity.this.startActivity(intent);
	}

	public void popwindow() {
		View popupView = getLayoutInflater().inflate(
				R.layout.layout_popupwindow, null);

		TextView personGrade = (TextView) popupView
				.findViewById(R.id.lainxigrade);

		personGrade.setText(data.get(listNum).get("goodsConnect").toString());
		bn = (Button) findViewById(R.id.wantedgoods);
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
		mPopupWindow.getContentView().setFocusableInTouchMode(true);
		mPopupWindow.getContentView().setFocusable(true);
		bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (mPopupWindow != null && !mPopupWindow.isShowing()) {
					mPopupWindow.showAtLocation(findViewById(R.id.sortdetails),
							Gravity.BOTTOM, 0, 0);

				}
			}
		});
		mPopupWindow.getContentView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU
						&& event.getRepeatCount() == 0
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mPopupWindow != null && mPopupWindow.isShowing()) {
						mPopupWindow.dismiss();
					}
					return true;
				}
				return false;
			}
		});
	}

	private void init() {

		goodsNameT = (TextView) findViewById(R.id.desgoodsname);
		goodsPriceT = (TextView) findViewById(R.id.desgoodsprice);
		goodsDescribeT = (TextView) findViewById(R.id.desgoods);
		//goodsInfoSpeak = (Button) findViewById(R.id.goodsInfoSpeak);
		goodsNameT.setText(data.get(listNum).get("bookName").toString());
		goodsPriceT.setText(data.get(listNum).get("bookMoney").toString());
		goodsDescribeT.setText(data.get(listNum).get("bookIntroduce")
				.toString());
		images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);
		images_ga.setSortDetailsActivity(this);
		ImageAdapter imageAdapter = new ImageAdapter(ShowGoodsActivity.this,
				data.get(listNum));
		images_ga.setAdapter(imageAdapter);
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		//pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
		pointLinear.setBackgroundColor(Color.WHITE);
		for (int i = 0; i < 3; i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}

	}

	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timeFlag = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timeTaks.timeCondition = false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
			if (mPopupWindow != null && !mPopupWindow.isShowing()) {
				mPopupWindow.showAtLocation(findViewById(R.id.sortdetails),
						Gravity.BOTTOM, 0, 0);
				System.out.println("------>点击了");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				gallerypisition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();// 存放数据
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;// 消息标识
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	Timer autoGallery = new Timer();

}
