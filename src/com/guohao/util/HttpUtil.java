package com.guohao.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
	public static HttpURLConnection getGetHttpUrlConnection(String address) {
		try {
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
					//Æ´½Ó×Ö·û´®²ÎÊý
					String params = "";
					for (int i = 0; i < list.size(); i++) {
						KV kv = list.get(i);
						params += kv.getKey()+"="+kv.getValue();
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
