package com.guohao.schoolproject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.custom.MyAlertDialog;
import com.guohao.custom.Title;
import com.guohao.entity.KV;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnKeyListener {
	private Title customTitle;
	private EditText phoneNum,pwd;
	private Activity activity;
	
	private MyAlertDialog myAlertDialog;
	
	private String account;
	private String password;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpCallBack.CALL_BACK_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						object = object.getJSONObject("user");
						
						Editor editor = Util.getPreference(activity).edit();
						editor.putString(Data.STU_NUMBER, object.getString("stuNumber"));
						editor.putString(Data.GRADE, object.getString("garde"));
						editor.putString(Data.PASSWORD, object.getString("password"));
						editor.putString(Data.HEAD_IMAGE, object.getString("headImage"));
						editor.putString(Data.NAME, object.getString("name"));
						editor.putString(Data.CLASS_NO, object.getString("classNo"));
						editor.putString(Data.ID, object.getString("id"));
						editor.putString(Data.TOKEN, object.getString("token"));
						
						editor.putString(Data.ACCOUNT, account);
						editor.putString(Data.PWD, password);
						editor.commit();
						
						MainActivity.actionStart(activity);
						finish();
					}else {
						Util.showToast(activity, object.getString("msg"));
					}
				} catch (JSONException e) {
					Util.showToast(activity, e.toString());
				}
				break;
			case HttpCallBack.CALL_BACK_FAIL:
				Util.dismiss();
				Util.showToast(activity, msg.obj.toString());
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		initData();
	}
	private void initData() {
		customTitle.setTitleText("登录");
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		phoneNum = (EditText) findViewById(R.id.id_edittext_phone_num);
		pwd = (EditText) findViewById(R.id.id_edittext_pwd);
		
		activity = LoginActivity.this;
	}
	
	
	
	
	public void login(View view) {
		account = phoneNum.getText().toString();
		password = pwd.getText().toString();
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
		NetworkInfo info = Util.getNetworkInfo(activity);
		if (info == null || !info.isAvailable()) {
			Util.showToast(activity, "无可用网络");
			return;
		}
		//弹出正在登陆提示框
		myAlertDialog = Util.showAlertDialog04(activity, "正在登录......");
		myAlertDialog.getAlertDialog().setOnKeyListener(this);
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
				Message msg = handler.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_OK;
				msg.obj = object;
				handler.sendMessage(msg);
			}
			@Override
			public void onError(Object object) {
				Message msg = handler.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_FAIL;
				msg.obj = object;
				handler.sendMessage(msg);
			}
		});
		
	}
	public void registe(View view) {
		RegisteActivity.actionStart(LoginActivity.this);
		overridePendingTransition(R.anim.anim_in_trans, R.anim.anim_out_trans);
	}
	public void forgetPwd(View view) {
		ForgetPwdActivity.actionStart(LoginActivity.this);
		overridePendingTransition(R.anim.anim_in_trans, R.anim.anim_out_trans);
	}
	public void loginWeiXin(View view) {
		Util.showToast(LoginActivity.this, "点击微信登录");
	}
	public void loginQQ(View view) {
		Util.showToast(LoginActivity.this, "点击QQ登录");
	}
	
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			if (Util.isShowing()) {
				Util.dismiss();
			}
			break;
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	public static void actionStart(Context c) {
		Intent intent = new Intent(c, LoginActivity.class);
		c.startActivity(intent);
	}
}
