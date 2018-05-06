package com.heu.groupon.projectgroupon;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by shado on 2018/3/26.
 */

public class BigPicsAdapter extends PagerAdapter {
    ArrayList<View> mViews;

    public BigPicsAdapter() {

    }

    public BigPicsAdapter(ArrayList<View> arr) {
        mViews = arr;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        //根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
        return arg0 == mViews.get((int) Integer.parseInt(arg1.toString()));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(mViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(mViews.get(position));

        //把当前新增视图的位置（position）作为Key传过去
        return position;
    }

}
