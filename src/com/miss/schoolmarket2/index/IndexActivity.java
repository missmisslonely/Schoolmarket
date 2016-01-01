//��ҳ�������Լ����·���
package com.miss.schoolmarket2.index;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.miss.schoolmaket2.myapplication.MyApplication;
import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.GridViewAdapter;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IndexActivity extends Activity implements IXListViewListener {
	GridView gridview;
	OnItemClickListener ocl_gridview = null;
	private CustomProgressDialog progressDialog = null;
	private XListView listView;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	IndexInforAdapter adapter;
	private Handler mHandler;
	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		MyApplication.getInstance().addActivity(this);
		findView();

		mHandler = new Handler();
		refresh();
		gridViewItemClick();
		gridview.setOnItemClickListener(ocl_gridview);

	}
	@Override
	public void onBackPressed() {
		Toast toast = Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�",
				Toast.LENGTH_SHORT);
		toast.show();
		MyApplication.getInstance().exit();
		//this.finish();
	}
	public void intentSearchActivity(View source) {
		Intent intent = new Intent();
		intent.setClass(IndexActivity.this, SearchActivity.class);
		startActivity(intent);
	}

	private void findView() {
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new GridViewAdapter(this));
		listView = (XListView) findViewById(R.id.activity_index_listView);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new ListViewListener());

	}

	// �����Ʒ��Ϣ��������Ʒ��Ϣ����ϸ��������
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
			intent.setClass(IndexActivity.this, ShowGoodsActivity.class);
			startActivity(intent);
		}

	}

	public void gridViewItemClick() {
		ocl_gridview = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (arg2) {
				case 0:// �����������鼮����
					intent.putExtra("name",
							getResources().getString(R.string.book));
					break;

				case 1:// �����������������
					intent.putExtra("name",
							getResources().getString(R.string.digital));
					break;

				case 2:// ��������ͨ����
					intent.putExtra("name",
							getResources().getString(R.string.transport));
					break;

				case 3:// ������������Ʒ
					intent.putExtra("name",
							getResources().getString(R.string.dailyuse));
					break;

				case 4:// ������������Ʒ
					intent.putExtra("name",
							getResources().getString(R.string.pe_use));
					break;

				case 5:// ��������װ��Ʒ
					intent.putExtra("name",
							getResources().getString(R.string.clothesuse));
					break;

				case 6:// �����������Ʒ
					intent.putExtra("name",
							getResources().getString(R.string.freegive));
					break;

				case 7:// ����������
					intent.putExtra("name",
							getResources().getString(R.string.other));
					break;
				}

				intent.setClass(IndexActivity.this,
						SortInformationActivity.class);
				startActivity(intent);

			}
		};
	}

	public void refresh() {
		IndexInforTask indexInforTask = new IndexInforTask(this);
		indexInforTask.execute("1");
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			
		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	class IndexInforTask extends AsyncTask<String, Void, String> {
		Context context;
	
		public IndexInforTask(Activity ctx) {
			this.context = ctx;
			startProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {// �����ִ̨�е������ں�̨�߳�ִ��
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/viewlatestcl?";
			url = url_constant + "page=" + params[0];
			String result = HttpUtils.connServerForResult(url);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {// ��̨����ִ����֮�󱻵��ã���ui�߳�ִ��

			List<IndexInfor> listIndexInfor = HttpUtils
					.parseJsonToIndexInfo(result);
			list = getList(listIndexInfor);
			adapter = new IndexInforAdapter(IndexActivity.this, list);

			listView.setAdapter(adapter);
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
					System.out.println("�ڶ�����Ƭ��ַ" + uri1);
					uri2 = listIndexInfor.get(i).list[2];
				}

				map.put("icon", uri0);
				map.put("icon1", uri1);
				map.put("icon2", uri2);
				map.put("bookName", listIndexInfor.get(i).getGoodsName());
				map.put("bookIntroduce", listIndexInfor.get(i)
						.getGoodsDescribe());
				map.put("bookMoney", "��"
						+ listIndexInfor.get(i).getGoodsPrice()+"Ԫ");

				map.put("goodsConnect", listIndexInfor.get(i).getGoodsConnect());
				list.add(map);
			}
		}
		return list;
	}

	// ����ˢ����Ҫ�ٴ���������һҳ
	@Override
	public void onRefresh() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				refresh();
				onLoad();
			}
		});
	}

	@Override
	public void onLoadMore() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				geneItems();

				adapter = new IndexInforAdapter

				(IndexActivity.this, list);
				listView.setAdapter(adapter);
				onLoad();
			}
		});

	}

	// ��ҳ��������
	public class MyThread extends Thread {
		public void run() {
			String url;
			String url_constant = IP.IpLoad +

			"XuptMarket/viewlatestcl?";
			url = url_constant + "page=" + pageNum++;
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
		new MyThread().start();

	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy��MM��dd��    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		listView.setRefreshTime(str);
	}

}
