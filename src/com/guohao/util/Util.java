package com.guohao.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Util {
	/** ֮ǰ��ʾ������ */  
    private static String oldMsg ;  
    /** Toast���� */  
    private static Toast toast = null ;  
    /** ��һ��ʱ�� */  
    private static long oneTime = 0 ;  
    /** �ڶ���ʱ�� */  
    private static long twoTime = 0 ;  
      
    /** 
     * ��ʾToast 
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
