package com.example.reader20.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.reader20.http.MyHttpUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by 27721_000 on 2016/6/4.
 */
public class HttpUtils {
    private static AsyncHttpClient sClient=new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandler) {
        sClient.get(MyHttpUrl.BASEURL+url,responseHandler);
    }
    public static void getImage(String url, ResponseHandlerInterface responseHandler) {
        sClient.get(url,responseHandler);
    }
    public static boolean isNetworkConnected(Context context){
        if (context!=null){
            ConnectivityManager manager= (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo=manager.getActiveNetworkInfo();
            if (mNetworkInfo!=null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
