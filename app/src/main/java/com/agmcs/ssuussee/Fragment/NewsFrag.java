package com.agmcs.ssuussee.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.agmcs.ssuussee.Adapter.NewsPagerAdapter;
import com.agmcs.ssuussee.R;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFrag extends android.support.v4.app.Fragment {
    @InjectView(R.id.pager)
    ViewPager pager;

    private NewsPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;
//    private LinearLayout toolbar_tabs;



    public NewsFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
//        toolbar_tabs = (LinearLayout)getActivity().findViewById(R.id.toolbar_and_tab);
        ButterKnife.inject(this,view);
        adapter = new NewsPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        setHasOptionsMenu(true);


        tabs = (PagerSlidingTabStrip)getActivity().findViewById(R.id.pager_tabs);
//        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.toolbar_and_tab);
        tabs.setViewPager(pager);
        tabs.setTextColorResource(R.color.actionbarTitleColor);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.animate().translationY(0).setInterpolator(new AccelerateInterpolator(1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        tabs.animate().translationY(0).setInterpolator(new AccelerateInterpolator(1));
        tabs.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        tabs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_about:
                new AlertDialog.Builder(getActivity())
                        .setTitle("About")
                        .setMessage("欢迎反馈至skkg@qq.com 积极更新")
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
