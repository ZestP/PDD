package com.heu.groupon.projectgroupon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmOrderActivity extends Activity {
    int mGoodID, mTeamID;
    boolean isSingle;
    String mName;
    float mPrice;
    JSONObject result;
    boolean hasresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent myintent = getIntent();
        mGoodID = myintent.getIntExtra("goodid", -1);
        isSingle = myintent.getBooleanExtra("issingle", true);
        mPrice = myintent.getFloatExtra("price", -1);
        mName = myintent.getStringExtra("name");
        mTeamID = myintent.getIntExtra("teamid", -1);
        ((TextView) findViewById(R.id.order_name)).setText(mName);
        ((TextView) findViewById(R.id.order_cost)).setText("￥" + mPrice);
        ((TextView) findViewById(R.id.confirm_total)).setText("￥" + mPrice);
        ((Button) findViewById(R.id.confirm_announce)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> ms = new HashMap<String, String>();
                if (isSingle) {
                    Log.i("ZX", "Single");
                    ms.put("userid", DataManager.userid);

                    ms.put("password", DataManager.password);
                    ms.put("address", ((TextView) findViewById(R.id.confirm_manualaddress)).getText().toString());
                    ms.put("goodid", Integer.toString(mGoodID));
                    AsyncSingleTask as = new AsyncSingleTask();
                    as.execute(ms);
                } else {

                    Log.i("ZX", "Multi");
                    ms.put("userid", DataManager.userid);

                    ms.put("password", DataManager.password);
                    ms.put("address", ((TextView) findViewById(R.id.confirm_manualaddress)).getText().toString());
                    if (mTeamID == -1) {
                        ms.put("goodid", Integer.toString(mGoodID));
                        AsyncGenerateTeamTask as = new AsyncGenerateTeamTask();
                        as.execute(ms);
                    } else {
                        ms.put("teamid", ((TextView) findViewById(R.id.confirm_manualaddress)).getText().toString());
                        AsyncJoinTeamTask as = new AsyncJoinTeamTask();
                        as.execute(ms);
                    }

                }
            }
        });
    }

    private class AsyncSingleTask extends AsyncTask<Map<String, String>, Object, Long> {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp = NetUtil.doPostString(DataManager.serverURL + "/ForPDD/SinglePurchase", maps[0]);
                Log.i("ZX", tmp);
                result = null;
                hasresult = false;
                if (tmp != "") {
                    if (tmp.charAt(0) == '{') {
                        hasresult = true;
                        result = new JSONObject(tmp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            if (hasresult) {
                AlertDialog dialog = null;
                try {
                    dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                            .setTitle("订单创建成功！")//设置对话框的标题
                            .setMessage("这是你的订单id:" + result.getInt("orderid"))//设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myintent = new Intent();
                                    myintent.setClass(ConfirmOrderActivity.this, MainActivity.class);
                                    startActivity(myintent);
                                    ConfirmOrderActivity.this.finish();
                                    dialog.dismiss();
                                }
                            }).create();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.show();
            } else {
                AlertDialog dialog = null;
                dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                        .setTitle("订单创建失败")//设置对话框的标题
                        .setMessage("请确认你的个人信息是否完整")//设置对话框的内容
                        //设置对话框的按钮
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }
    }

    private class AsyncGenerateTeamTask extends AsyncTask<Map<String, String>, Object, Long> {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp = NetUtil.doPostString(DataManager.serverURL + "/ForPDD/GenerateTeam", maps[0]);
                Log.i("ZX", tmp);
                result = null;
                hasresult = false;
                if (tmp != "") {
                    if (tmp.charAt(0) == '{') {
                        hasresult = true;
                        result = new JSONObject(tmp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            if (hasresult) {
                AlertDialog dialog = null;
                try {
                    dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                            .setTitle("订单创建成功！")//设置对话框的标题
                            .setMessage("这是你的订单id:" + result.getInt("orderid"))//设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myintent = new Intent();
                                    myintent.setClass(ConfirmOrderActivity.this, MainActivity.class);
                                    startActivity(myintent);
                                    ConfirmOrderActivity.this.finish();
                                    dialog.dismiss();
                                }
                            }).create();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.show();
            } else {
                AlertDialog dialog = null;
                dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                        .setTitle("订单创建失败")//设置对话框的标题
                        .setMessage("请确认你的个人信息是否完整")//设置对话框的内容
                        //设置对话框的按钮
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }
    }

    private class AsyncJoinTeamTask extends AsyncTask<Map<String, String>, Object, Long> {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp = NetUtil.doPostString(DataManager.serverURL + "/ForPDD/JoinTeam", maps[0]);
                Log.i("ZX", tmp);
                result = null;
                hasresult = false;
                if (tmp != "") {
                    if (tmp.charAt(0) == '{') {
                        hasresult = true;
                        result = new JSONObject(tmp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            if (hasresult) {
                AlertDialog dialog = null;
                try {
                    dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                            .setTitle("订单创建成功！")//设置对话框的标题
                            .setMessage("这是你的订单id:" + result.getInt("orderid"))//设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myintent = new Intent();
                                    myintent.setClass(ConfirmOrderActivity.this, MainActivity.class);
                                    startActivity(myintent);
                                    ConfirmOrderActivity.this.finish();
                                    dialog.dismiss();
                                }
                            }).create();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.show();
            } else {
                AlertDialog dialog = null;
                dialog = new AlertDialog.Builder(ConfirmOrderActivity.this)
                        .setTitle("订单创建失败")//设置对话框的标题
                        .setMessage("请确认你的个人信息是否完整")//设置对话框的内容
                        //设置对话框的按钮
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }
    }
}
