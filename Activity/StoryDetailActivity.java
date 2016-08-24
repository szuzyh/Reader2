package com.example.reader20.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reader20.R;
import com.example.reader20.http.MyHttpClient;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.http.NetWorkUtils;
import com.example.reader20.model.StoryDetail;
import com.example.reader20.model.StoryExtra;
import com.example.reader20.model.StorySimple;
import com.example.reader20.utils.GsonUtils;
import com.example.reader20.utils.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoryDetailActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    @Bind(R.id.iv_story_image)
    ImageView iv_story_image;
    @Bind(R.id.toolBar_story)
    Toolbar mToolbar;
    @Bind(R.id.collapsingToolbarLayout_story)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.appBarLayout_story)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.wv_story_body)
    WebView wv_story_body;
    @Bind(R.id.coordinatorLayout_story)
    CoordinatorLayout mCoordinatorLayout;

    private StorySimple mStorySimple;
    private StoryDetail mStoryDetail;
    private Handler mHandler=new Handler();
    private TextView toolbar_menu_comment_tv;
    private TextView toolbar_menu_praise_tv;
    private StoryExtra storyExtra;
    private  final String WX_APP_ID="wx762e06c34cb7285d";
    private IWXAPI mApi;
    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        ButterKnife.bind(this);
        initWX();
        initData();
        initView();
    }

    private void initData() {
        mStorySimple=getIntent().getParcelableExtra("story_simple");
//        mStorySimple=getIntent().getParcelableExtra("story_before");
    }

    private void initView() {
        setSupportActionBar(mToolbar);
       // ActionBar bar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        bar.setDisplayShowCustomEnabled(true);
//        View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.toolbar_layout,null);
//        bar.setCustomView(v);
        mCollapsingToolbarLayout.setTitle(mStorySimple.getTitle());
        wv_story_body.getSettings().setJavaScriptEnabled(true);
        wv_story_body.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv_story_body.getSettings().setDomStorageEnabled(true);
        wv_story_body.getSettings().setAppCacheEnabled(true);
        wv_story_body.getSettings().setDatabaseEnabled(true);


        if (NetWorkUtils.isNetWorkConnected(this)){
            MyHttpClient.httpGet(MyHttpUrl.DAILY_BASEURL + mStorySimple.getId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mStoryDetail= GsonUtils.getObject(response.body().string(),StoryDetail.class);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                         final ImageLoader imageLoader=ImageLoader.getInstance();
                            DisplayImageOptions options=new DisplayImageOptions.Builder()
                                    .cacheInMemory(true)
                                    .cacheOnDisk(true)
                                    .build();
                            imageLoader.displayImage(mStoryDetail.getImage(),iv_story_image,options);
                           // mFile=new File(mStoryDetail.getImage());
                            //想一想啊 怎么把图片搞出来
                            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                            String html = "<html><head>" + css + "</head><body>" + mStoryDetail.getBody() + "</body></html>";
                            html = html.replace("<div class=\"img-place-holder\">", "");//这个删掉也无所谓吧
                            System.out.println(html);
                            wv_story_body.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
                            getStoryExtraData();
                        }
                    });
                }
            });
        }

    }

    private File mFile;
    private void getStoryExtraData() {

        HttpUtils.get(MyHttpUrl.STORY_EXTRA+mStorySimple.getId(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Gson gson=new Gson();
                storyExtra=gson.fromJson(s,StoryExtra.class);
                toolbar_menu_praise_tv.setText(storyExtra.getPopularity());
                toolbar_menu_comment_tv.setText(storyExtra.getComments());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_so_on,menu);
        final MenuItem comment=menu.findItem(R.id.comment);
        final MenuItem praise=menu.findItem(R.id.praise);
        final MenuItem share=menu.findItem(R.id.share);
        toolbar_menu_comment_tv= (TextView) comment.getActionView();
        toolbar_menu_praise_tv= (TextView) praise.getActionView();
        comment.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(comment);
            }
        });
        praise.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(praise);
            }
        });
        share.setOnMenuItemClickListener(this);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        return true;
    }

//

    private ShareActionProvider mShareActionProvider;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.comment:
                Intent commentIntent=new Intent(this,CommentS.class);
                commentIntent.putExtra("id",mStorySimple.getId());
                commentIntent.putExtra("long_comments",storyExtra.getLong_comments());
                commentIntent.putExtra("short_comments",storyExtra.getShort_comments());
                commentIntent.putExtra("comments",storyExtra.getComments());
                startActivity(commentIntent);
                break;
            default:
                break;



        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                WXWebpageObject wxWebpage=new WXWebpageObject();
                wxWebpage.webpageUrl=MyHttpUrl.SHARE_URL+mStoryDetail.getId();
                WXMediaMessage msg=new WXMediaMessage(wxWebpage);
                msg.title=mStoryDetail.getTitle();
                msg.description=mStorySimple.getTitle()+"(分享自@reader app)";
              //  Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.praise);
                Bitmap bitmap= BitmapFactory.decodeFile(mFile.getAbsolutePath());
                //msg.thumbData=bmpToByteArray(bitmap,true);
                msg.setThumbImage(bitmap);
                System.out.println(""+mStorySimple.getImages().get(0));

                SendMessageToWX.Req req=new SendMessageToWX.Req();
                req.transaction="webpage"+System.currentTimeMillis();
                req.message=msg;
                req.scene=SendMessageToWX.Req.WXSceneSession;
                mApi.sendReq(req);

                break;
        }
        return true;
    }



    private void initWX() {
        mApi= WXAPIFactory.createWXAPI(this,WX_APP_ID,true);
        mApi.registerApp(WX_APP_ID);
    }
    private String buildTransaction(final String type) {

        return (type == null) ? String.valueOf(System.currentTimeMillis())

                : type + System.currentTimeMillis();

    }


    private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);

        if (needRecycle) {

            bmp.recycle();

        }

        byte[] result = output.toByteArray();

        try {

            output.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }


}
