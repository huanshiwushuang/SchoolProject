package com.guohao.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.guohao.Interface.HttpCallBack;
import com.guohao.entity.KV;

import android.util.Log;

public class HttpUtil {
	public static final String POST = "POST";
	public static final String GET = "GET";
	private static HttpURLConnection connection;
	
	public static HttpURLConnection getPostHttpUrlConnection(String address) {
		try {
			URL url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
	public static HttpURLConnection getGetHttpUrlConnection(String address, List<KV> list) {
		Boolean once = true;
		try {
			for (int i = 0; i < list.size(); i++) {
				KV kv = list.get(i);
				String k = URLEncoder.encode(kv.getKey(), Data.ENCODE);
				String v = URLEncoder.encode(kv.getValue().toString(), Data.ENCODE);
				if (once) {
					once = false;
					address += "?";
				}
				address += k+"="+v;
				if (i != list.size()-1) {
					address += "&";
				}
			}
			URL url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(GET);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void requestData(final HttpURLConnection connection, final List<KV> list, final HttpCallBack callBack) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (connection.getRequestMethod().equals(POST)) {
					//拼接字符串参数
					String params = "";
					for (int i = 0; i < list.size(); i++) {
						KV kv = list.get(i);
						try {
							params += URLEncoder.encode(kv.getKey(), Data.ENCODE)+"="+URLEncoder.encode(kv.getValue().toString(), Data.ENCODE);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						if (i != list.size()-1) {
							params += "&";
						}
					}
					try {
						DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
						dataOutputStream.writeBytes(params);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					connection.connect();
					if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						InputStream in = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String line = "";
						StringBuilder builder = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
						callBack.onFinish(builder.toString());
					}else {
						callBack.onError(Data.NETWORK_EXCEPTION);
					}
				} catch (IOException e) {
					callBack.onError(Data.NETWORK_EXCEPTION);
				}
			}
		}).start();
	}
}
