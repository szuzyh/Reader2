package com.example.reader20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reader20.R;
import com.example.reader20.http.MyHttpClient;
import com.example.reader20.http.MyHttpUrl;
import com.example.reader20.utils.SPUtils;
import com.example.reader20.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

public class LoginActivity extends Activity {

    @Bind(R.id.mlv_login)
    MaterialLoginView mlv_login;

    private EditText mRegisterName,mRegisterUser,mRegisterPass,mRegisterPassReq;
    private EditText mLoginUser,mLoginPass;
    private TextView mRegisterBtn,mLoginBtn;
    private Boolean isRegisterViewGone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mlv_login.setListener(new MaterialLoginViewListener() {
            @Override
            public Boolean onRegister(TextInputLayout registerName, TextInputLayout registerUser,
                                      TextInputLayout registerPass, TextInputLayout registerPassRep,
                                      TextView registerBtn) {
                initRegisterView(registerName, registerUser, registerPass, registerPassRep, registerBtn);
                final String name = registerName.getEditText().getText().toString();
                final String email = registerUser.getEditText().getText().toString();
                final String pass = registerPass.getEditText().getText().toString();
                final String passReq = registerPassRep.getEditText().getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(pass) || TextUtils.isEmpty(passReq)) {
                    Utils.ToastMsg(LoginActivity.this, "不能为空！", Toast.LENGTH_LONG);
                    return false;
                }
                if (!(email.endsWith("@qq.com")
                        || email.endsWith("@gmail.com") || email.endsWith("@foxmail.com")
                        || email.endsWith("@yahoo.com") || email.endsWith("@163.com"))) {
                    if (!pass.equals(passReq)) {
                        Utils.ToastMsg(LoginActivity.this, "两次密码不一致", Toast.LENGTH_LONG);
                    } else {
                        Utils.ToastMsg(LoginActivity.this, "请输入qq、163、gmail、yahoo、foamail" +
                                "等邮箱", Toast.LENGTH_SHORT);
                    }
                    return false;
                } else if(!pass.equals(passReq)){
                    Utils.ToastMsg(LoginActivity.this, "两次密码不一致", Toast.LENGTH_LONG);
                    return false;
                }
                setRegisterEnable(false);
                //用HashMap进行数据存储
             Map<String,String> params=new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",pass);

                MyHttpClient.httpPost(MyHttpUrl.HTTP_URL + MyHttpUrl.REGISTER, params,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                mHandler.sendEmptyMessage(REGISTER_FAIL);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject=new JSONObject(response.body().string());
                                    if (jsonObject.getString("register").equals("true")){
                                        mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                                    }else {
                                        mHandler.sendEmptyMessage(REGISTER_FAIL_EXIST);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                return isRegisterViewGone;
            }

            //登录的回调
            @Override
            public Boolean onLogin(TextInputLayout loginUser, TextInputLayout loginPass, TextView loginBtn) {
                initLoginView(loginUser,loginPass,loginBtn);


                final String password=loginPass.getEditText().getText().toString();
                final String email=loginUser.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Utils.ToastMsg(LoginActivity.this,"邮箱或密码不能为空",Toast.LENGTH_LONG);
                    return false;
                }

                setLoginEnable(false);
                final Map<String,String> params=new HashMap<>();

                params.put("email", email);
                params.put("password", password);


                MyHttpClient.httpPost(MyHttpUrl.HTTP_URL + MyHttpUrl.LOGIN, params
                        , new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                mHandler.sendEmptyMessage(LOGIN_FAIL);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject=new JSONObject(response.body().string());
                                    if (jsonObject.getString("login").equals("true")){
                                        SPUtils.save(LoginActivity.this,params);
                                        mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                                    }else {
                                        mHandler.sendEmptyMessage(LOGIN_FAIL_PASS_ERROR);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                return true;
            }
        });

    }

    private void setLoginEnable(boolean b) {
        mLoginUser.setEnabled(b);
        mLoginPass.setEnabled(b);
        mLoginBtn.setEnabled(b);
    }

    private void initLoginView(TextInputLayout loginUser, TextInputLayout loginPass, TextView loginBtn) {
        mLoginBtn=loginBtn;
        mLoginPass=loginPass.getEditText();
        mLoginUser=loginUser.getEditText();
    }

    private final int LOGIN_FAIL=0;
    private final int LOGIN_SUCCESS=1;
    private final int REGISTER_FAIL=2;
    private final int REGISTER_FAIL_EXIST=3;
    private final int REGISTER_SUCCESS=4;
    private final int LOGIN_FAIL_PASS_ERROR=5;

    private Handler mHandler=new Handler(){
        //处理http的异步回调

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_FAIL:
                    setLoginEnable(true);
                    Utils.ToastMsg(LoginActivity.this,"登录失败",Toast.LENGTH_LONG);
                    break;
                case REGISTER_FAIL:
                    isRegisterViewGone=false;
                    setRegisterEnable(true);
                    Utils.ToastMsg(LoginActivity.this,"注册失败",Toast.LENGTH_LONG);
                    break;
                case REGISTER_FAIL_EXIST:
                    isRegisterViewGone=false;
                    Utils.ToastMsg(LoginActivity.this,"用户或邮箱不存在",Toast.LENGTH_SHORT);
                    setRegisterEnable(true);
                    break;
                case REGISTER_SUCCESS:
                    isRegisterViewGone=true;
                    Utils.ToastMsg(LoginActivity.this,"注册成功",Toast.LENGTH_LONG);
                    setRegisterEnable(true);
                    break;
                case LOGIN_SUCCESS:
                    Utils.ToastMsg(LoginActivity.this,"登录成功",Toast.LENGTH_LONG);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                    startActivity(intent);
                    finish();
                    break;
                case LOGIN_FAIL_PASS_ERROR:
                    Utils.ToastMsg(LoginActivity.this,"用户或者密码错误",Toast.LENGTH_SHORT);
                    setLoginEnable(true);
                    break;
                default:
                    break;
            }

        }
    };
    private void setRegisterEnable(boolean b) {
        mRegisterUser.setEnabled(b);
        mRegisterPass.setEnabled(b);
        mRegisterName.setEnabled(b);
        mRegisterPassReq.setEnabled(b);
        mRegisterBtn.setEnabled(b);
    }

    private void initRegisterView(TextInputLayout registerName, TextInputLayout registerUser,
                                  TextInputLayout registerPass, TextInputLayout registerPassRep,
                                  TextView registerBtn) {
        mRegisterBtn=registerBtn;
        mRegisterName=registerName.getEditText();
        mRegisterPass=registerPass.getEditText();
        mRegisterPassReq=registerPassRep.getEditText();
        mRegisterUser=registerUser.getEditText();
    }

}
