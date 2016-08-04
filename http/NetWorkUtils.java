package com.example.reader20.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 27721_000 on 2016/7/8.
 */
public class NetWorkUtils {

    public static final String NetWork_ERROR="no network connect";

    public static  boolean isNetWorkConnected(Context context){
        if (context==null){
            throw  new IllegalArgumentException("Context can not be null");//throw是干什么的呢
        }
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        if (info!=null){
            return info.isAvailable();
        }
        return false;
    }

}
