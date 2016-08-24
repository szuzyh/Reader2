package com.example.reader20.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.reader20.R;
import com.example.reader20.adapter.CommentsListAdapter;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.model.CommentsData;
import com.example.reader20.utils.HttpUtils;
import com.example.reader20.utils.ScreenUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class CommentS extends AppCompatActivity {



    private String id;
    private String long_comments_num;
    private String short_comments_num;
    private String comments_num;
    private ListView lv_comments;
    private Toolbar toolbar_comments;
    private LinearLayout ll_commentS;
    private PopupWindow popupWindow;
    private List<CommentsData.comments> mCommentsList=new ArrayList<>();
    private CommentsListAdapter mAdapter;
    private CommentsData mCommentsData;
    private boolean isOpen = false;
    private int clickPosition;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_s);
        lv_comments= (ListView) findViewById(R.id.comment_listView);
        toolbar_comments= (Toolbar) findViewById(R.id.comment_toolbar);
        ll_commentS= (LinearLayout) findViewById(R.id.ll_commentS);
        id=getIntent().getStringExtra("id");
        long_comments_num=getIntent().getStringExtra("long_comments");
        comments_num=getIntent().getStringExtra("comments");
        short_comments_num=getIntent().getStringExtra("short_comments");
        toolbar_comments.setTitle(comments_num+"条点评");
        setSupportActionBar(toolbar_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_comments.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final View popupView= getLayoutInflater().inflate(R.layout.popup_progress,null);
        popupWindow=new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT
        ,RelativeLayout.LayoutParams.MATCH_PARENT,true);

        for (int i=0;i<3;i++){
            CommentsData.comments comments=new CommentsData.comments();
            comments.setAuthor("null"+i);
            comments.setId(long_comments_num);//用于传送长评数目
            comments.setLikes(short_comments_num);//短评数目
            mCommentsList.add(i,comments);
        }
        mAdapter=new CommentsListAdapter(mCommentsList,CommentS.this);
        lv_comments.setAdapter(mAdapter);
        lv_comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCommentsList.get(position).getAuthor().equals("null2")){
                    if (!isOpen){
                        showPopupWindow();
                        clickPosition=position;
                        getCommentData(MyHttpUrl.SHORT_COMMENTS);
                        isOpen=true;
                    }else {
                        mAdapter.addData(mCommentsList.subList(0,position+1));
                        scrollToItem(0);
                        isOpen=false;
                    }
                }
            }
        });

    }

    private void getCommentData(final String url) {
        String commentDataUrl=url.replace("news_id",id);
        HttpUtils.get(commentDataUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"load fail",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                if (url.equals(MyHttpUrl.LONG_COMMENTS)){
                    parseJsonLongComments(s);
                }else {
                    parseJsonShortComments(s);
                }
            }
        });
    }

    private void parseJsonShortComments(String s) {
        Gson gson=new Gson();
        mCommentsData =gson.fromJson(s,CommentsData.class);
        if (mCommentsData!=null&&mCommentsData.getComments().size()>0){
            for (CommentsData.comments comments:mCommentsData.getComments()){
                mCommentsList.add(comments);
            }
            dismissPopupWindow();
            mAdapter.addData(mCommentsList);
            scrollToItem(clickPosition);
        }else {
            dismissPopupWindow();
        }
    }

    private void dismissPopupWindow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        },250);
    }

    private void parseJsonLongComments(String s) {
        Gson gson=new Gson();
        mCommentsData =gson.fromJson(s,CommentsData.class);
        if (mCommentsData!=null&&mCommentsData.getComments().size()>0){
           mCommentsList.clear();
            mCommentsList=mCommentsData.getComments();
            if (!TextUtils.isEmpty(mCommentsList.get(0).getId())){
                for (int i=0;i<3;i++){
                    if (i==1){
                        continue;
                    }
                    CommentsData.comments comments=new CommentsData.comments();
                    comments.setAuthor("null"+i);
                    comments.setId(long_comments_num);//用于传送长评数目
                    comments.setLikes(short_comments_num);//短评数目
                    if (i==2){
                        mCommentsList.add(comments);
                        break;
                    }
                    mCommentsList.add(0,comments);

                }
                dismissPopupWindow();
                mAdapter.addData(mCommentsList);
            }
        }else {
            dismissPopupWindow();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isOpen){
                    getCommentData(MyHttpUrl.LONG_COMMENTS);
                }else {
                    getCommentData(MyHttpUrl.SHORT_COMMENTS);
                }
            }
        },500);
    }

    private void showPopupWindow() {
        popupWindow.showAtLocation(ll_commentS, Gravity.CENTER,0,0);
    }

    public void scrollToItem(int position) {
        //   listView.setSelection(position);
        //    listView.smoothScrollToPosition(position);
        //    listView.smoothScrollToPositionFromTop(position,0);
        lv_comments .smoothScrollToPositionFromTop(position,0,500);
    }

    public int getEmptyItemHeight() {
        int height= ScreenUtils.getScreenHeight(this)-
                toolbar_comments.getLayoutParams().height-ScreenUtils.dp2px(this,115);


        return height;
    }

    @Override
    public void onBackPressed() {
        if (popupWindow.isShowing()) {     //无法执行   // TODO: 2016/8/9
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
