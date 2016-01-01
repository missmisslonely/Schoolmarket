package com.miss.schoolmarket2.adapter;

import java.util.ArrayList;
import java.util.HashMap;


import com.miss.schoolmarket2.R;
import com.miss.schoolmarket2.index.until.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexInforAdapter extends BaseAdapter {
    
   private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public IndexInforAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_index_list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.bookName ); // title
        TextView artist = (TextView)vi.findViewById(R.id.bookIntroduce); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.bookMoney); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.searchimageView); // thumb image
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        
        title.setText(song.get("bookName"));
        artist.setText(song.get("bookIntroduce"));
        duration.setText(song.get("bookMoney"));
        imageLoader.DisplayImage(song.get("icon"), thumb_image);
        System.out.println("Í¼Æ¬µÄurl--->"+song.get("icon"));
        return vi;
    }
}