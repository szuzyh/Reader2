package com.example.reader20.utils;

import android.util.Log;

/**
 * Created by 27721_000 on 2016/7/17.
 */

//这个类是拿来封装成一个测试类
public class ZLog {

    private static final String LOG_TAG_PREFIX = "ZZZ---";
    //调试
    public static void d(Object who, String message) {
        Log.d(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message);
    }
//信息
    public static void i(Object who, String message) {
        Log.i(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message);
    }
    //详细信息
    public static void v(Object who, String message) {
        Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message);
    }
//错误
public static void e(Object who, String message) {
    Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message);
}
    //警告
    public static void w(Object who, String message) {
        Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message);
    }


    //调试
    public static void d(Object who, String message,Throwable tr) {
        Log.d(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message,tr);
    }
    //信息
    public static void i(Object who, String message,Throwable tr) {
        Log.i(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message,tr);
    }
    //详细信息
    public static void v(Object who, String message,Throwable tr) {
        Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message,tr);
    }
    //错误
    public static void e(Object who, String message,Throwable tr) {
        Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message,tr);
    }
    //警告
    public static void w(Object who, String message,Throwable tr) {
        Log.v(LOG_TAG_PREFIX + who.getClass().getSimpleName(), message,tr);
    }





}
