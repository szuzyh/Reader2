package com.example.reader20.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by 27721_000 on 2016/7/8.
 */
public class SPUtils {
    private static final String SP_TAG="READER_APP";
    public static final String EMAIL="email";

//登录页用到
    public static final void save(Context context, Map<String,String> data){
        SharedPreferences sp=context.getSharedPreferences(SP_TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if (!MapUtils.isEmpty(data)){
            for (Map.Entry<String,String> entry:data.entrySet()){
                editor.putString(entry.getKey(),entry.getValue());
            }
            editor.commit();
        }
    }

    //欢迎页用到
    public static final String get(Context context,String Key){
        SharedPreferences sp=context.getSharedPreferences(SP_TAG,Context.MODE_PRIVATE);//为什么呢
        return sp.getString(Key,null);
    }

}
