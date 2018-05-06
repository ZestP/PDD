package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    HorizontalListView hListView;
    HorizontalListViewAdapter hListViewAdapter;
    ImageView previewImg;
    View olderSelectView = null,olderSelected=null;
    View v;
    Context mContext;
    JSONArray result,resulttype;
    Bitmap[] mBmp;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    public HomeFragment(){}
    public HomeFragment(Context con)
    {
        mContext=con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        initHomeList();
        return v;
    }
    public void initUI(){
        hListView = (HorizontalListView)v.findViewById(R.id.home_horizontal);




        mSharedPreferences = mContext.getSharedPreferences("PDD", MODE_PRIVATE);
        DataManager.goodstypes = mSharedPreferences.getStringSet("goodstypes", null);
        if(DataManager.goodstypes!=null)
        {
            initTitle();
        }
        Map<String, String> ms = new HashMap<String, String>();
        AsyncGetGoodsTypesTask at = new AsyncGetGoodsTypesTask();
        at.execute(ms);





    }
    public void initHomeList()
    {
        Map<String,String> ms=new HashMap<String,String >();
        AsyncGetResultTask at=new AsyncGetResultTask();
        at.execute(ms);
    }
    public void diveIntoSpecifiedCatgory(String cat)
    {
        Map<String,String> ms=new HashMap<String,String >();
        ms.put("goodtype",cat);
        AsyncGetResultTask at=new AsyncGetResultTask();
        at.execute(ms);
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
                        mBmp = new Bitmap[result.length()];
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject tj = result.getJSONObject(i);
                                JSONArray bmpjson=tj.getJSONArray("imgurls");
                                if(bmpjson!=null) {
                                    String bmpUrl = DataManager.serverURL+"/ForPDD/GetImage";
                                    Map<String,String> ms=new HashMap<String,String >();
                                    ms.put("imgpath",bmpjson.getString(0));
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
            ArrayList<HomeListData> rid=new ArrayList<HomeListData>();

            if(result!=null) {
                for (int i = 0; i < result.length(); i++) {
                    HomeListData tmp = new HomeListData();
                    try {
                        JSONObject tj = result.getJSONObject(i);
                        tmp.name = tj.getString("goodname");
                        tmp.price = Float.parseFloat(tj.getString("teamprice"));
                        tmp.oriprice = Float.parseFloat(tj.getString("loneprice"));
                        tmp.bitmap = mBmp[i];
                        tmp.goodID = Integer.parseInt(tj.getString("goodid"));
                        Log.i("ZX", tmp.name + " " + tmp.price);
                        //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    rid.add(tmp);
                }



                HomeListAdapter rg=new HomeListAdapter(mContext,rid);
                ListView lv=v.findViewById(R.id.home_list);
                lv.setAdapter(rg);
            }else{
                Log.i("ZX","RESULT IS NULL");
            }
        }
    }

    public void initTitle() {
        String[] titles = new String[DataManager.goodstypes.size()+1];
        titles[0]="热门";
        int cnt = 1;
        for (String s : DataManager.goodstypes) {
            titles[cnt] = s;
            cnt++;
        }
        hListViewAdapter = new HorizontalListViewAdapter(mContext, titles);
        hListView.setAdapter(hListViewAdapter);
        hListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (olderSelected != null) {
                    olderSelected.setSelected(false); //上一个选中的View恢复原背景
//                          ((TextView)olderSelected.findViewById(R.id.text_list_item)).setTextColor(Color.BLACK);
                }
                olderSelected = view;
                view.setSelected(true);
//                      ((TextView)view.findViewById(R.id.text_list_item)).setTextColor(Color.RED);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i("ZX", "HItemClicked");
                if (olderSelectView == null) {
                    olderSelectView = view;
                } else {
                    olderSelectView.setSelected(false);
                    ((TextView) olderSelected.findViewById(R.id.text_list_item)).setTextColor(Color.parseColor("#000000"));
                    olderSelectView = null;
                }
                olderSelectView = view;
                view.setSelected(true);
                String selName=((TextView) view.findViewById(R.id.text_list_item)).getText().toString();
                Log.i("ZX",selName);
                if(selName=="热门")
                {
                    initHomeList();
                }else{
                    diveIntoSpecifiedCatgory(selName);
                }
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();

            }
        });
    }



    private class AsyncGetGoodsTypesTask extends AsyncTask<Map<String,String>,Object,Long>
    {
        @Override
        protected Long doInBackground(Map<String, String>[] maps) {
            try {
//                    Map<String,String> ms=new HashMap<String,String >();
//                    ms.put("goodname","6");
                String tmp=NetUtil.doPostString(DataManager.serverURL+"/ForPDD/GetGoodsTypes",maps[0]);
                Log.i("ZX", tmp);
                if(!tmp.equals("")) {
                    try {
                        resulttype = new JSONArray(tmp);
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


            if (resulttype != null) {
                DataManager.goodstypes=new HashSet<String>();
                for (int i = 0; i < resulttype.length(); i++) {
                    try {
                        String tj = resulttype.getString(i);
                        DataManager.goodstypes.add(tj);
                        Log.i("ZX", tj);
                        //tmp.buycnt= Integer.parseInt(tj.getString("teamprice"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                initTitle();
            } else {
                Log.i("ZX", "RESULT IS NULL");
            }
        }
    }
}
