package com.miss.schoolmarket2.adapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.miss.schoolmarket2.LoadActivity;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.db.DBManager;
import com.miss.schoolmarket2.entity.GoodsInfor;
import com.miss.schoolmarket2.entity.UserInfor;
import com.miss.schoolmarket2.home.HomeActivity;
import com.miss.schoolmarket2.person.MyPublishActivity;
import com.miss.schoolmarket2.until.HttpUtils;
import com.miss.schoolmarket2.until.IP;
import com.miss.schoolmarket2.until.Preferences;
import com.miss.schoolmarket2.until.Signal;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class DeletableAdapter extends BaseAdapter{
	private DBManager dbManager;
	private Context context;
	private ArrayList<GoodsInfor> goodsInfor;
	public DeletableAdapter(Context context,ArrayList<GoodsInfor> goodsInfor){
		this.context = context;
		this.goodsInfor=goodsInfor;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goodsInfor.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return goodsInfor.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int index=position;
		View view=convertView;
		if(view==null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view=inflater.inflate(R.layout.activity_mypublish_list_item, null);
		}
		//System.out.println("我的花园植物图片路径--->"+flowerInfor.get(1).getflowerPicPath().toString());
		final ImageView imageView1 = (ImageView)view.findViewById(R.id.activity_mypublish_goodsImage1);
		Bitmap bit = decodeFile1(new File(goodsInfor.get(position).goodsPicAD1.toString()));
		imageView1.setImageBitmap(bit);
		final TextView textgoodsName=(TextView)view.findViewById(R.id.activity_mypublish_goodsName);
		textgoodsName.setText(goodsInfor.get(position).goodsName.toString());
		final TextView textgoodsDescribe=(TextView)view.findViewById(R.id.activity_mypublish_goodsDescribe);
		textgoodsDescribe.setText(goodsInfor.get(position).goodsDescribe.toString());
		final TextView textgoodsPrice=(TextView)view.findViewById(R.id.activity_mypublish_goodsPrice);
		textgoodsPrice.setText(goodsInfor.get(position).goodsPrice.toString()+"￥");
		final ImageView imageView=(ImageView)view.findViewById(R.id.activity_mypublish_goodsdelet);
		//imageView.setBackgroundResource(android.R.drawable.ic_delete);
		imageView.setTag(position);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dbManager = new DBManager(context);
                dbManager.delData(goodsInfor.get(index).goodsName.toString());
                refresh(goodsInfor.get(index).goodsId.toString());
                goodsInfor.remove(index);
				notifyDataSetChanged();
				
				Toast.makeText(context, textgoodsName.getText().toString(), Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
	public void refresh(String goodsId)
	{
		DeletTask task = new DeletTask(context);
		task.execute(goodsId);
	}
	private Bitmap decodeFile1(File f){
        try {
            
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
	class DeletTask extends AsyncTask<String, Void, String> {
		Context context;
		private ProgressDialog pdialog;

		public DeletTask(Context ctx) {
			this.context = ctx;
			pdialog = ProgressDialog.show(context, "正在删除...", "系统正在处理您的请求");
		}

		@Override
		protected String doInBackground(String... params) {// 处理后台执行的任务，在后台线程执行
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/delgoodscl?";
			url = url_constant + "goodsId=" + params[0]; 
				
			String resultBase64 = HttpUtils.connServerForResult(url);
			String result = null;
			try {
				result = HttpUtils.decode(resultBase64);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("----->转码有误");
				e.printStackTrace();
			}
			System.out.println("----->删除转码后"+result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行
			// 配置信息操作
			//if(result = "["Response":"already_del"]")
			
			pdialog.dismiss();
			if(Signal.signal>0){
			Signal.signal--;
			}
			
		}
	}
}
