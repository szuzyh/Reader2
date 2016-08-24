package com.example.reader20.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.reader20.R;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.model.StorySimple;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 27721_000 on 2016/8/20.
 */
public class MainViewAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private OnItemClickListener mListener;
    private Context mContext;
    private List<StorySimple> mStories;
    String lastDate;

    public MainViewAdapter(Context context, List<StorySimple> stories) {
        mContext = context;
        mStories = stories;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_new_items,parent,false);
        // View view=mInflater.inflate(R.layout.card_view,parent,false);
        MainViewHolder holder=new MainViewHolder(view);
        return holder;//这是为什么呢  应该是会执行一次，有执行顺序
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {

        holder.tv_story_title.setText(mStories.get(position).getTitle().toString());

        StorySimple storySimple=mStories.get(position);
        if (storySimple.getType() == MyHttpUrl.TOPIC) {
            ((FrameLayout) holder.tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            holder.tv_story_title.setVisibility(View.GONE);
            holder.img_story_image.setVisibility(View.GONE);
            holder.tv_topic.setVisibility(View.VISIBLE);
            holder.tv_topic.setText(storySimple.getTitle());
        } else {
            holder.tv_topic.setVisibility(View.GONE);
            holder.img_story_image.setVisibility(View.VISIBLE);
            holder.tv_topic.setVisibility(View.VISIBLE);
            //图片显示
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


            //时间显示
            holder.itemView.setTag(position);
//        if (position==1){
//            holder.item_time.setVisibility(View.VISIBLE);
//            holder.item_time.setText("今日热闻");
//            lastDate=mStories.get(position).getDate();
//        }else {
//            if (lastDate.equals(mStories.get(position).getDate())){
//                holder.item_time.setVisibility(View.GONE);
//            }else {
//                holder.item_time.setVisibility(View.VISIBLE);
//                holder.item_time.setText(mStories.get(position).getDate());
//                lastDate=mStories.get(position).getDate();
//            }
//        }
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

    public void  addBefore(List<StorySimple> beforeItems){
        mStories.addAll(beforeItems);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
