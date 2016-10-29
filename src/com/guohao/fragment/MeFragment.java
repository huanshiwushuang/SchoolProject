package com.guohao.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.custom.CircleImageView;
import com.guohao.custom.MeBg;
import com.guohao.entity.KV;
import com.guohao.schoolproject.R;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MeFragment extends Fragment implements OnClickListener,OnDismissListener {
	private final int TAKE_PHOTO = 1;
	private final int CROP_PHOTO = 2;
	private Activity activity;
	
	private View view;
	private MeBg meBg01,meBg02,meBg03;
	private View alertCeng;
	private CircleImageView headImageView;
	
	private String name,stuNumber,grade,headImage;
	private PopupWindow popupWindow;
	private Uri uri;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me, null);
		activity = getActivity();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initView();
		initData();
		setData();
	}
	private void setData() {
		meBg01.setTextViewText02(name);
		meBg02.setTextViewText02(stuNumber);
		meBg03.setTextViewText02(grade);
		Log.d("guohao", "我的图片地址："+headImage);
		setHeadImage(headImage);
	}
	public void setHeadImage(String address) {
		address = Data.IP+Data.PROJECT_NAME+(address.startsWith("/")? address : (address ="/"+address))+"?random="+System.currentTimeMillis();
		ImageLoader loader = ImageLoader.getInstance();
		Log.d("guohao", "图片地址："+address);
		loader.displayImage(address, headImageView);
	}
	private void initData() {
		SharedPreferences p = Util.getPreference(getActivity());
		name = p.getString(Data.NAME, "");
		stuNumber = p.getString(Data.STU_NUMBER, "");
		grade = p.getString(Data.GRADE, "");
		headImage = p.getString(Data.HEAD_IMAGE, "");
	}

	private void initView() {
		headImageView = (CircleImageView) view.findViewById(R.id.id_imageview_user_pic);
		headImageView.setOnClickListener(this);
		alertCeng = getActivity().findViewById(R.id.id_view_alert_ceng);
		meBg01 = (MeBg) view.findViewById(R.id.id_mebg01);
		meBg02 = (MeBg) view.findViewById(R.id.id_mebg02);
		meBg03 = (MeBg) view.findViewById(R.id.id_mebg03);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_imageview_user_pic:
			//弹出层
			alertCeng.setVisibility(View.VISIBLE);
			
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_select_photo, new FrameLayout(getActivity()));
			//点击 popup外面，dismiss。
			View dismissView = view.findViewById(R.id.id_view_dismiss);
			dismissView.setOnClickListener(this);
			
			TextView takePhoto = (TextView) view.findViewById(R.id.id_textview_take_photo);
			TextView selectPhoto = (TextView) view.findViewById(R.id.id_textview_select_photo);
			TextView cancle = (TextView) view.findViewById(R.id.id_textview_cancle);
			takePhoto.setOnClickListener(this);
			selectPhoto.setOnClickListener(this);
			cancle.setOnClickListener(this);
			
			popupWindow = new PopupWindow(view, 
					WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			popupWindow.setFocusable(true);
			
			// 实例化一个ColorDrawable颜色为半透明 ---必要的，当背景不为空的时候，才能够返回键 dismiss
			ColorDrawable dw = new ColorDrawable(0x00000000);
			popupWindow.setBackgroundDrawable(dw);
			
			// 设置popWindow的显示和消失动画
			popupWindow.setAnimationStyle(R.style.StyleSelectPhoto);
			//设置dismiss监听事件
			popupWindow.setOnDismissListener(this);
			// 在底部显示
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.id_view_dismiss:
			popupWindow.dismiss();
			break;
		case R.id.id_textview_take_photo:
			File file = new File(Data.PATH_PHOTO, "/headImage.jpg");
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			uri = Uri.fromFile(file);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, TAKE_PHOTO);
			break;
		case R.id.id_textview_select_photo:
			Util.showToast(activity, "选择相册");
			break;
		case R.id.id_textview_cancle:
			popupWindow.dismiss();
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				//dismiss
				popupWindow.dismiss();
				Util.showAlertDialog04(activity, "正在更新头像......");
				
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("token", Util.getPreference(activity).getString(Data.TOKEN, ""));
				try {
					params.put("headFile", new File(uri.getPath()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				client.post(Data.URL_CHANGE_HEAD_PHOTO, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						Util.dismiss();
						String response = new String(responseBody);
						Log.d("guohao", "数据："+response);
						try {
							JSONObject object = new JSONObject(response);
							String status = object.getString("status");
							if (status.equals("1")) {
								Util.showToast(activity, "上传成功");
								
								object = object.getJSONObject("user");
								String headImage = object.getString("headImage");
								Editor editor = Util.getPreference(activity).edit();
								editor.putString(Data.HEAD_IMAGE, headImage);
								editor.commit();
								//更新头像后，为了避免缓存，须添加随机参数
								setHeadImage(headImage);
							}else {
								Util.showToast(activity, object.getString("msg"));
							}
						} catch (JSONException e) {
							Util.showToast(activity, e.toString());
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						Util.dismiss();
						Util.showToast(activity, new String(responseBody));
					}
				});
			}
			break;
		}
	}
	@Override
	public void onDismiss() {
		alertCeng.setVisibility(View.GONE);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
