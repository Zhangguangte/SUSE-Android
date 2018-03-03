package com.agmcs.ssuussee.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agmcs.ssuussee.Fragment.NewsPagerFrag;

/**
 * Created by agmcs on 2015/5/19.
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TITLES = new String[]{"新闻动态","公告"};

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        NewsPagerFrag fragment = NewsPagerFrag.newInstance(position);
//        LibraryFrag fragment = NewsPagerFrag.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
