package com.example.reader20.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by 27721_000 on 2016/8/23.
 */
public class ScreenUtils {

    public static int getScreenHeight(Context context) {
        WindowManager manager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    public static int dp2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale + 0.5f);
    }
}
