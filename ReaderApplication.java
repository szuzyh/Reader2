package com.example.reader20;

import android.app.Application;
import android.content.Context;

import com.example.reader20.http.MyHttpClient;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Created by 27721_000 on 2016/7/8.
 */
public class ReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        MyHttpClient.initInstance();
    }
    private void initImageLoader(Context context) {

//        File cacheDir= StorageUtils.getCacheDirectory(context);
//        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
//                .threadPoolSize(3)
//                .threadPriority(Thread.NORM_PRIORITY-2)
//                .memoryCache(new LruMemoryCache(2*1024*1024))
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCache(new UnlimitedDiskCache(cacheDir)).writeDebugLogs()
//                .build();
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);

//        ImageLoader.getInstance().init(config);
    }
}
