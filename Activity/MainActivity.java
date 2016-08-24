package com.example.reader20.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.reader20.Fragment.LatestFragment;
import com.example.reader20.Fragment.ThemeFragment;
import com.example.reader20.R;
import com.example.reader20.utils.Utils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
//    @Bind(R.id.iv_user)
//    ImageView iv_User;


    private LatestFragment mLatestFragment;
    private ThemeFragment mThemeFragment;
    private long lastTime;
    private  boolean isBitmapBack=false;
    private  Bitmap UserBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }
private  boolean isChangeUseIv=false;
    private void initView() {
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ToastMsg(MainActivity.this,"爱你们哦", Toast.LENGTH_SHORT);
            }
        });
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();//?????
        mDrawerLayout.setDrawerListener(toggle);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);
        mLatestFragment=new LatestFragment();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_content, mLatestFragment).commit();
        View headerView=mNavigationView.getHeaderView(0);
         iv_user= (ImageView) headerView.findViewById(R.id.iv_user);
        if (!isChangeUseIv){
            iv_user.setImageResource(R.drawable.first);
            isChangeUseIv=true;
        }else {
            //从存储的照片提取
            Bitmap bitmap= BitmapFactory.decodeFile(userIvPath);
            iv_user.setImageBitmap(bitmap);
            isChangeUseIv=true;


        }

        iv_user.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showChoosePicDialog();
           }
       });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                intent.putExtra("email",)

                startActivity(intent);
                onDestroy();
                break;
            case R.id.action_search:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        selectNavItem(item.getItemId());
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }else {
            long currentTime=System.currentTimeMillis();
            if (currentTime-lastTime>2000){
                Snackbar.make(mFab,"再按一次退出",Snackbar.LENGTH_SHORT)
                        .setAction("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();
                lastTime=currentTime;
            }else {
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void selectNavItem(int id) {
        switch (id){

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fl_content, mLatestFragment).commit();
                break;
            default:
                mThemeFragment=new ThemeFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("themeId",getThemeId(id));

                mThemeFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_content,mThemeFragment).commit();

                break;


        }
    }
    public int getThemeId(int id) {
        int themeId=-1;
        switch (id){
            case R.id.nav_psychology:
                themeId=13;
                break;
            case R.id.nav_user_recommended:
                themeId=12;
                break;
            case R.id.nav_movie:
                themeId=3;
                break;
            case R.id.nav_not_allowed_bored:
                themeId=11;
                break;
            case R.id.nav_design:
                themeId=4;
                break;
            case R.id.nav_big_company:
                themeId=5;
                break;
            case R.id.nav_economic:
                themeId=6;
                break;
            case R.id.nav_internet_safety:
                themeId=10;
                break;
            case R.id.nav_start_games:
                themeId=2;
                break;
            case R.id.nav_music:
                themeId=7;
                break;
            case R.id.nav_cartoon:
                themeId=9;
                break;
            case R.id.nav_sports:
                themeId=8;
                break;
        }
        return themeId;
    }

    /**
     * 显示修改头像的对话框
     */
    protected static final int CHOOSE_PICTURE=0;
    protected static final int TAKE_PICTURE=1;
    private static final int CROP_SMALL_PICTURE=2;
    protected static Uri tempUri;
    private ImageView iv_user;
    private void showChoosePicDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items={"选择本地照片","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case CHOOSE_PICTURE:
                        //Intent openAlbumIntent=new Intent(Intent.ACTION_GET_CONTENT);
                        Intent openAlbumIntent=new Intent("android.intent.action.PICK");
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent,CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE:
                        Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
                        startActivityForResult(openCameraIntent,TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data!=null){
                        setImageToView(data);
                    }
                    break;
            }
        }
    }



    private void setImageToView(Intent data) {
        Bundle extras=data.getExtras();
        if (extras!=null){
            Bitmap photo=extras.getParcelable("data");
            photo=Utils.toRoundBitmap(photo,tempUri);
            userIvPath=Utils.savePhoto(photo, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), String
                    .valueOf(System.currentTimeMillis()));
            iv_user.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    String userIvPath;
    private void uploadPic(Bitmap bitmap) {
        String imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        if (imagePath!=null){
            Toast.makeText(this,"假装上传中...",Toast.LENGTH_SHORT).show();
        }
    }

    private void startPhotoZoom(Uri uri) {
        if (uri==null){
            Toast.makeText(this,"没有这样照片哦",Toast.LENGTH_SHORT).show();
        }
        tempUri=uri;
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }

}
