package com.example.reader20.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.Map;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 27721_000 on 2016/7/8.
 */
public class MyHttpClient {
    private static OkHttpClient mHttpClient;

    public MyHttpClient(){}

    public static void initInstance(){
        if (mHttpClient==null){
            mHttpClient=new OkHttpClient.Builder().addNetworkInterceptor(
                    new StethoInterceptor()).build();
        }
    }


    //get
    public static void httpGet(String url, Callback listener){

        Request request=new  Request.Builder().url(url).tag(url).build();
        mHttpClient.newCall(request).enqueue(listener);
    }

    //post
    public static void httpPost(String url, Map<String,String> params,Callback listener){
        if (params==null){
            httpGet(url,listener);
            return;
        }
        //okhttp3.0+
        FormBody.Builder builder=new FormBody.Builder();
        //okhttp2.0+ FormEncodingBuilder builder=new FormEncodingBuilder();
        //将数据存储到特定的容器中？
        //存储键值对
        Set<Map.Entry<String,String>> entrySet=params.entrySet();
        for (Map.Entry<String,String> entry:entrySet) {
          builder.add(entry.getKey(), entry.getValue());
      }
        RequestBody requestbody=builder.build();
        //post请求体
        Request request= new Request.Builder().url(url).post(requestbody).tag(url).build();
        mHttpClient.newCall(request).enqueue(listener);
        }

    }


