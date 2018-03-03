package com.agmcs.ssuussee.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agmcs.ssuussee.Activity.CourseDetailActivity;
import com.agmcs.ssuussee.Activity.Login_JwActivity;
import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NetUtils;
import com.agmcs.ssuussee.Views.CourseView;
import com.agmcs.ssuussee.Views.TimeTable;
import com.agmcs.ssuussee.db.Dao;
import com.agmcs.ssuussee.model.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SchedulesFrag extends android.support.v4.app.Fragment {
    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int SET_CURRENT_WEEK_CODE = 2;
    Dao dao;
    BaseApplication app = null;

    List<Course> courseList = new ArrayList<Course>();

    @InjectView(R.id.timeTable)
    TimeTable timeTable;

    private static final int REFRESH = 0;
    private Button drop_down_btn;
    private RelativeLayout drop_down_btn_parentView;

    private PopupWindow popupWindow;

    private int current_week;
    private int real_current_week;
    private String[] week;
    private static final String[] weekList = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期天"};

    public SchedulesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedules, container, false);
        ButterKnife.inject(this,view);
        app = (BaseApplication)getActivity().getApplication();
        dao = Dao.getInstanse(getActivity());

        current_week = app.getCurrentWeek();
        real_current_week = current_week;

        courseList = app.getCourseList();
        if(courseList.size() > 0){
            generateCourseView(courseList);
        }
        timeTable.setCurrent_week(current_week);
        timeTable.setMlickListener(new TimeTable.ClickListener() {
            @Override
            public void onClick(int num) {
                Log.d("hihi1996", num + "ddsddd");
                final List<CourseView> list = new ArrayList<CourseView>();
                int cur = 0;
                if (num == 0) {
                    list.add((CourseView) timeTable.getChildAt(cur));
                }
                while (num != 0) {
                    cur = num % 100;
                    num = num / 100;
                    list.add((CourseView) timeTable.getChildAt(cur));
                }
                if (list.size() == 1) {
                    showCourseDetail(list.get(0));
                }else{
                    ListView listView = new ListView(getActivity());
                    listView.setAdapter(new ArrayAdapter<CourseView>(getActivity(),android.R.layout.simple_list_item_1,list));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            showCourseDetail(list.get(i));
                        }
                    });
                    new AlertDialog.Builder(getActivity())
                            .setView(listView)
                            .show();
                }
            }
        });

        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.main_toolBar);
        drop_down_btn = (Button)toolbar.findViewById(R.id.toolBar_btn);
        drop_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_popupwindow(v);
            }
        });
        drop_down_btn_parentView =(RelativeLayout)toolbar.findViewById(R.id.toolBar_btn_parent);

        week = new String[]{"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周",
                "第10周","第11周","第12周","第13周","第14周","第15周","第16周","第17周","第18周","第19周",
                "第20周","第21周","第22周","第23周","第24周","第25周"};

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedules_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.schedules_login:
                if(NetUtils.checkNet(getActivity())){
                    Intent intent = new Intent(getActivity(), Login_JwActivity.class);
                    intent.putExtra(Login_JwActivity.TYPE_KEY, Login_JwActivity.TYPE_SCHEDULES);
                    startActivityForResult(intent,LOGIN_REQUEST_CODE);
                }else{
                    Toast.makeText(getActivity(), "你没联网啊~~", Toast.LENGTH_SHORT).show();
                }
//                if(NetUtils.checkNet(getActivity())){
//                    LoginFragmentDialog dialog = LoginFragmentDialog.newInstance(LoginFragmentDialog.JW_DIALOG);
//                    dialog.setTargetFragment(this,LOGIN_REQUEST_CODE);
//                    dialog.show(getFragmentManager(),"JWGL_LOGIN");
//                }else{
//                    Toast.makeText(getActivity(),"你没联网啊~~",Toast.LENGTH_SHORT).show();
//                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE){
            switch (resultCode){
                case Login_JwActivity.READ_SUCCESS:
                    courseList = app.getCourseList();
                    generateCourseView(courseList);
                    break;
            }
        }else if (requestCode == SET_CURRENT_WEEK_CODE){
            switch (resultCode){
                case SetCurrentWeekDialog.SUCCES_RESULT:
                    current_week = data.getIntExtra(SetCurrentWeekDialog.CHOICE,1);
                    app.saveCurretWeek(current_week);
                    real_current_week = current_week;
                    setCurrentWeek();
                    popupWindow.dismiss();
                    break;
            }
        }
    }



    private void generateCourseView(List<Course> courseList){
//        Drawable shape = getResources().getDrawable(R.drawable.);
        try {
            timeTable.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(courseList == null){
            return;
        }
        for(int i=0; i<courseList.size(); i++){
            Course course = courseList.get(i);
            CourseView courseView = new CourseView(getActivity());
            courseView.setTitle(course.getTitle());
            courseView.setStart(course.getStart());
            courseView.setEnd(course.getEnd());
            courseView.setDayOfWeek(course.getDay_of_week());
            courseView.setStart_week(course.getStart_week());
            courseView.setEnd_week(course.getEnd_week());
            courseView.setTeacher(course.getTeacher());
            courseView.setClasses(course.getClasses());
            courseView.setGravity(Gravity.CENTER);
            courseView.setText(course.getTitle() + "\n@" + course.getClasses());
            courseView.setBgResIndex(course.getBgResIndex());
            timeTable.addView(courseView);
        }
    }

    class PopupWindowAdapter extends ArrayAdapter<String> {
        private String[] list;


        public PopupWindowAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.course_popup_item,null,false);
                holder = new ViewHolder();
                holder.text = (TextView)view.findViewById(R.id.course_popup_week);
                view.setTag(holder);
            }else{
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }
            if(real_current_week-1 == position){
                holder.text.setText(list[position] + " (当前周)");
            }else {
                holder.text.setText(list[position]);
            }

            return view;
        }

        class ViewHolder{
            TextView text;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        current_week = app.getCurrentWeek();
        show_drop_down();
    }


    @Override
    public void onStop() {
        super.onStop();
        hide_drop_down();
    }

    private void show_drop_down() {
//        drop_down_btn.setVisibility(View.VISIBLE);
        drop_down_btn_parentView.setVisibility(View.VISIBLE);
        setCurrentWeek();
    }

    private void setCurrentWeek() {
        if(current_week == real_current_week){
            drop_down_btn.setText(week[current_week - 1]);
        }else {
            drop_down_btn.setText(week[current_week - 1] + "(非当前周)");
        }
        timeTable.setCurrent_week(current_week);
        timeTable.requestLayout();
    }

    private void hide_drop_down() {
//        drop_down_btn.setVisibility(View.GONE);
        drop_down_btn_parentView.setVisibility(View.GONE);
    }

    private void show_popupwindow(View toggleView){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.course_popup_windows,null);

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics()));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        ListView listView = (ListView)view.findViewById(R.id.course_popup_listview);
        listView.setAdapter(new PopupWindowAdapter(getActivity(), R.layout.course_popup_item, week));
        listView.setSelection(real_current_week - 1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_week = position + 1;
                setCurrentWeek();
                popupWindow.dismiss();
            }
        });

        Button modify_current_week = (Button)view.findViewById(R.id.modify_current_week);
        modify_current_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCurrentWeekDialog dialog = new SetCurrentWeekDialog();
                dialog.setTargetFragment(SchedulesFrag.this, SET_CURRENT_WEEK_CODE);
                dialog.show(getFragmentManager(), "SET_CURRENT_WEEK_DIALOG");
            }
        });

        popupWindow.showAsDropDown(toggleView,0,0);
    }


    private void showCourseDetail(CourseView courseView){
        String jiesu = weekList[courseView.getDayOfWeek()] + " 第" + courseView.getStart() + "-" + courseView.getEnd() + "节";
        String zousu =  courseView.getStart_week() + "至" + courseView.getEnd_week() + "周";
        Intent intent = new Intent(getActivity(),CourseDetailActivity.class);
        intent.putExtra(CourseDetailActivity.TITLE,courseView.getTitle());
        intent.putExtra(CourseDetailActivity.TEACHER,courseView.getTeacher());
        intent.putExtra(CourseDetailActivity.CLASSES, courseView.getClasses());
        intent.putExtra(CourseDetailActivity.JIESU,jiesu);
        intent.putExtra(CourseDetailActivity.ZOUSU,zousu);
        startActivity(intent);
    }


}
