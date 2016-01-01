package com.miss.schoolmarket2.person;

import java.util.ArrayList;

import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.adapter.DeletableAdapter;
import com.miss.schoolmarket2.db.DBManager;
import com.miss.schoolmarket2.entity.GoodsInfor;
import com.miss.schoolmarket2.until.Signal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyPublishFragmentA extends Fragment {

	ListView list_view;
	ArrayList<GoodsInfor> infoList;
	private Adapter adapter;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = this.getArguments();
		infoList = (ArrayList<GoodsInfor>) bundle.getSerializable("infoList");
		view = inflater.inflate(R.layout.activity_mypublishfragmenta, null);
		adapter = new DeletableAdapter(getActivity(), infoList);
		list_view = (ListView) view
				.findViewById(R.id.activity_MyPublish_listView);
		if (infoList.size() > 0) {
			list_view.setAdapter((BaseAdapter) adapter);
		}

		return view;
	}

}
