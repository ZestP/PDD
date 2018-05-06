package com.heu.groupon.projectgroupon;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {
    View mView;
    EditText mSearchEdit;
    Context mContext;
    JSONArray result;
    Bitmap[] mBmp;
    public SearchFragment()
    {}
    public SearchFragment(Context con)
    {
        mContext=con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_search, container, false);
        InitView();
        return mView;
    }
    protected void InitView()
    {
        mSearchEdit=mView.findViewById(R.id.search_input);
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(mContext, "Search", Toast.LENGTH_SHORT).show();
                    // search pressed and perform your functionality.
                }
                if(!mSearchEdit.getText().equals(""))
                {
                    Map<String,String> ms=new HashMap<String,String >();
                    ms.put("goodname",mSearchEdit.getText().toString());
                    AsyncGetResultTask at=new AsyncGetResultTask();
                    at.execute(ms);
                }
                return true;
            }
        });
    }


    private class AsyncGetResultTask extends AsyncTask<Map<String,String>,Object,Long>
    {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetGoods",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    try {
                        result = new JSONArray(tmp);
                        mBmp = new Bitmap[result.length()];
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject tj = result.getJSONObject(i);
                                JSONArray bmpjson=tj.getJSONArray("imgurls");
                                if(bmpjson!=null) {
                                    String bmpUrl = DataManager.serverURL + "/ForPDD/GetImage";
                                    Map<String, String> ms = new HashMap<String, String>();
                                    ms.put("imgpath", bmpjson.getString(0));
                                    mBmp[i] = NetUtil.doGetBitmap(bmpUrl, ms);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            ArrayList<ResultItemData> rid=new ArrayList<ResultItemData>();
            if(result!=null) {
                for (int i = 0; i < result.length(); i++) {
                    ResultItemData tmp = new ResultItemData();
                    try {
                        JSONObject tj = result.getJSONObject(i);
                        tmp.name = tj.getString("goodname");
                        tmp.price = Float.parseFloat(tj.getString("teamprice"));
                        tmp.bitmap = mBmp[i];
                        tmp.goodID = Integer.parseInt(tj.getString("goodid"));
                        Log.i("ZX", tmp.name + " " + tmp.price);
                        //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    rid.add(tmp);
                }

                ResultGridAdapter rg = new ResultGridAdapter(mContext, rid);
                GridView gv = mView.findViewById(R.id.result_grid);
                gv.setAdapter(rg);
            }else{
                Log.i("ZX","RESULT IS NULL");
            }
        }
    }
}
