package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFragment extends Fragment {
    Context mContext;
    View mView;
    JSONArray result;
    Bitmap[] mBmp;
    ArrayList<OrderItemData> rid;
    public MyFragment()
    {}
    public MyFragment(Context con)
    {
        mContext=con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_my, container, false);
        InitView();
        return mView;
    }
    protected void InitView()
    {
        ((TextView)mView.findViewById(R.id.my_title)).setText(DataManager.username);
        Map<String,String> ms=new HashMap<String,String >();
        ms.put("userid",DataManager.userid);
        AsyncGetOrderTask at=new AsyncGetOrderTask();
        at.execute(ms);
    }
    private class AsyncGetOrderTask extends AsyncTask<Map<String,String>,Object,Long>
    {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
                rid=new ArrayList<OrderItemData>();
                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetOrders",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    try {
                        result = new JSONArray(tmp);

                        for (int i = 0; i < result.length(); i++) {
                            OrderItemData oid = new OrderItemData();
                            try {

                                JSONObject tj = result.getJSONObject(i);


                                oid.status = tj.getString("orderstatus");

                                oid.orderID = Integer.parseInt(tj.getString("orderid"));
                                oid.goodID = Integer.parseInt(tj.getString("goodid"));
                                Map<String,String> ms=new HashMap<String,String >();
                                ms.put("goodid",Integer.toString(oid.goodID));
                                Log.i("XC","TMP2GOOOID:"+oid.goodID);
                                String tmp2=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetGoods",ms);
                                Log.i("XC","TMP2:"+tmp2);
                                if(!tmp2.equals("")) {
                                    JSONArray result2=new JSONArray(tmp2);
                                    JSONObject currentGood=result2.getJSONObject(0);
                                    oid.name = currentGood.getString("goodname");
                                    oid.cost = Float.parseFloat(currentGood.getString("teamprice"));
                                    JSONArray bmpjson=currentGood.getJSONArray("imgurls");
                                    if(bmpjson!=null) {
                                        String bmpUrl = DataManager.serverURL + "/ForPDD/GetImage";
                                        Map<String, String> ms2 = new HashMap<String, String>();
                                        ms2.put("imgpath", bmpjson.getString(0));
                                        mBmp[i] = NetUtil.doGetBitmap(bmpUrl, ms2);
                                    }
                                    oid.bitmap = mBmp[i];
                                }
                                //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            rid.add(oid);
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
            if(result!=null) {


                OrderListAdapter rg = new OrderListAdapter(mContext, rid);
                ListView lv = mView.findViewById(R.id.my_orderlist);
                lv.setAdapter(rg);
            }else{
                Log.i("ZX","RESULT IS NULL");
            }
        }
    }
}
