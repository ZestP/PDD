package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zest on 2018/3/28.
 */

public class DetailGroupAdapter extends BaseAdapter {
    ArrayList<DetailGroupData> mDetailGroupList;
    Context mContext;
    DetailActivity mDA;
    public DetailGroupAdapter(Context mContext, ArrayList<DetailGroupData> rid,DetailActivity da) {
        this.mContext = mContext;
        this.mDetailGroupList = rid;
        mDA=da;
    }

    @Override
    public int getCount() {
        return mDetailGroupList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDetailGroupList.get(i);
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

    public void addItem(DetailGroupData m) {
        mDetailGroupList.add(m);
    }

    public void removeItem(int position) {
        mDetailGroupList.remove(position);
    }

    private View createView(ViewGroup parent) {
        // TODO Auto-generated method stub
        View convertView;
        ViewHolder vh = new ViewHolder();

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
        vh.mJoin=convertView.findViewById(R.id.group_join);
        vh.mStatus=convertView.findViewById(R.id.group_status);
        convertView.setTag(vh);

        return convertView;
    }

    private void bindViewWithData(final int position, View convertView) {
        ViewHolder vh = (ViewHolder)convertView.getTag();
        vh.mStatus.setText("还差"+mDetailGroupList.get(position).buycnt+"人拼成");
        vh.mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDA.showPickupDialog(mDA.mPrice,false,mDetailGroupList.get(position).teamid);
            }
        });
    }

    public class ViewHolder {
        public TextView mStatus;
        public Button mJoin;
    }

}
