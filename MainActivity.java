package com.example.reader20;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.reader20.utils.Utils;

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


    private LatestFragment mLatestFragment;
    private ThemeFragment mThemeFragment;
    private long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ToastMsg(MainActivity.this,"Test", Toast.LENGTH_SHORT);
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
}
