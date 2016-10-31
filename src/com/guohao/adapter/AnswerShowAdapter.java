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

public class AnswerShowAdapter extends ArrayAdapter<Object[]> {
	private List<Object[]> list;
	private Boolean isDisplayNext = true;

	public AnswerShowAdapter(Context context, int resource, List<Object[]> objects) {
		super(context, resource, objects);
		list = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		Object[] objects = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_answer_show_bg, new FrameLayout(getContext()));
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_imageview);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.id_textview);
			viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.id_imageview_next);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		int imageId = (Integer) objects[0];
		String text = (String) objects[1];
		viewHolder.imageView.setImageResource(imageId);
		viewHolder.textView.setText(text);
		if (isDisplayNext == false) {
			viewHolder.imageView2.setVisibility(View.GONE);
		}
		return convertView;
	}
	public void setDisplayNext(Boolean b) {
		isDisplayNext = b;
	}
	class ViewHolder {
		ImageView imageView;
		TextView textView;
		ImageView imageView2;
	}

}
