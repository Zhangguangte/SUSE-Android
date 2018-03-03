package com.agmcs.ssuussee.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.agmcs.ssuussee.Adapter.SplashPagerAdapter;
import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.Fragment.GradeFrag;
import com.agmcs.ssuussee.Fragment.LibraryFrag;
import com.agmcs.ssuussee.Fragment.NewsFrag;
import com.agmcs.ssuussee.Fragment.SchedulesFrag;
import com.agmcs.ssuussee.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.drawLayout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.list_menu)
    ListView list_menu;

    @InjectView(R.id.main_toolBar)
    Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fm;
    private NewsFrag newsFrag;
    private LibraryFrag libraryFrag;
    private SchedulesFrag schedulesFrag;
    private GradeFrag gradeFrag;

    BaseApplication app;
    public static final String[] MENUS = new String[]{"资讯","课表","图书馆","成绩"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        app = (BaseApplication)getApplication();
        SharedPreferences spf = app.getSpf();
        //基础
        MobclickAgent.updateOnlineConfig(this);
        //更新
        UmengUpdateAgent.update(this);

        if(spf.getBoolean("isfirstrun",true)){
            spf.edit().putBoolean("isfirstrun",false).commit();
            startActivity(new Intent(MainActivity.this, FirstSplashScreenActivity.class));
            finish();
        }

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }

//        ToolBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

//        菜单
//        list_menu.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_item, MENUS));
        int[] MENUS_IMG = new int[]{R.drawable.ic_public_grey600_18dp, R.drawable.ic_event_note_grey600_18dp,
                R.drawable.ic_book_grey600_18dp, R.drawable.ic_school_grey600_18dp};
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < MENUS.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("img", MENUS_IMG[i]);
            item.put("title", MENUS[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.menu_item, new String[] { "img", "title"},
                new int[] { R.id.menu_img, R.id.menu_title});
        list_menu.setAdapter(simpleAdapter);
        list_menu.setSelection(0);
        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menu_selected(position);
            }
        });

        fm = getSupportFragmentManager();
        menu_selected(0);
    }

    private void menu_selected(int position) {
        //todo
        list_menu.setItemChecked(position, true);
        switch (position){
            case 0:
                if(newsFrag == null){
                    newsFrag = new NewsFrag();
                }
                fm.beginTransaction().replace(R.id.content, newsFrag).commitAllowingStateLoss();
                break;
            case 1:
                if(schedulesFrag == null){
                    schedulesFrag = new SchedulesFrag();
                }
                fm.beginTransaction().replace(R.id.content, schedulesFrag).commitAllowingStateLoss();
                break;
            case 2:
                if(libraryFrag == null){
                    libraryFrag = new LibraryFrag();
                }
                fm.beginTransaction().replace(R.id.content, libraryFrag).commitAllowingStateLoss();
                break;
            case 3:
                if(gradeFrag == null){
                    gradeFrag = new GradeFrag();
                }
                fm.beginTransaction().replace(R.id.content, gradeFrag).commitAllowingStateLoss();
                break;
        }
        setTitle(MENUS[position]);
        drawerLayout.closeDrawers();
    }



    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
