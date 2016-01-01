package com.miss.schoolmarket2.until;

import android.content.Context;
import android.content.SharedPreferences;

public class Signal
{
	public static  int signal=0 ;
	public Signal(Context context,int signal)
	{
		SharedPreferences settings;
		settings = context.getSharedPreferences(Preferences.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("signal", signal);
		editor.commit();
	}
}