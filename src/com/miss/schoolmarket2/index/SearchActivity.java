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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity implements IXListViewListener {
	private XListView listView1;
	private EditText editSearch;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private IndexInforAdapter adapter;
	private Handler mHandler1;
	private CustomProgressDialog progressDialog = null;
	private String keywords ;
	private Button search;
	private String page = "1";
	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		init();

		mHandler1 = new Handler();

	}

	public void Search(View source) {
		keywords = editSearch.getText().toString();
		
		refresh();
	}

	private void init() {
		editSearch = (EditText) findViewById(R.id.editSearch);
		listView1 = (XListView) findViewById(R.id.activity_search_listView);
		search = (Button)findViewById(R.id.bnSearch);
		listView1.setPullLoadEnable(true);
		listView1.setXListViewListener(this);
		listView1.setOnItemClickListener(new ListViewListener());
		
		
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				search.setClickable(true);
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				search.setClickable(true);
				
			}

		});
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
			intent.setClass(SearchActivity.this, ShowGoodsActivity.class);
			startActivity(intent);
		}

	}

	public void refresh() {
		SortInforTask sortInforTask = new SortInforTask(this);
		sortInforTask.execute(keywords, page);
	}

	public void sortBack(View source) {
		this.onBackPressed();
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

	// �첽��������
	class SortInforTask extends AsyncTask<String, Void, String> {
		Context context;

		public SortInforTask(Activity ctx) {
			this.context = ctx;
			startProgressDialog();
		}

		@Override
		protected String doInBackground(String... params) {// �����ִ̨�е������ں�̨�߳�ִ��
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/indistinctsearchcl?";
			url = url_constant + "keywords=" + params[0] + "&&page="
					+ params[1];
			String result = HttpUtils.connServerForResult(url);

			return result;
		}

		@Override
		protected void onPostExecute(String result) {// ��̨����ִ����֮�󱻵��ã���ui�߳�ִ��

			List<IndexInfor> listIndexInfor = HttpUtils
					.parseJsonToIndexInfo(result);
			list = getList(listIndexInfor);
			adapter = new IndexInforAdapter(SearchActivity.this, list);

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
						+ listIndexInfor.get(i).getGoodsPrice() + "Ԫ");

				map.put("goodsConnect", listIndexInfor.get(i).getGoodsConnect());
				list.add(map);
			}
		}
		return list;
	}

	// ����ˢ����Ҫ�ٴ���������һҳ
	@Override
	public void onRefresh() {
		mHandler1.post(new Runnable() {
			@Override
			public void run() {
				refresh();
				onLoad();
				System.out.println("----->����ˢ��");
			}
		});
	}

	@Override
	public void onLoadMore() {
		mHandler1.post(new Runnable() {
			@Override
			public void run() {
				System.out.println("----->���ظ���");
				geneItems();

				adapter = new IndexInforAdapter

				(SearchActivity.this, list);
				listView1.setAdapter(adapter);
				onLoad();
			}
		});

	}

	// ��ҳ��������
	public class MyThread1 extends Thread {
		public void run() {
			String url;
			String url_constant = IP.IpLoad + "XuptMarket/indistinctsearchcl?";
			url = url_constant + "keywords=" + keywords + "&&page=" + pageNum++;
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
				"yyyy��MM��dd��    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		listView1.setRefreshTime(str);
	}

}
