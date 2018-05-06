package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeListAdapter extends BaseAdapter {
    ArrayList<HomeListData> mHomeList;
    Context mContext;

    public HomeListAdapter(Context mContext, ArrayList<HomeListData> rid) {
        this.mContext = mContext;
        this.mHomeList = rid;
    }

    @Override
    public int getCount() {
        return mHomeList.size();
    }

    @Override
    public Object getItem(int i) {
        return mHomeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = createView(parent);
        }
        bindViewWithData(position, convertView);
        return convertView;
    }

    public void addItem(HomeListData m) {
        mHomeList.add(m);
    }

    public void removeItem(int position) {
        mHomeList.remove(position);
    }

    private View createView(ViewGroup parent) {
        // TODO Auto-generated method stub
        View convertView;
        HomeListAdapter.ViewHolder vh = new HomeListAdapter.ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        vh.mPrice=convertView.findViewById(R.id.home_price);
        vh.mName=convertView.findViewById(R.id.home_name);
        vh.mImage=convertView.findViewById(R.id.home_image);
        vh.mOriPrice=convertView.findViewById(R.id.home_oriprice);
        vh.mGoBtn=convertView.findViewById(R.id.home_gobtn);
        convertView.setTag(vh);
        return convertView;
    }

    private void bindViewWithData(final int i, View convertView) {
        ViewHolder vh=(ViewHolder)convertView.getTag();
        vh.mName.setText(mHomeList.get(i).name);
        vh.mPrice.setText("￥"+mHomeList.get(i).price);
        vh.mOriPrice.setText("￥"+mHomeList.get(i).oriprice);
        vh.mImage.setImageBitmap(mHomeList.get(i).bitmap);
        vh.mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent();
                myintent.setClass(mContext, DetailActivity.class);
                myintent.putExtra("GoodID",mHomeList.get(i).goodID);
                mContext.startActivity(myintent);

            }
        });
    }

    public class ViewHolder {
        public TextView mPrice, mOriPrice,mName, mBuyCnt,mGoBtn;
        public ImageView mImage;
    }
}
