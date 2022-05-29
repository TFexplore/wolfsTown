package com.example.wolfstown.ui.community.detail;
 
import static android.content.ContentValues.TAG;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wolfstown.R;
import com.example.wolfstown.common.Utils;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	LayoutInflater layoutInflater;
	private ImageView mImageView;
 
	public GridViewAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}
 
	@Override
	public int getCount() {
		return list.size()+1;//注意此处
	}
 
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
 		if (convertView==null){
			convertView = layoutInflater.inflate(R.layout.detail_grid_item, null);
		}

		mImageView = (ImageView) convertView.findViewById(R.id.item);
		if (position < list.size()) {
			Glide.with(Utils.getApp().getApplicationContext()).load(list.get(position))
					.error(R.mipmap.ic_launcher)
					.placeholder(R.mipmap.app)
					.dontAnimate()
					.into(mImageView);
			Log.d(TAG, "getView: ---------------");
		}else{
			mImageView.setBackgroundResource(R.drawable.add_img);//最后一个显示加号图片
		}

		return convertView;
	}

 
}