package com.guohao.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Util {
	/** 之前显示的内容 */  
    private static String oldMsg ;  
    /** Toast对象 */  
    private static Toast toast = null ;  
    /** 第一次时间 */  
    private static long oneTime = 0 ;  
    /** 第二次时间 */  
    private static long twoTime = 0 ;  
      
    /** 
     * 显示Toast 
     * @param context 
     * @param message 
     */  
    public static void showToast(Context context,String message){  
        if(toast == null){  
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);  
            toast.show() ;  
            oneTime = System.currentTimeMillis() ;  
        }else{  
            twoTime = System.currentTimeMillis() ;  
            if(message.equals(oldMsg)){
            	toast.cancel();
            	toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); 
            	toast.show(); 
            }else{  
                oldMsg = message ;  
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); 
                toast.show();
            }  
        }  
        oneTime = twoTime ;  
    }  
}
