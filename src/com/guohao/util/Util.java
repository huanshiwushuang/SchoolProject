package com.guohao.util;

import com.guohao.custom.MyAlertDialog;

import android.content.Context;
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
    public static MyAlertDialog showAlertDialog(Context context, String message, int flag) {
    	alertDialog = new MyAlertDialog(context);
    	alertDialog.setTitle("提示信息");
    	alertDialog.setMessage(message);
    	alertDialog.setYesText("确定");
    	alertDialog.setNoText("取消");
    	alertDialog.setFlag(flag);
    	alertDialog.show();
    	return alertDialog;
	}
}
