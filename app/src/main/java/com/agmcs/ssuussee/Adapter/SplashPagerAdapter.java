package com.agmcs.ssuussee.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by agmcs on 2015/5/28.
 */
public class SplashPagerAdapter extends PagerAdapter {

    //界面列表
    private List<View> views;

    public SplashPagerAdapter (List<View> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == views.get((int)Integer.parseInt(object.toString()));
    }
}
