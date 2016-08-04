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
import com.example.reader20.model.Latest;
import com.example.reader20.model.StorySimple;
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
public class LatestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Boolean mIsDataLoaded =true;
    private Latest mLatest;
    private RecyclerViewAdapter mAdapter;
    private List<StorySimple> mStories;
    private final String TAG="LatestFragment";
    private final int LOAD_DATA_FAIL =1;
    private final int LOAD_DATA_SUCCESS=2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_main,container,false);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srfl_frag);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_frag);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary
        ,R.color.green,R.color.yellow);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(),StoryDetailActivity.class);
                intent.putExtra("story_simple",mStories.get(position));
                startActivity(intent);
                ZLog.d(this,"position id:"+mStories.get(position).getId());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
        ,LinearLayoutManager.VERTICAL,false));
        return view;

    }

    @Override
    public void onRefresh() {
        loadLatestData();
    }

    private void loadLatestData() {

        MyHttpClient.httpGet(MyHttpUrl.DAILY_BASEURL + MyHttpUrl.LATEST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(LOAD_DATA_FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mLatest= GsonUtils.getObject(response.body().string(),Latest.class);
                mHandler.sendEmptyMessage(LOAD_DATA_SUCCESS);
                System.out.println("++++ mLatest data:"+mLatest.toString()+"mLatest story title"+
                        mLatest.getStories().get(0).getTitle());
            }
        });
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_DATA_FAIL:

                    break;
                case LOAD_DATA_SUCCESS:
                    mStories=mLatest.getStories();
                    mAdapter.addDataList(mStories);

                    break;
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStories=new ArrayList<>();
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
