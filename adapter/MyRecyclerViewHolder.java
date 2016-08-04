package com.example.reader20.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reader20.R;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    //RecyclerView必须使用viewHolder
    public TextView tv_story_title;
    public ImageView img_story_image;
    public ImageView iv_multiPic;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        tv_story_title = (TextView) itemView.findViewById(R.id.tv_title);
        img_story_image= (ImageView) itemView.findViewById(R.id.iv_image);
        iv_multiPic= (ImageView) itemView.findViewById(R.id.iv_multiPic);
    }
}
