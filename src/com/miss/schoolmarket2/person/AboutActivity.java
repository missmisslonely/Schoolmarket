package com.miss.schoolmarket2.person;

import com.miss.schoolmarket2.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	
	public void AboutBack(View source)
	{
		this.onBackPressed();
	}

}
