package com.guohao.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	private Paint paint;
	private Boolean lockCircle = false;

	public CircleImageView(Context context) {
		this(context,null);
	}
	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (lockCircle) {
			drawable = null;
		}
        if (drawable != null) {  
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  
            Bitmap b = getCircleBitmap(bitmap, 14);  
            Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());  
            Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);  
        }else {
        	super.onDraw(canvas);
		}
	}
	
	/**
     * ��ȡԲ��ͼƬ����
     * @param bitmap
     * @param pixels
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
    	int widthThis = getWidth();
    	int heightThis = getHeight();
    	int widthBitmap = bitmap.getWidth();
    	int heightBitmap = bitmap.getHeight();
    	
    	int minBianBitmap = Math.min(widthBitmap, heightBitmap);
    	int maxBianBitmap = Math.max(widthBitmap, heightBitmap);
    	
    	//�Ƚ�ͼƬ�Ŀ��ת��Ϊ�ȱߣ��ü�������̱�Ϊ���ȣ�
    	Bitmap output = Bitmap.createBitmap
    			(bitmap, 
    			widthBitmap > heightBitmap ? (maxBianBitmap-minBianBitmap)/2 : 0,
    			widthBitmap > heightBitmap ? 0 : (maxBianBitmap-minBianBitmap)/2,
    			minBianBitmap, minBianBitmap);
    	//�ٽ�ͼƬ�Ŀ�ߵȱ�������Ϊ��ImageView����һ���Ŀ�ߣ���ʱ��ͼƬ��һ�������Σ��߳�����ImageView��
    	output= Bitmap.createScaledBitmap(output, widthThis, heightThis, true);
    	
    	Bitmap drawPaper = Bitmap.createBitmap(output.getWidth(), output.getHeight(), Config.ARGB_8888);
        //��������
        Canvas canvas = new Canvas(drawPaper);
        //������Ϊ͸��
        canvas.drawARGB(0, 0, 0, 0);
        //��������---���ử��
        paint.setAntiAlias(true);
        paint.setColor(0xffff0000);
        
    	
        //�Ȼ�һ��Բ��
        canvas.drawCircle(widthThis/2, heightThis/2, (Math.min(widthThis, heightThis))/2, paint);  
        //ȡԭͼ��canvas�ϵ�ΪԭͼSRC������Ҫ����ΪĿ��ͼDST���ཻ�Ĳ��֣�Mode.SRC_IN��
        //��ϸ�μ���http://folksy.iteye.com/blog/1488629  �����˲�����ת�أ�
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        
        //��ȡBitmapͼƬ����������Ĭ��ΪͼƬ������-���£�����Ϊ�˽�ȡ��ͼƬ���м��أ�����㣩
        //ͼƬ��Դ����Χ
        Rect rectFrom = new Rect(0, 0, output.getWidth(), output.getHeight());
        //����canvas��Ŀ������Χ
        Rect rectTo = new Rect(0, 0, output.getWidth(), output.getHeight());
        //�ٻ�һ������
        canvas.drawBitmap(output, rectFrom, rectTo, paint);
        return drawPaper;  
    }
	public void setLockCircle(Boolean lockCircle) {
		this.lockCircle = lockCircle;
	} 
}
