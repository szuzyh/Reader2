package com.example.reader20.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 27721_000 on 2016/7/8.
 */
public class Utils {

    public static void ToastMsg(Context context,String msg,int type){
        if (type!= Toast.LENGTH_LONG&&type!=Toast.LENGTH_SHORT){
            throw new IllegalArgumentException("Toast Type must be " +
                    "Toast.LENGTH_LONG or Toast.LENGTH_SHORT");
        }
        switch (type){
            case Toast.LENGTH_LONG:
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                break;
            case Toast.LENGTH_SHORT:
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
