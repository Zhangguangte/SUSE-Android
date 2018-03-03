package com.agmcs.ssuussee.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

import com.agmcs.ssuussee.Activity.NewsContentActivity;
import com.agmcs.ssuussee.Adapter.NewsAdapter;
import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NetUtils;
import com.agmcs.ssuussee.Util.NewsItemBiz;
import com.agmcs.ssuussee.db.Dao;
import com.agmcs.ssuussee.model.NewsItem;
import com.agmcs.ssuussee.model.Newsdata;
import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsPagerFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsPagerFrag extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager layoutManager;

    private static final String NEWS_TYPE = "news_type";

    private boolean is_on_load = false;

    private Dao dao;

    private int currentPage = 1;

    private int news_type;

    private NewsItemBiz newsItemBiz = new NewsItemBiz();
    private NewsAdapter adapter;

    private List<NewsItem> datas = new ArrayList<NewsItem>();

//    private LinearLayout toolbar_tabs;
    private Toolbar toolbar;
//    private FrameLayout framLayout;

    private PagerSlidingTabStrip tabs;

    private boolean controlsVisible = true;

    private BaseApplication app;

    public static NewsPagerFrag newInstance(int type) {
        NewsPagerFrag fragment = new NewsPagerFrag();
        Bundle args = new Bundle();
        args.putInt(NEWS_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsPagerFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            news_type = getArguments().getInt(NEWS_TYPE);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news_pager_item, container, false);
        ButterKnife.inject(this, view);
        dao = Dao.getInstanse(getActivity().getApplicationContext());
        app = (BaseApplication)getActivity().getApplication();
        datas = dao.getNewsList(news_type);
        adapter = new NewsAdapter(datas);
        adapter.setmOnItemClick(new NewsAdapter.onItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), NewsContentActivity.class);
                i.putExtra(NewsContentActivity.URL_DATA,datas.get(position).getLink());
                i.putExtra(NewsContentActivity.TITLE,datas.get(position).getTitle());
                startActivity(i);
            }
            @Override
            public boolean onLongClick(View view, int position) {
                return false;
            }
        });

//        toolbar_tabs = (LinearLayout)getActivity().findViewById(R.id.toolbar_and_tab);
        toolbar = (Toolbar)getActivity().findViewById(R.id.main_toolBar);
//        framLayout = (FrameLayout)getActivity().findViewById(R.id.content);

        tabs = (PagerSlidingTabStrip)getActivity().findViewById(R.id.pager_tabs);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int last_item;
            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && last_item + 1 == datas.size() && !is_on_load) {
                    is_on_load = true;
                    new LoadDatasTask().execute(1);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last_item = layoutManager.findLastVisibleItemPosition();

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //do hide
                    hideToolBar();
                    Log.d("hihi1996", "hide");
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //do show
                    Log.d("hihi1996", "show");
                    showToolBar();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }
            }
        });


        return view;
    }

    private void hideToolBar(){
        tabs.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(3));

    }

    private void showToolBar(){
        tabs.animate().translationY(0).setInterpolator(new AccelerateInterpolator(3));
    }

    @Override
    public void onRefresh() {
        if(NetUtils.checkNet(getActivity())){
            new LoadDatasTask().execute(0);
        }else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
                List<NewsItem> newsItems;
            switch (params[0]){
                case 0:
                    refreshData();
                    return 0;
                case 1:
                    loadData();
                    return 1;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            switch (result){
                case 0:
                    adapter.reAdd(datas);
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    adapter.addAll(datas);
                    is_on_load = false;
                    break;
                case -1:
                    break;
            }
        }

    }

    private void loadData() {
        List<NewsItem> newsItems;
        try {
            currentPage = currentPage + 1;
            newsItems = newsItemBiz.getNewsList(news_type,currentPage);
            Log.d("hihi1996","load count" + newsItems.size());
            datas.addAll(newsItems);
            dao.addNewsList(newsItems);
        } catch (Exception e) {
            e.printStackTrace();
            currentPage -= 1;
        }
    }

    private void refreshData() {
        datas = newsItemBiz.getNewsList(news_type, 1);
        dao.deleteNews(news_type);
        dao.addNewsList(datas);
    }


    @Override
    public void onStart() {
        super.onStart();
        controlsVisible = true;
    }
}

