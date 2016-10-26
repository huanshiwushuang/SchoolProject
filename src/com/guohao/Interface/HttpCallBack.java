package com.guohao.Interface;

public interface HttpCallBack {
	public static final int CALL_BACK_OK = 1;
	public static final int CALL_BACK_FAIL = 0;
	public void onFinish(Object object);
	public void onError(Object object);
}
