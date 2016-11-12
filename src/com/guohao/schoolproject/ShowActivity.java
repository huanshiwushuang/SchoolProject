package com.guohao.schoolproject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.entity.KV;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ShowActivity extends Activity {
	private Handler handler;
	private Runnable r;
	private String account;
	private String password;
	private Boolean isTimeOK = false,isLoginOK = false;
	
	private SharedPreferences p;
	private Activity activity;
	private NetworkInfo info;
	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HttpCallBack.CALL_BACK_OK:
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					Util.showToast(activity, object.getString("msg"));
					if (status.equals("1")) {
						isLoginOK = true;
						if (isTimeOK) {
							jumpMain();
						}
					}else {
						jumpLogin();
					}
				} catch (JSONException e) {
					Util.showToast(activity, e.toString());
					jumpLogin();
				}
				break;
			case HttpCallBack.CALL_BACK_FAIL:
				Util.showToast(activity, msg.obj.toString());
				jumpLogin();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		
		initView();
		initData();
		login();
		jump();
	}

	private void jump() {
		handler.postDelayed(r, 3*1000);
	}

	public void login() {
		if (account.equals("") || password.equals("")) {
			return;
		}
		Boolean isAccountOK = StringUtil.checkAccount(account);
		if (!isAccountOK) {
			Util.showToast(activity, "非法账户");
			return;
		}
		Boolean isPwdOK = StringUtil.checkPwd(password);
		if (!isPwdOK) {
			Util.showToast(activity, "非法密码");
			return;
		}
		info = Util.getNetworkInfo(activity);
		if (info == null || !info.isAvailable()) {
			Util.showToast(activity, "无可用网络");
			return;
		}
		//构建参数
		List<KV> list = new ArrayList<KV>();
		list.add(new KV("stuNumber", account));
		list.add(new KV("password", password));
		//发起POST登录请求
		HttpURLConnection connection = HttpUtil.getPostHttpUrlConnection(Data.URL_LOGIN);
		connection.setConnectTimeout(6*1000);
		connection.setReadTimeout(6*1000);
		HttpUtil.requestData(connection, list, new HttpCallBack() {
			@Override
			public void onFinish(Object object) {
				Message msg = handler2.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_OK;
				msg.obj = object;
				handler2.sendMessage(msg);
			}
			@Override
			public void onError(Object object) {
				Message msg = handler2.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_FAIL;
				msg.obj = object;
				handler2.sendMessage(msg);
			}
		});
		
	}
	private void jumpMain() {
		MainActivity.actionStart(activity);
		finish();
	}
	private void jumpLogin() {
		LoginActivity.actionStart(activity);
		finish();
	}
	private void initView() {
		activity = ShowActivity.this;
		p = Util.getPreference(activity);
		handler = new Handler();
		info = Util.getNetworkInfo(activity);
		r = new Runnable() {
			@Override
			public void run() {
				isTimeOK = true;
				//这里需要登录，跳转不同的界面
				if (account.equals("") || password.equals("")) {
					jumpLogin();
				}else if (info == null || !info.isAvailable()) {
					jumpLogin();
				}else if (isLoginOK) {
					jumpMain();
				}
			}
		};
	}
	private void initData() {
		account = p.getString(Data.ACCOUNT, "");
		password = p.getString(Data.PWD, "");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(r);
		handler2.removeCallbacksAndMessages(null);
	}
}
