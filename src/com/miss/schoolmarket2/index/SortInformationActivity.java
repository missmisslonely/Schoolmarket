package com.miss.schoolmarket2.index;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.IndexInforAdapter;
import com.miss.schoolmarket2.entity.IndexInfor;

import com.miss.schoolmarket2.until.HttpUtils;
import com.miss.schoolmarket2.until.IP;
import com.miss.schoolmarket2.view.XListView;
import com.miss.schoolmarket2.view.XListView.IXListViewListener;
import com.miss.schoolmarket2.widgets.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SortInformationActivity extends Activity implements
		IXListViewListener {
	private XListView listView1;
	private CustomProgressDialog progressDialog = null;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private IndexInforAdapter adapter;
	private Handler mHandler1;
	private TextView topSort;
	private String goodsTypeId;
	private String page = "1";
	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sortinfor);

		setTextView();
		init();
		refresh();
		mHandler1 = new Handler();

	}

	private void init() {
		listView1 = (XListView) findViewById(R.id.activity_sort_listView);
		listView1.setPullLoadEnable(true);
		listView1.setXListViewListener(this);
		listView1.setOnItemClickListener(new ListViewListener());
	}

	// 点击物品信息，进入物品信息的详细描述界面
	class ListViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			bundle.putSerializable("goodslist", list);
			bundle.putInt("listNum", arg2 - 1);
			intent.putExtras(bundle);
			intent.setClass(SortInformationActivity.this,
					ShowGoodsActivity.class);
			startActivity(intent);
		}

	}

	private void setTextView() {
		Intent intent = getIntent();
		String string = intent.getStringExtra("name");
		topSort = (TextView) findViewById(R.id.title_sort_text);

		if (string.equals(getResources().getString(R.string.book))) {
			topSort.setText(getResources().getString(R.string.book));
			goodsTypeId = "1";

		} else if (string.equals(getResources().getString(R.string.digital))) {
			topSort.setText(getResources().getString(R.string.digital));
			goodsTypeId = "2";
		} else if (string.equals(getResources().getString(R.string.transport))) {
			topSort.setText(getResources().getString(R.string.transport));
			goodsTypeId = "3";
		} else if (string.equals(getResources().getString(R.string.dailyuse))) {
			topSort.setText(getResources().getString(R.string.dailyuse));
			goodsTypeId = "4";
		} else if (string.equals(getResources().getString(R.string.pe_use))) {
			topSort.setText(getResources().getString(R.string.pe_use));
			goodsTypeId = "5";
		} else if (string.equals(getResources().getString(R.string.clothesuse))) {
			topSort.setText(getResources().getString(R.string.clothesuse));
			goodsTypeId = "6";
		} else if (string.equals(getResources().getString(R.string.freegive))) {
			topSort.setText(getResources().getString(R.string.freegive));
			goodsTypeId = "7";
		} else if (string.equals(getResources().getString(R.string.other))) {
			topSort.setText(getResources().getString(R.string.other));
			goodsTypeId = "8";
		}
	}

	public void refresh() {
		SortInforTask sortInforTask = new SortInforTask(this);
		sortInforTask.execute(goodsTypeId, page);
	}

	public void sortBack(View source) {
		this.onBackPressed();
	}
	private void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
	    	
		}
		
    	progressDialog.show();
	}
	
	private void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	// 异步加载数据
	class SortInforTask extends AsyncTask<String, Void, String> {
		Context context;
		

		public SortInforTask(Activity ctx) {
			this.context = ctx;
			startProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {// 处理后台执行的任务，在后台线程执行
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/classifiedsearchcl?";
			url = url_constant + "goodsTypeId=" + params[0] + "&&page="
					+ params[1];
			String result = HttpUtils.connServerForResult(url);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行

			List<IndexInfor> listIndexInfor = HttpUtils
					.parseJsonToIndexInfo(result);
			list = getList(listIndexInfor);
			adapter = new IndexInforAdapter(SortInformationActivity.this, list);

			listView1.setAdapter(adapter);
			stopProgressDialog();

		}

	}

	public ArrayList<HashMap<String, String>> getList(
			List<IndexInfor> listIndexInfor) {
		ArrayList<HashMap<String, String>> list;

		list = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> map;

		for (int i = 0; i < listIndexInfor.size(); i++) {
			map = new HashMap<String, String>();
			String uri0 = null;
			String uri1 = "";
			String uri2 = "";
			if (listIndexInfor.get(i).list[0] != null) {

				uri0 = listIndexInfor.get(i).list[0];
				if (listIndexInfor.get(i).list.length > 1) {
					uri1 = listIndexInfor.get(i).list[1];
					System.out.println("第二张照片地址" + uri1);
					uri2 = listIndexInfor.get(i).list[2];
				}

				map.put("icon", uri0);
				map.put("icon1", uri1);
				map.put("icon2", uri2);
				map.put("bookName", listIndexInfor.get(i).getGoodsName());
				map.put("bookIntroduce", listIndexInfor.get(i)
						.getGoodsDescribe());
				map.put("bookMoney", "￥"
						+ listIndexInfor.get(i).getGoodsPrice()+"元");

				map.put("goodsConnect", listIndexInfor.get(i).getGoodsConnect());
				list.add(map);
			}
		}
		return list;
	}

	// 下拉刷新需要再次请求最新一页
	@Override
	public void onRefresh() {
		mHandler1.post(new Runnable() {
			@Override
			public void run() {
				refresh();
				onLoad();
				System.out.println("----->下拉刷新");
			}
		});
	}

	@Override
	public void onLoadMore() {
		mHandler1.post(new Runnable() {
			@Override
			public void run() {
				System.out.println("----->加载更多");
				geneItems();

				adapter = new IndexInforAdapter

				(SortInformationActivity.this, list);
				listView1.setAdapter(adapter);
				onLoad();
			}
		});

	}

	// 分页加载数据
	public class MyThread1 extends Thread {
		public void run() {
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/classifiedsearchcl?";
			url = url_constant + "goodsTypeId=" + goodsTypeId + "&&page="
					+ pageNum++;
			String result = HttpUtils.connServerForResult(url);

			List<IndexInfor> listIndexInfor = HttpUtils
					.parseJsonToIndexInfo(result);
			ArrayList<HashMap<String, String>> list1 = getList(listIndexInfor);
			if (list != null) {
				list.addAll(list1);
			}

		}
	}

	private void geneItems() {
		new MyThread1().start();

	}

	private void onLoad() {
		listView1.stopRefresh();
		listView1.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		listView1.setRefreshTime(str);
	}

}
