package com.miss.schoolmarket2.adapter;


import java.util.HashMap;
import java.util.List;

import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.index.ShowGoodsActivity;
import com.miss.schoolmarket2.index.until.ImageLoaderHD;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter{  
    private List<String> imageUrls;       //图片地址list  
     
    private ImageAdapter self;
    Uri uri;
    Intent intent;
    ImageView imageView;
    public ImageLoaderHD imageLoaderHD;
    private HashMap<String, String> map = new HashMap<String, String>();
    private  String[] imgs = {"","",""} ;
    
    private Activity activity;
    public ImageAdapter(Activity activity,HashMap<String, String> map) {  
       
        this.activity = activity;  
        this.self = this;
        this.map = map;
        
        imageLoaderHD=new ImageLoaderHD(activity.getApplicationContext());
      
        
    }  
  
    public int getCount() {  
        return Integer.MAX_VALUE;  
    }  
  
    public Object getItem(int position) {  
    	return position; 
    }  
   
    public long getItemId(int position) {  
        return position;  
    }  
  
    @SuppressWarnings("unused")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0: {
						self.notifyDataSetChanged();
					}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};
    
    public View getView(int position, View convertView, ViewGroup parent) {  
    	
    	if(map.get("icon").toString() != null)
    	{
    		imgs[0] = map.get("icon").toString();
        	imgs[1] = map.get("icon1").toString();
        	imgs[2] = map.get("icon2").toString();
    	}
    	
        //Bitmap image;  
        if(convertView==null){  
            convertView = LayoutInflater.from(activity).inflate(R.layout.activity_showgoods_item,null); //实例化convertView  
            Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT,Gallery.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(params);
          
            convertView.setTag("");  
  
        }  
      
       
         imageView = (ImageView) convertView.findViewById(R.id.gallery_image);  
         
         imageLoaderHD.DisplayImage(imgs[position%3], imageView);
     
        //设置缩放比例：保持原样  
        imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
      
        ((ShowGoodsActivity)activity).changePointView(position % 3);
        
        return convertView;  
        
    }  
  
   
}  
