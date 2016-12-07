package com.guohao.schoolproject;


import java.io.File;

import com.guohao.util.Data;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		//缓存图片目录
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, Data.PATH_PHOTO+"/cache");
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.img341)
				.showImageOnFail(R.drawable.img341)
				.showImageOnLoading(R.drawable.img341)
				.resetViewBeforeLoading(false)
				.imageScaleType(ImageScaleType.NONE)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(false)
				.build();
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		
		config.memoryCacheExtraOptions(480, 800);
		config.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024));	
		config.memoryCacheSize(10*1024*1024);
		config.threadPoolSize(3);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50*1024*1024);
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
//		config.writeDebugLogs(); // 如果 APP 上市，就移除它，这是打印日志的
		config.defaultDisplayImageOptions(options);
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
}
