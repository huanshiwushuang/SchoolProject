package com.guohao.object.pull;

import android.view.View;

public interface Pull {
	//设置下拉头
	public void setHeaderView(View view);
	//设置上拉尾
	public void setFooterView(View view);
	
	//获取下拉头的尺寸
	public void getHeaderSize();
	//获取上拉头的尺寸
	public void getFooterSize();
	
	//设置下拉监听器
	public void setPullDownListener();
	//设置上拉监听器
	public void setPullUpListener();
	
	//回调函数---下拉
	public void onPullDown();
	//回调函数---上拉
	public void onPullUp();
	
}
