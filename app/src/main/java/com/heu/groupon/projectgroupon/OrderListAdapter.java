package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {
    ArrayList<OrderItemData> mOrderList;
    Context mContext;

    public OrderListAdapter(Context mContext, ArrayList<OrderItemData> rid) {
        this.mContext = mContext;
        this.mOrderList = rid;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrderList.get(i);
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

    public void addItem(OrderItemData m) {
        mOrderList.add(m);
    }

    public void removeItem(int position) {
        mOrderList.remove(position);
    }

    private View createView(ViewGroup parent) {
        // TODO Auto-generated method stub
        View convertView;
        OrderListAdapter.ViewHolder vh = new OrderListAdapter.ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        vh.mCost=convertView.findViewById(R.id.order_cost);
        vh.mName=convertView.findViewById(R.id.order_name);
        vh.mStatus=convertView.findViewById(R.id.order_status);
        vh.mImage=convertView.findViewById(R.id.image);
        vh.mRefund=convertView.findViewById(R.id.order_refund);
        vh.mConfirm=convertView.findViewById(R.id.order_confirm);
        vh.mConfirm=convertView.findViewById(R.id.order_confirm);
        convertView.setTag(vh);
        return convertView;
    }

    private void bindViewWithData(final int i, View convertView) {
        ViewHolder vh=(ViewHolder)convertView.getTag();
        vh.mName.setText(mOrderList.get(i).name);
        vh.mCost.setText("￥"+mOrderList.get(i).cost);
        vh.mStatus.setText(mOrderList.get(i).status);
        vh.mImage.setImageBitmap(mOrderList.get(i).bitmap);
        vh.mRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        vh.mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    public class ViewHolder {
        public TextView mCost,mName,mStatus;
        public Button mRefund,mConfirm;
        public ImageView mImage;
    }
}
