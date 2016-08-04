package com.example.reader20.utils;

import java.util.Map;

/**
 * Created by 27721_000 on 2016/7/16.
 */
public class MapUtils {
    public static final boolean isEmpty(Map map){
        if (map!=null&&map.size()>0){
            return false;
        }
        return true;
    }
}
