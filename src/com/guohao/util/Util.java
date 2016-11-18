package com.guohao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.guohao.custom.MyAlertDialog;
import com.guohao.java.MySqliteOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Util {
    private static String oldMsg;  
    private static Toast toast = null;  
    private static long oneTime = 0;  
    private static long twoTime = 0;  
    
    public static void showToast(Context context,String message){  
        if(toast == null){  
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);  
            toast.show();
            oneTime = System.currentTimeMillis();  
        }else{  
            twoTime = System.currentTimeMillis();  
            if(message.equals(oldMsg)){
            	toast.cancel();
            	toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); 
            	toast.show(); 
            }else{  
                oldMsg = message;
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); 
                toast.show();
            }  
        }  
        oneTime = twoTime;
    }  
    
    private static MyAlertDialog alertDialog;
    public static MyAlertDialog showAlertDialog01(Context context, String message, int flag) {
    	alertDialog = new MyAlertDialog(context, MyAlertDialog.Layout01);
    	alertDialog.setTitle("提示信息");
    	alertDialog.setMessage(message);
    	alertDialog.setYesText("确定");
    	alertDialog.setNoText("取消");
    	alertDialog.setFlag(flag);
    	alertDialog.show();
    	return alertDialog;
	}
    public static MyAlertDialog showAlertDialog04(Context context, String prompt) {
    	alertDialog = new MyAlertDialog(context, MyAlertDialog.Layout04);
		int screenHeight = alertDialog.getScreenHeight();
		alertDialog.setLoadingPrompt(prompt);
		alertDialog.setheight((int)(screenHeight/5.6));
		alertDialog.setCancelable(false);
		alertDialog.show();
		return alertDialog;
	}
    public static MyAlertDialog showAlertDialog05(Context context, String message) {
    	alertDialog = new MyAlertDialog(context, MyAlertDialog.Layout05);
    	alertDialog.setTitle("提示信息");
    	alertDialog.setMessage(message);
    	alertDialog.setYesText("确定");
    	alertDialog.setNoText("取消");
    	alertDialog.show();
    	return alertDialog;
	}
    public static MyAlertDialog showAlertDialog06(Context context, String message) {
    	alertDialog = new MyAlertDialog(context, MyAlertDialog.Layout06);
    	alertDialog.setTitle("提示信息");
    	alertDialog.setMessage(message);
    	alertDialog.setYesText("确定");
    	alertDialog.setNoText("取消");
    	alertDialog.show();
    	return alertDialog;
	}
    public static MyAlertDialog showAlertDialog07(Context context, String message) {
    	alertDialog = new MyAlertDialog(context, MyAlertDialog.Layout07);
    	alertDialog.setTitle("提示信息");
    	alertDialog.setMessage(message);
    	alertDialog.setYesText("确定");
    	alertDialog.setNoText("取消");
    	alertDialog.show();
    	return alertDialog;
	}
    public static void setCancelable(Boolean flag) {
		if (alertDialog != null) {
			alertDialog.setCancelable(flag);
		}
	}
    public static void dismiss() {
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
	}
    public static Boolean isShowing() {
		if (alertDialog != null) {
			return alertDialog.isShowing();
		}
		return false;
	}
    public static SQLiteDatabase getDatabase(Activity activity) {
    	int version = 0;
    	SQLiteDatabase db = null;
		try {
			version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	db = new MySqliteOpenHelper(activity, Data.SCHOOL_PROJECT_DB, null, version).getReadableDatabase();
		return db;
	}
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
    //获取网络信息 info
  	public static NetworkInfo getNetworkInfo(Context c) {
  		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
  		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
  		return info;
  	}
    public static SharedPreferences getPreference(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
    public static String inputStreamToString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = "";
		StringBuilder builder = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
