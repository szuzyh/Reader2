package com.example.reader20.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reader20.Activity.StoryDetailActivity;
import com.example.reader20.R;
import com.example.reader20.adapter.OnItemClickListener;
import com.example.reader20.adapter.RecyclerViewAdapter;
import com.example.reader20.http.MyHttpClient;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.model.Before;
import com.example.reader20.model.Latest;
import com.example.reader20.model.StorySimple;
import com.example.reader20.utils.GsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class LatestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private Boolean mIsDataLoaded =true;
    private Latest mLatest;
    private Before mBefore;
    private RecyclerViewAdapter mAdapter;
   // private MainViewAdapter mAdapter;
    private List<StorySimple> mStories;
    private List<StorySimple> mBeforeStories;
    private final String TAG="LatestFragment";
    private final int LOAD_DATA_FAIL =1;
    private final int LOAD_LATESTDATA_SUCCESS=2;
    private final int LOAD_BEFOREDATA_SUCCESS=3;
    boolean isLoading = false;
    private boolean isLoadBefore=false;
    private boolean isLoadFirst;
    String date;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_main,container,false);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srfl_frag);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_frag);
        final LinearLayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary
        ,R.color.green,R.color.yellow);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mLatest.getStories().get(position).getType()==MyHttpUrl.TOPIC){
                    getActivity().onBackPressed();
                }else {
                    Intent intent = new Intent(getActivity(), StoryDetailActivity.class);
                    intent.putExtra("story_simple", mStories.get(position));

                    // intent.putExtra("story_before",mBeforeStories.get(position));
                    startActivity(intent);


                }

            }
        });

//        RecyclerViewHeader header=RecyclerViewHeader.fromXml(getActivity(),R.layout.test);
//        header.attachTo(mRecyclerView);
     //   mRecyclerView.addView(header);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    int visibleItemCount=manager.getChildCount();
                    int totalItemCount=manager.getItemCount();
                    int first=manager.findFirstVisibleItemPosition();
                    if (first+visibleItemCount>=totalItemCount){
                        loadBeforeData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;


    }


    private void loadBeforeData() {
        if (!isLoadBefore){
            isLoadBefore=true;
          MyHttpClient.httpGet(MyHttpUrl.DAILY_BASEURL + MyHttpUrl.BEFORE_URL + date, new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                  mHandler.sendEmptyMessage(LOAD_DATA_FAIL);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                  mBefore = GsonUtils.getObject(response.body().string(),Before.class);
                  date=mBefore.getDate();
                  for (StorySimple entity :
                          mBefore.getStories()) {
                      entity.setDate(convertDate(date));
                  }
                  isLoadBefore = false;
//                  mHandler.post(new Runnable() {
//                      @Override
//                      public void run() {
//                         // mBeforeStories=mBefore.getStories();
//                          List<StorySimple> storiesEntities = mBefore.getStories();
//                          StorySimple topic=new StorySimple();
//                          topic.setType(MyHttpUrl.TOPIC);
//                          topic.setTitle(convertDate(date));
//                          mBeforeStories.add(0, topic);
//                          mAdapter.addBefore(storiesEntities);
//                      }
//                  });
                  mHandler.sendEmptyMessage(LOAD_BEFOREDATA_SUCCESS);

              }
          });
        }

    }

    @Override
    public void onRefresh() {
        loadLatestData();
    }

    private void loadLatestData() {
        if (!isLoading) {
            isLoading=true;
            MyHttpClient.httpGet(MyHttpUrl.DAILY_BASEURL + MyHttpUrl.LATEST, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(LOAD_DATA_FAIL);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mLatest = GsonUtils.getObject(response.body().string(), Latest.class);
                    date = mLatest.getDate();
                    lastDate = date;
                    isLoading=false;
                    for (StorySimple entity :
                            mLatest.getStories()) {
                        entity.setDate(convertDate(date));
                    }
                    mHandler.sendEmptyMessage(LOAD_LATESTDATA_SUCCESS);

                }
            });
        }
    }
    String lastDate;
    private String convertDate(String date) {
        String res = date.substring(0, 4);
        res += "年";
        res += date.substring(4, 6);
        res += "月";
        res += date.substring(6, 8);
        res += "日";
        return res;
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_DATA_FAIL:

                    break;
                case LOAD_LATESTDATA_SUCCESS:
                    mStories=mLatest.getStories();
                    mAdapter.addDataList(mStories);
                    break;
                case LOAD_BEFOREDATA_SUCCESS:

                    mBeforeStories=mBefore.getStories();
                    StorySimple topic=new StorySimple();
                    topic.setType(MyHttpUrl.TOPIC);
                    topic.setTitle(convertDate(date));
                    mBeforeStories.add(0, topic);
//                  mLatest.getStories().addAll(mBefore.getStories());
//                  mAdapter.addBefore(mBefore.getStories());
                    mLatest.getStories().addAll(mBeforeStories);
                    mAdapter.addDataList(mLatest.getStories());
                    break;
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStories=new ArrayList<>();
       // mAdapter=new MainViewAdapter(getActivity(),mStories);
        mAdapter=new RecyclerViewAdapter(getActivity(),mStories);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadLatestData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
