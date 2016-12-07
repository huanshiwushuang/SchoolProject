package com.guohao.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
     * 获取圆形图片方法
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
    	
    	//先将图片的宽高转换为等边（裁剪，以最短边为长度）
    	Bitmap output = Bitmap.createBitmap
    			(bitmap, 
    			widthBitmap > heightBitmap ? (maxBianBitmap-minBianBitmap)/2 : 0,
    			widthBitmap > heightBitmap ? 0 : (maxBianBitmap-minBianBitmap)/2,
    			minBianBitmap, minBianBitmap);
    	//再将图片的宽高等比例缩放为与ImageView容器一样的宽高（此时的图片是一个正方形，边长等于ImageView）
    	output= Bitmap.createScaledBitmap(output, widthThis, heightThis, true);
    	
    	Bitmap drawPaper = Bitmap.createBitmap(output.getWidth(), output.getHeight(), Config.ARGB_8888);
        //构建画板
        Canvas canvas = new Canvas(drawPaper);
        //画板置为透明
        canvas.drawARGB(0, 0, 0, 0);
        //构建画笔---油漆画笔
        paint.setAntiAlias(true);
        paint.setColor(0xffff0000);
        
    	
        //先画一个圆形
        canvas.drawCircle(widthThis/2, heightThis/2, (Math.min(widthThis, heightThis))/2, paint);  
        //取原图（canvas上的为原图SRC，画笔要画的为目标图DST）相交的部分（Mode.SRC_IN）
        //详细参见：http://folksy.iteye.com/blog/1488629  （新浪博客有转载）
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        
        //截取Bitmap图片的内容区域，默认为图片的左上-右下（这里为了截取到图片的中间呢，须计算）
        //图片来源区域范围
        Rect rectFrom = new Rect(0, 0, output.getWidth(), output.getHeight());
        //画到canvas的目标区域范围
        Rect rectTo = new Rect(0, 0, output.getWidth(), output.getHeight());
        //再画一个矩形
        canvas.drawBitmap(output, rectFrom, rectTo, paint);
        return drawPaper;  
    }
	public void setLockCircle(Boolean lockCircle) {
		this.lockCircle = lockCircle;
	} 
}
