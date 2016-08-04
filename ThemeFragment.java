package com.example.reader20;


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

import com.example.reader20.adapter.RecyclerViewAdapter;
import com.example.reader20.adapter.OnItemClickListener;
import com.example.reader20.http.MyHttpClient;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.model.StorySimple;
import com.example.reader20.model.ThemeStories;
import com.example.reader20.utils.GsonUtils;
import com.example.reader20.utils.ZLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 27721_000 on 2016/7/17.
 */
public class ThemeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Boolean mIsDataLoaded =true;
    private ThemeStories mThemeStories;
    private RecyclerViewAdapter mAdapter;
    private List<StorySimple> Stories;
    private final String TAG="ThemeFragment";
    private final int LOAD_DATA_FAIL =1;
    private final int DATA_LOAD_SUCCESS=2;
    private int mThemeId;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_DATA_FAIL:
                    break;
                case DATA_LOAD_SUCCESS:
                    Stories=mThemeStories.getStories();
                    mAdapter.addDataList(Stories);
                    break;
            }
            mSwipeRefreshLayout.setRefreshing(false);

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Stories=new ArrayList<>();
        mAdapter=new RecyclerViewAdapter(getActivity(),Stories);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                loadThemeData();

            }
        });
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mThemeId=getArguments().getInt("themeId");

        View view=inflater.inflate(R.layout.fragment_main,container,false);

        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srfl_frag);

        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_frag);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary
        ,R.color.green,R.color.yellow);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(),StoryDetailActivity.class);
                intent.putExtra("story_simple",Stories.get(position));
                startActivity(intent);
                ZLog.d(this,"position id:"+Stories.get(position).getId());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));
        return view;


    }
    private void loadThemeData() {

        MyHttpClient.httpGet(MyHttpUrl.Theme_BASE + mThemeId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(LOAD_DATA_FAIL);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mThemeStories= GsonUtils.getObject(response.body().string(),ThemeStories.class);
                mHandler.sendEmptyMessage(DATA_LOAD_SUCCESS);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        loadThemeData();
    }
}
