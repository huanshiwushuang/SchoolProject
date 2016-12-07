package com.guohao.adapter;

import java.util.List;

import com.guohao.schoolproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MeListviewBgAdapter extends ArrayAdapter<Object[]> {
	private List<Object[]> list;
	private int resource;

	public MeListviewBgAdapter(Context context, int resource, List<Object[]> objects) {
		super(context, resource, objects);
		list = objects;
		this.resource = resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		Object[] objects = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(resource, new FrameLayout(getContext()));
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_radiobutton);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.id_textview);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.imageView.setImageResource((Integer)objects[0]);
		viewHolder.textView.setText((String)objects[1]);
		return convertView;
	}
	
	class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
}
