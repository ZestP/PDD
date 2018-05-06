package com.heu.groupon.projectgroupon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DetailActivity extends Activity {
    JSONArray result,resultgroup;
    Bitmap[] mBmp;
    TextView mOriginalPrice;
    ViewPager mBigPics;
    ArrayList<View> mBigPicsArr;
    ListView mDetailGroupList;
    ArrayList<DetailGroupData> mDetailGroupData;

    float mPrice,mOriPrice;
    int mBuyCnt,mGroupCnt,mRepCount;
    String mGoodName,mGoodDesc;



    int goodID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent=getIntent();
        goodID=intent.getIntExtra("GoodID",-1);
        Log.i("ZX",""+goodID);
        Map<String,String> ms=new HashMap<String,String >();
        ms.put("goodid",Integer.toString(goodID));
        AsyncGetResultTask at=new AsyncGetResultTask();
        at.execute(ms);
        AsyncGetGroupTask ag=new AsyncGetGroupTask();
        ag.execute(ms);

    }
    public void showPickupDialog(float price,boolean isSingle,int teamid)
    {
        Bitmap bm=null;
        if(mBmp!=null&&mBmp.length>0)
        {
            bm=mBmp[0];
        }
        PickupDialog.Builder builder=new PickupDialog.Builder(this,R.style.Dialog_FS,goodID,price,mRepCount,bm,isSingle,mGoodName,teamid);
        PickupDialog dialog=builder.createDialog();
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int)display.getHeight()/2;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        lp.gravity= Gravity.BOTTOM;
        win.setAttributes(lp);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }

    private class AsyncGetResultTask extends AsyncTask<Map<String,String>,Object,Long>
    {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetGoods",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    try {
                        result = new JSONArray(tmp);

                            try {
                                JSONObject tj = result.getJSONObject(0);
                                JSONArray bmpjson=tj.getJSONArray("imgurls");
                                if(bmpjson!=null) {
                                    mBmp = new Bitmap[bmpjson.length()];
                                    for(int i=0;i<bmpjson.length();i++)
                                    {
                                        String bmpUrl = DataManager.serverURL+"/ForPDD/GetImage";
                                        Map<String,String> ms=new HashMap<String,String >();
                                        ms.put("imgpath",bmpjson.getString(i));
                                        Log.i("ZX",bmpjson.getString(i));
                                        mBmp[i] = NetUtil.doGetBitmap(bmpUrl, ms);
                                    }

                                }
                                //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {

                try {
                    JSONObject tj=result.getJSONObject(0);
                    mGoodName=tj.getString("goodname");
                    ((TextView)findViewById(R.id.detail_name)).setText(mGoodName);

                    mPrice=Float.parseFloat(tj.getString("teamprice"));
                    ((TextView)findViewById(R.id.detail_price)).setText("￥"+mPrice);

                    mOriPrice=Float.parseFloat(tj.getString("loneprice"));
                    mOriginalPrice=findViewById(R.id.detail_oriprice);
                    mOriginalPrice.setText("￥"+mOriPrice);
                    mOriginalPrice.setPaintFlags(mOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mBigPicsArr=new ArrayList<View>();
                    mBigPics=findViewById(R.id.detail_pics);
                    mRepCount=Integer.parseInt(tj.getString("repertorynum"));
                    mBuyCnt=Integer.parseInt(tj.getString("teampeoplenum"));
                    ((TextView)findViewById(R.id.detail_info)).setText(mBuyCnt+"人已购该商品");

                    ((TextView)findViewById(R.id.detail_description)).setText(tj.getString("description"));
                    LayoutInflater inflater=getLayoutInflater().from(DetailActivity.this);
                    for(int i=0;i<mBmp.length;i++)
                    {
                        View tmp=inflater.inflate(R.layout.detail_bigpic,mBigPics,false);
                        mBigPicsArr.add(tmp);
                        ((ImageView)tmp.findViewById(R.id.detail_bigpic_image)).setImageBitmap(mBmp[i]);
                        ((TextView)tmp.findViewById(R.id.detail_bigpic_count)).setText(Integer.toString(i+1)+"/"+ mBmp.length);
                    }

                    mBigPics.setAdapter(new BigPicsAdapter(mBigPicsArr));





                } catch (JSONException e) {
                    e.printStackTrace();
                }



        }
    }
    private class AsyncGetGroupTask extends AsyncTask<Map<String,String>,Object,Long>
    {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetTeams",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    try {
                        resultgroup = new JSONArray(tmp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            if (resultgroup != null) {
                mGroupCnt=resultgroup.length();
                mDetailGroupData=new ArrayList<DetailGroupData>();
                Log.i("ZX","GROUP:"+mGroupCnt);

                if(mGroupCnt>0){
                    ((TextView)findViewById(R.id.detail_groupinfo)).setText(mGroupCnt+"人在拼单,可直接参与");
                }else{
                    ((TextView)findViewById(R.id.detail_groupinfo)).setText(mGroupCnt+"人在拼单");
                }
                for (int i = 0; i < resultgroup.length(); i++) {
                    try {
                        DetailGroupData dg=new DetailGroupData();
                        JSONObject tmp=resultgroup.getJSONObject(i);
                        dg.teamid=tmp.getInt("teamid");
                        dg.buycnt=tmp.getInt("teamstatus");
                        mDetailGroupData.add(dg);
                        //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                mDetailGroupList=findViewById(R.id.detail_grouplist);
                mDetailGroupList.setAdapter(new DetailGroupAdapter(DetailActivity.this,mDetailGroupData,DetailActivity.this));
                setListViewHeightBasedOnChildren(mDetailGroupList);
                ((Button)findViewById(R.id.detail_announce_single)).setText("单独购买\n￥"+mOriPrice);
                findViewById(R.id.detail_announce_single).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPickupDialog(mOriPrice,true,-1);
                    }
                });
                ((Button)findViewById(R.id.detail_announce)).setText("发起拼单\n￥"+mPrice);
                findViewById(R.id.detail_announce).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPickupDialog(mPrice,false,-1);
                    }
                });
            } else {
                Log.i("ZX", "RESULT IS NULL");
            }
        }
    }
}