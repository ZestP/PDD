package com.heu.groupon.projectgroupon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class RadioGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater = null;

    private List<RadioButton> rdlist = new ArrayList<>();

    public RadioGridAdapter(Context context, List<String> dataList) {
        mContext = context;
        mDatas = dataList;
        mInflater = LayoutInflater.from(mContext);
        for(int i=0;i<mDatas.size();i++)
        {
            rdlist.add(null);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_radio_gridview, parent, false);

            holder.radioButton = (RadioButton) convertView.findViewById(R.id.item_radio_gridView_radioButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.radioButton.setText(mDatas.get(position));
        rdlist.set(position,holder.radioButton);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("---点击了:", position + "");
                for (int i = 0; i < rdlist.size(); i++) {
                    rdlist.get(i).setChecked(false);
                }
                rdlist.get(position).setChecked(true);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private RadioButton radioButton;
    }
}