package com.example.reader20;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.http.NetWorkUtils;
import com.example.reader20.utils.HttpUtils;
import com.example.reader20.utils.SPUtils;
import com.example.reader20.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {
    @Bind(R.id.iv_splash) ImageView iv_splash;
    private boolean mIsLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        //是否已经登录，如果之前登录过，直接进入主界面，若没有。则进入登录界面
        mIsLogin= SPUtils.get(this,SPUtils.EMAIL)==null?false:true;
        //动画加载
        File dir=getFilesDir();
        final File imgFile=new File(dir,"start.jpg");
        if (imgFile.exists()){
            iv_splash.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }else {
            iv_splash.setImageResource(R.drawable.bg_welcome);
        }
       final Animation scaleAnim= AnimationUtils.loadAnimation(this,R.anim.splash);
// final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        scaleAnim.setFillAfter(true);
//        scaleAnim.setDuration(3000);

       // final ScaleAnimation scaleAnim= (ScaleAnimation) AnimationUtils.loadAnimation(this,R.anim.splash);
        iv_splash.startAnimation(scaleAnim);
        //动画监听
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //判断是否联网 如果没有联网，则进行相应提醒
                if (!NetWorkUtils.isNetWorkConnected(WelcomeActivity.this)){
                    Utils.ToastMsg(WelcomeActivity.this,NetWorkUtils.NetWork_ERROR, Toast.LENGTH_LONG);
                }
            }
            //动画结束后实现的逻辑
            @Override
            public void onAnimationEnd(Animation animation) {

                //判断是否登录，如果已经登录，则跳转进入主界面，如果没有登录，则进入登录界面

                HttpUtils.get(MyHttpUrl.START, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                        try {
                            JSONObject jsonObject=new JSONObject(new String(bytes));
                            String url=jsonObject.getString("img");

                            HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    saveImage(imgFile,bytes);
                                  //  startMainActivity();

                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                    //startMainActivity();

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                    // startMainActivity();
                    }
                });
                if (mIsLogin) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    private void saveImage(File file, byte[] bytes) {
        try{
            if (file.exists()){file.delete();}
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void startMainActivity() {
//
//        if (mIsLogin){
//            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
//            finish();
//        }else {
//            Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
//            startActivity(intent);
//            overridePendingTransition(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
//            finish();
//        }
//
//    }


}
