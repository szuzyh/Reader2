package com.example.reader20.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class GsonUtils  {

    public static <T> T getObject(String jsonString,Class<T> tClass){
        T t=null;
        try {
            Gson gson=new Gson();
            t=gson.fromJson(jsonString,tClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> getObjects(String jsonString, Class<T> cls){
        List<T> list=new ArrayList<>();
        try{
            Gson gson=new Gson();
            list=gson.fromJson(jsonString,new TypeToken<List<T>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
