package com.miss.schoolmarket2.until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetConnect
{
	/** 
     * ��⵱�����磨WLAN��3G/2G��״̬ 
     * @param context Context 
     * @return true ��ʾ������� 
     */  
    public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();  
            if (info != null && info.isConnected())   
            {  
                // ��ǰ���������ӵ�  
                if (info.getState() == NetworkInfo.State.CONNECTED)   
                {  
                    // ��ǰ�����ӵ��������  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
}