package com.example.reader20.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reader20.Activity.CommentS;
import com.example.reader20.R;
import com.example.reader20.model.CommentsData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 27721_000 on 2016/8/23.
 */
public class CommentsListAdapter extends BaseAdapter {

    private List<CommentsData.comments> mCommentsList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private CommentsData.comments mComments;
    private int nullPosition;//?
    protected static Uri tempUri;

    public CommentsListAdapter(List<CommentsData.comments> commentsList, Context context) {
        mCommentsList = commentsList;
        mContext = context;
        mImageLoader=ImageLoader.getInstance();
        mOptions=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(60))
                .build();
    }

    @Override
    public int getCount() {
        return mCommentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mComments=mCommentsList.get(position);
        if (mComments.getAuthor().equals("null0")){
            View view= LayoutInflater.from(mContext).inflate(R.layout.longcomments_title,null);
            TextView textView= (TextView) view.findViewById(R.id.longComments_title_tv);
            textView.setText(mCommentsList.get(position).getId()+"条长评论");

            return view;
        }else if (mComments.getAuthor().equals("null1")){
            View view=LayoutInflater.from(mContext).inflate(R.layout.longcomments_is_empty, null);
            int height=((CommentS)mContext).getEmptyItemHeight();
            AbsListView.LayoutParams params=new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT,height);
            view.setLayoutParams(params);
            nullPosition=position;
            return view;
        }else {
            if (mComments.getAuthor().equals("null2")) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.longcomments_title, null);
                TextView textView = (TextView) view.findViewById(R.id.longComments_title_tv);
                textView.setText(mCommentsList.get(position).getLikes() + "条短评论");
                ImageView imageView = (ImageView) view.findViewById(R.id.comments_loadMore_iv);
                imageView.setVisibility(View.VISIBLE);
                return view;
            } else {
                ViewHolder viewHolder = null;
                nullPosition = -1;
                if (convertView == null || convertView.getTag() == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.comment_avatar_iv = (ImageView) convertView.findViewById(R.id.comment_avatar_iv);
                    viewHolder.comment_author_tv = (TextView) convertView.findViewById(R.id.comment_author_tv);
                    viewHolder.comment_content_tv = (TextView) convertView.findViewById(R.id.comment_content_tv);
                    viewHolder.comment_content_replay_tv = (TextView) convertView.findViewById(R.id.comment_content_replay_tv);
                    viewHolder.comment_time_tv = (TextView) convertView.findViewById(R.id.comment_time_tv);
                    viewHolder.comment_like_tv = (TextView) convertView.findViewById(R.id.comment_like_tv);
                    viewHolder.comment_expand_tv = (TextView) convertView.findViewById(R.id.comment_expand_tv);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.comment_author_tv.getPaint().setFakeBoldText(true);
                viewHolder.comment_author_tv.setText(mComments.getAuthor());
                mImageLoader.displayImage(mComments.getAvatar(),viewHolder.comment_avatar_iv, mOptions);
                viewHolder.comment_like_tv.setText(mComments.getLikes());
                viewHolder.comment_content_tv.setText(mComments.getContent());
                if (mComments.getReply_to()!=null){
                    viewHolder.comment_content_replay_tv.setVisibility(View.VISIBLE);
                    String replyString="//"+mComments.getReply_to().getAuthor()+": "+mComments.getReply_to().getContent();
                    viewHolder.comment_content_replay_tv.setText(replyString);
                }else {
                    System.out.println("reply null");
                }
                Date date = new Date(Long.parseLong(mComments.getTime())*1000);
                SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                viewHolder.comment_time_tv.setText(format.format(date));
                return convertView;
            }

        }

    }
    @Override
    public boolean isEnabled(int position) {
        if (position == 0 || (nullPosition != -1 && position == nullPosition)) {
            return false;
        }

        return true;
    }
    public void addData(List<CommentsData.comments> commentsList) {
        this.mCommentsList = commentsList;
        notifyDataSetChanged();
    }
    private class ViewHolder {
        private ImageView comment_avatar_iv;
        private TextView comment_author_tv;
        private TextView comment_like_tv;
        private TextView comment_content_tv;
        private TextView comment_content_replay_tv;
        private TextView comment_time_tv;
        private TextView comment_expand_tv;
    }
}
