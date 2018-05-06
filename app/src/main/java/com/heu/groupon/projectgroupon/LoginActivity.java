package com.heu.groupon.projectgroupon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private TextView mTvUsername,mTvPassword,mTvToreg,mTvTologin,mTvRegUsername,mTvRegPassword;
    private View mLoginPage,mRegPage;
    JSONObject result;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginPage=findViewById(R.id.login_page);
        mRegPage=findViewById(R.id.reg_page);
        mTvToreg=findViewById(R.id.login_to_reg);
        mTvToreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChangeAnim(mLoginPage,mRegPage);

            }
        });
        mTvTologin=findViewById(R.id.reg_to_login);
        mTvTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChangeAnim(mRegPage,mLoginPage);

            }
        });
        mTvUsername=findViewById(R.id.login_name);
        mTvPassword=findViewById(R.id.login_password);
        mTvPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO) {



                    if (mTvPassword.getText().equals("")) {
                        Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (mTvUsername.getText().equals("")) {
                        Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Map<String,String> ms=new HashMap<String,String >();
                    ms.put("userid",mTvUsername.getText().toString());
                    ms.put("password",mTvPassword.getText().toString());
                    AsyncLoginTask at=new AsyncLoginTask();
                    at.execute(ms);
                    Toast.makeText(LoginActivity.this, "正在登录……", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mTvRegUsername=findViewById(R.id.reg_name);
        mTvRegPassword=findViewById(R.id.reg_password);
        mTvRegPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO) {




                    if (mTvPassword.getText().equals("")) {
                        Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (mTvUsername.getText().equals("")) {
                        Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Map<String,String> ms=new HashMap<String,String >();
                    ms.put("username",mTvRegUsername.getText().toString());
                    ms.put("password",mTvRegPassword.getText().toString());
                    AsyncRegTask at=new AsyncRegTask();
                    at.execute(ms);
                    Toast.makeText(LoginActivity.this, "正在注册……", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        setInitAnim();

        mSharedPreferences = getSharedPreferences("PDD", MODE_PRIVATE);
        DataManager.userid = mSharedPreferences.getString("userid", "");
        String psw=mSharedPreferences.getString("password", "");
        if(!DataManager.userid.equals("")&&!psw.equals(""))
        {
            mTvUsername.setText(DataManager.userid);
            mTvPassword.setText(psw);
            Map<String,String> ms=new HashMap<String,String >();
            ms.put("userid", DataManager.userid);
            ms.put("password",psw);
            AsyncLoginTask at=new AsyncLoginTask();
            at.execute(ms);
            Toast.makeText(LoginActivity.this, "正在登录……", Toast.LENGTH_SHORT).show();
        }


    }
    private class AsyncLoginTask extends AsyncTask<Map<String,String>,Object,Boolean>
    {
        @Override
        protected Boolean doInBackground(Map<String, String>[] maps) {
            try {

                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/SignIn",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    if(tmp.equals("false"))
                    {
                        return false;
                    }
                    try {
                        result = new JSONObject(tmp);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean valid) {
            if(!valid)
            {
                Toast.makeText(LoginActivity.this, "用户名或密码不正确！", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                DataManager.userid=result.getString("userid");
                DataManager.username=result.getString("username");
                DataManager.password=mTvPassword.getText().toString();
                mSharedPreferences = getSharedPreferences("PDD", MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                editor.putString("userid", DataManager.userid);
                editor.putString("password", mTvPassword.getText().toString());
                editor.apply();
                Intent myintent = new Intent();
                myintent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(myintent);
                LoginActivity.this.finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


    private class AsyncRegTask extends AsyncTask<Map<String,String>,Object,Boolean>
    {
        @Override
        protected Boolean doInBackground(Map<String, String>[] maps) {
            try {

                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/SignUp",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    if(tmp.equals("false"))
                    {
                        return false;
                    }
                    try {
                        result = new JSONObject(tmp);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean valid) {
            if(!valid)
            {
                Toast.makeText(LoginActivity.this, "注册失败，请尝试其他用户名或密码！", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                DataManager.userid=result.getString("userid");
                DataManager.username=result.getString("username");
                DataManager.password=mTvPassword.getText().toString();
                mSharedPreferences = getSharedPreferences("PDD", MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                editor.putString("userid", DataManager.userid);
                editor.putString("password", mTvPassword.getText().toString());
                editor.apply();
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注册成功！")//设置对话框的标题
                        .setMessage("这是你的用户id:"+DataManager.userid+"\n它将作为你的登录名")//设置对话框的内容
                        //设置对话框的按钮
                        .setPositiveButton("我已记住id！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myintent = new Intent();
                                myintent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(myintent);
                                LoginActivity.this.finish();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }



    public void setChangeAnim(final View from, final View to) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(from, "alpha", 1f, 0f);
        animator1.setDuration(200);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                from.setVisibility(View.GONE);
                to.setVisibility(View.VISIBLE);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(to, "alpha", 0f, 1f);
                animator2.setDuration(200);
                animator2.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        animator1.start();

    }
    public void setInitAnim() {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLoginPage, "alpha", 0f, 1f);
        animator1.setDuration(200);
        animator1.start();

    }
}
