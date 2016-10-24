package com.guohao.java;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class MyHandler extends Handler{
	WeakReference<Activity> mWeakReference;
	
	public MyHandler(Activity activity) {
		mWeakReference = new WeakReference<Activity>(activity);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		if (mWeakReference != null) {
			Activity activity = mWeakReference.get();
			if (activity != null) {
				
			}
		}
	}
}
