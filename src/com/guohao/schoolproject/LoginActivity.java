package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.custom.Title;
import com.guohao.entity.KV;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private Title customTitle;
	private EditText phoneNum,pwd;
	private Activity activity;
	private final int ok = 1;
	private final int error = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ok:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						MainActivity.actionStart(activity);
						finish();
					}else {
						Util.showToast(activity, object.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case error:
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
		customTitle.setTitleText("��¼");
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		phoneNum = (EditText) findViewById(R.id.id_edittext_phone_num);
		pwd = (EditText) findViewById(R.id.id_edittext_pwd);
		
		activity = LoginActivity.this;
	}
	
	
	
	
	public void login(View view) {
		String account = phoneNum.getText().toString();
		String password = pwd.getText().toString();
		Boolean isAccountOK = StringUtil.checkAccount(account);
		if (!isAccountOK) {
			Util.showToast(activity, "�˻�����");
			return;
		}
		Boolean isPwdOK = StringUtil.checkPwd(password);
		if (!isPwdOK) {
			Util.showToast(activity, "��������");
			return;
		}
		NetworkInfo info = Util.getNetworkInfo(activity);
		if (info == null || !info.isAvailable()) {
			Util.showToast(activity, "�޿�������");
			return;
		}
		Util.showAlertDialog04(activity, "���ڵ�¼......");
		//��������
		List<KV> list = new ArrayList<KV>();
		list.add(new KV("stuNumber", account));
		list.add(new KV("password", password));
		//����POST��¼����
		HttpUtil.requestData(HttpUtil.getPostHttpUrlConnection(Data.URL_LOGIN), list, new HttpCallBack() {
			@Override
			public void onFinish(Object object) {
				Message msg = handler.obtainMessage();
				msg.what = ok;
				msg.obj = object;
				handler.sendMessage(msg);
			}
			@Override
			public void onError(Object object) {
				Message msg = handler.obtainMessage();
				msg.what = error;
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
		Util.showToast(LoginActivity.this, "���΢�ŵ�¼");
	}
	public void loginQQ(View view) {
		Util.showToast(LoginActivity.this, "���QQ��¼");
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
