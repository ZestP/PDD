package com.heu.groupon.projectgroupon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ResultGridAdapter extends BaseAdapter {
    List<ResultItemData> mResultItemList;
    Context mContext;
    public ResultGridAdapter(Context mContext, List<ResultItemData> rid) {
        this.mContext = mContext;
        this.mResultItemList = rid;
    }
    @Override
    public int getCount() {
        return mResultItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mResultItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = createView(parent);
        }
        bindViewWithData(position, convertView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent();
                myintent.setClass(mContext, DetailActivity.class);
                myintent.putExtra("GoodID",mResultItemList.get(position).goodID);
                mContext.startActivity(myintent);
            }
        });
        return convertView;
    }

    public void addItem(ResultItemData m) {
        mResultItemList.add(m);
    }

    public void removeItem(int position) {
        mResultItemList.remove(position);
    }

    private View createView(ViewGroup parent) {
        // TODO Auto-generated method stub
        View convertView;
        ViewHolder vh = new ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result, parent, false);
        vh.mName = convertView.findViewById(R.id.name);
        vh.mPrice = convertView.findViewById(R.id.price);
        vh.mBuyCnt = convertView.findViewById(R.id.buycnt);
        vh.mImage=convertView.findViewById(R.id.image);
        convertView.setTag(vh);
        return convertView;
    }

    private void bindViewWithData(int position, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.mBuyCnt.setText(mResultItemList.get(position).buycnt+"人已购买");
        vh.mName.setText(mResultItemList.get(position).name);
        vh.mPrice.setText("￥"+mResultItemList.get(position).price);
        vh.mImage.setImageBitmap(mResultItemList.get(position).bitmap);
        //vh.mName.setText(mResultItemList.get(position).name);

    }

    public class ViewHolder {
        public TextView mPrice, mName,mBuyCnt;
        public ImageView mImage;
    }
}
