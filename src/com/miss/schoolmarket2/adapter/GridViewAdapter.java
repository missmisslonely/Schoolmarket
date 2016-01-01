package com.miss.schoolmarket2.adapter;



import com.miss.schoolmarket2.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
        private Context mContext;

    public GridViewAdapter(Context c) {
        mContext = c;
    }
        @Override
        public int getCount() {
                // TODO Auto-generated method stub
                return mThumbIds.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
                View view = View.inflate(mContext, R.layout.gridview_item, null);
                LinearLayout rl = (LinearLayout) view.findViewById(R.id.relaGrid);
        
        ImageView image = (ImageView) rl.findViewById(R.id.chooseImage);
        TextView text = (TextView) rl.findViewById(R.id.chooseText);        
        
        image.setImageResource(mThumbIds[position]);
        text.setText(mTextValues[position]);
        
        return rl;
        }
        
        // references to our images
    private Integer[] mThumbIds = {
            R.drawable.home_book,R.drawable.home_shuma,R.drawable.home_jiaotong,
            R.drawable.home_life,R.drawable.home_tiyu,R.drawable.home_cloth,
            R.drawable.home_send,R.drawable.home_qita
    };
    
    private String[] mTextValues = {
                    "书籍报刊","数码电子","交通工具",
                    "生活用品","体育用品","服装饰品","免费赠送","其他",
                   
    };

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}