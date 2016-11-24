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
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener,TextWatcher,OnKeyListener {
	private static int flag;
	public static final int SUGGEST = 1;
	public static final int CHANGE_PWD = 2;
	public static final int ABOUT = 3;
	private Activity activity;
	private MyAlertDialog myAlertDialog;
	private String pwdNew;
	
	private Title customTitle;
	
	//---01
	private EditText editText;
	private Button submit;
	private TextView contentLength;
	//---02
	private EditText editText02;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpCallBack.CALL_BACK_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						Editor editor = Util.getPreference(activity).edit();
						editor.putString(Data.PWD, StringUtil.encodeMD5(pwdNew));
						editor.commit();
						
						Util.showToast(activity, object.getString("msg"));
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
		switch (flag) {
		case SUGGEST:
			setContentView(R.layout.activity_setting_suggest);
			break;
		case CHANGE_PWD:
			setContentView(R.layout.activity_setting_change_pwd);
			break;
		case ABOUT:
			setContentView(R.layout.activity_setting_about);
			break;
		}
		
		initView();
	}
	
	private void initView() {
		activity = SettingActivity.this;
		customTitle = (Title) findViewById(R.id.id_custom_title);
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case SUGGEST:
			customTitle.setTitleText("意见反馈");
			editText = (EditText) findViewById(R.id.id_edittext);
			submit = (Button) findViewById(R.id.id_submit);
			contentLength = (TextView) findViewById(R.id.id_textview_length);
			submit.setOnClickListener(this);
			editText.addTextChangedListener(this);
			break;
		case CHANGE_PWD:
			customTitle.setTitleText("修改密码");
			editText = (EditText) findViewById(R.id.id_edittext_pwd01);
			editText02 = (EditText) findViewById(R.id.id_edittext_pwd02);
			submit = (Button) findViewById(R.id.id_submit);
			submit.setOnClickListener(this);
			break;
		case ABOUT:
			customTitle.setTitleText("关于");
			break;
		}
	}

	public static void actionStart(Context context, int flag) {
		SettingActivity.flag = flag;
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (flag) {
		case SUGGEST:
			Util.showToast(SettingActivity.this, "建议提交");
			break;
		case CHANGE_PWD:
			String pwdOld = editText.getText().toString();
			pwdNew = editText02.getText().toString();
			Boolean isPwdOKOld = StringUtil.checkPwd(pwdOld);
			if (!isPwdOKOld) {
				Util.showToast(activity, "非法旧密码");
				return;
			}
			Boolean isPwdOKNew = StringUtil.checkPwd(pwdNew);
			if (!isPwdOKNew) {
				Util.showToast(activity, "非法新密码");
				return;
			}
			String encodePwd = Util.getPreference(activity).getString(Data.PWD, "");
			if (!pwdOld.equals(encodePwd)) {
				Util.showToast(activity, "旧密码错误");
				return;
			}
			NetworkInfo info = Util.getNetworkInfo(activity);
			if (info == null || !info.isAvailable()) {
				Util.showToast(activity, "无可用网络");
				return;
			}
			//隐藏键盘
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			//弹出正在登陆提示框
			myAlertDialog = Util.showAlertDialog04(activity, "正在修改密码......");
			myAlertDialog.getAlertDialog().setOnKeyListener(this);
			//构建参数
			List<KV> list = new ArrayList<KV>();
			list.add(new KV("token", Util.getPreference(activity).getString(Data.TOKEN, "")));
			list.add(new KV("oldPsd", pwdOld));
			list.add(new KV("newPsd", pwdNew));
			//发起POST登录请求
			HttpURLConnection connection = HttpUtil.getPostHttpUrlConnection(Data.URL_CHANGE_PWD);
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
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		contentLength.setText((255-s.length())+"");
	}
	@Override
	public void afterTextChanged(Editable s) {
		
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
}

