package com.example.reader20.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.reader20.R;
import com.example.reader20.model.StorySimple;
import com.example.reader20.utils.ZLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private OnItemClickListener mListener;
    private Context mContext;
    private List<StorySimple> mStories;

public RecyclerViewAdapter(Context context, List<StorySimple> stories){
    mContext=context;
    mStories=stories;

}
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.card_view,parent,false);
        MyRecyclerViewHolder holder=new MyRecyclerViewHolder(view);
        return holder;//这是为什么呢  应该是会执行一次，有执行顺序
}

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        ZLog.d(this,mStories.get(position).getTitle());

        holder.tv_story_title.setText(mStories.get(position).getTitle().toString());
        if (mStories.get(position).getImages()!=null){
            final ImageLoader imageLoader=ImageLoader.getInstance();
            DisplayImageOptions options=new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            if (mStories.get(position).getImages().size()==1){
                imageLoader.displayImage(mStories.get(position).getImages().get(0)
                ,holder.img_story_image,options);
            }
        }else {
//            holder.img_story_image.setVisibility(View.GONE);
            holder.img_story_image.setImageResource(R.drawable.readericon);
        }

        if (Boolean.valueOf(mStories.get(position).getMultipic())==true){
            holder.iv_multiPic.setVisibility(View.VISIBLE);
        }else {
            holder.iv_multiPic.setVisibility(View.GONE);
        }

        if (mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(holder.itemView,holder.getPosition());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    public void addDataList(List<StorySimple> items){
        mStories.clear();
        mStories.addAll(items);
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

}
