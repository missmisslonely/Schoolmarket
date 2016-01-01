package com.miss.schoolmarket2.home;


import com.miss.schoolmaket2.myapplication.MyApplication;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.index.IndexActivity;
import com.miss.schoolmarket2.publish.PublishActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;

/**
 * 
 * TODO Intent切换页
 * 
 * 
 * 
 */
public class HomeActivity extends TabActivity implements OnClickListener {
	public static HomeActivity instance = null;
	public static String TAB_TAG_RECOM = "recom";
	public static String TAB_TAG_CATE = "cate";
	// public static String TAB_TAG_TOP = "top";
	public static String TAB_TAG_MY = "my";
	// public static String TAB_TAG_OFF = "off";

	private Intent intent_index, intent_publish, intent_person;
	private ImageView iv_xinxi, iv_fabu, iv_wode;
	private Animation left_in, left_out, right_in, right_out;
	private int curTab = R.id.ll_recom;
	private static TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		setContentView(R.layout.activity_home);
		initAnim();
		initIntent();
		initView();
		initTabHost();
	}

	// 退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
																				// back键
			finish();
		}
		return false;

	}

	/**
	 * 初始化Animation
	 */
	private void initAnim() {
		left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
		left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);
		right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}

	/**
	 * 初始化Intent
	 */
	private void initIntent() {
		intent_index = new Intent(this, IndexActivity.class);
		intent_publish = new Intent(this, PublishActivity.class);
		intent_person = new Intent(this, PersonGroupActivity.class);
	}

	private void initView() {
		findViewById(R.id.ll_recom).setOnClickListener(this);
		findViewById(R.id.ll_cate).setOnClickListener(this);

		findViewById(R.id.ll_my).setOnClickListener(this);
		// findViewById(R.id.ll_off).setOnClickListener(this);

		iv_xinxi = (ImageView) findViewById(R.id.xinxi);
		iv_xinxi.setImageResource(R.drawable.home_xinxi);
		iv_fabu = (ImageView) findViewById(R.id.fabu);

		iv_wode = (ImageView) findViewById(R.id.wode);
		// iv_off = (ImageView) findViewById(R.id.iv_off);
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return tabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	private void initTabHost() {
		tabHost = getTabHost();
		tabHost.addTab(buildTabSpec(TAB_TAG_RECOM, R.string.home_xinxi,
				R.drawable.home_xinxi_press, intent_index));
		tabHost.addTab(buildTabSpec(TAB_TAG_CATE, R.string.home_fabu,
				R.drawable.home_fabu_press, intent_publish));

		tabHost.addTab(buildTabSpec(TAB_TAG_MY, R.string.home_wode,
				R.drawable.home_wode_press, intent_person));

		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == curTab) {
			return;
		}
		iv_xinxi.setImageResource(R.drawable.home_xinxi_press);
		iv_fabu.setImageResource(R.drawable.home_fabu_press);
		// iv_top.setImageResource(R.drawable.phone_navi_top);
		iv_wode.setImageResource(R.drawable.home_wode_press);
		// iv_off.setImageResource(R.drawable.phone_navi_off);

		int checkedId = v.getId();
		final boolean or;
		if (checkedId < curTab) {
			or = true;
		} else {
			or = false;
		}
		if (or) {
			tabHost.getCurrentView().startAnimation(right_out);
		} else {
			tabHost.getCurrentView().startAnimation(left_out);
		}
		switch (checkedId) {
		case R.id.ll_recom:
			tabHost.setCurrentTabByTag(TAB_TAG_RECOM);
			iv_xinxi.setImageResource(R.drawable.home_xinxi);
			break;
		case R.id.ll_cate:
			tabHost.setCurrentTabByTag(TAB_TAG_CATE);
			iv_fabu.setImageResource(R.drawable.home_fabu);
			break;

		case R.id.ll_my:
			tabHost.setCurrentTabByTag(TAB_TAG_MY);
			iv_wode.setImageResource(R.drawable.home_wode);
			break;
		
		default:
			break;
		}
		if (or) {
			tabHost.getCurrentView().startAnimation(left_in);
		} else {
			tabHost.getCurrentView().startAnimation(right_in);
		}
		curTab = checkedId;
	}

}
