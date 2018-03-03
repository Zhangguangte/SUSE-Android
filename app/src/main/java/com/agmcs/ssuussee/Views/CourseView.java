package com.agmcs.ssuussee.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.agmcs.ssuussee.R;

/**
 * Created by agmcs on 2015/5/23.
 */
public class CourseView extends Button {
    private String title;
    private String classes;
    private String teacher;
    private int start_week;
    private int end_week;
    private int start;
    private int end;
    private int dayOfWeek;
    private int bgResIndex;

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getBgResIndex() {
        return bgResIndex;
    }

    public void setBgResIndex(int bgResIndex) {
        this.bgResIndex = bgResIndex;
    }

    int[] colors = new int[]{R.drawable.ic_course_bg_bohelv, R.drawable.ic_course_bg_cheng, R.drawable.ic_course_bg_cyan,
            R.drawable.ic_course_bg_fen, R.drawable.ic_course_bg_huang,
            R.drawable.ic_course_bg_kafei, R.drawable.ic_course_bg_lan, R.drawable.ic_course_bg_lv,
            R.drawable.ic_course_bg_molan, R.drawable.ic_course_bg_pulan, R.drawable.ic_course_bg_qing,
            R.drawable.ic_course_bg_tao, R.drawable.ic_course_bg_tuhuang, R.drawable.ic_course_bg_zi};

    int[] multi_colors = new int[]{R.drawable.ic_course_bg_bohelv_multi, R.drawable.ic_course_bg_cheng_multi, R.drawable.ic_course_bg_cyan_multi,
            R.drawable.ic_course_bg_fen_multi, R.drawable.ic_course_bg_huang_multi,
            R.drawable.ic_course_bg_kafei_multi, R.drawable.ic_course_bg_lan_multi, R.drawable.ic_course_bg_lv_multi,
            R.drawable.ic_course_bg_molan_multi, R.drawable.ic_course_bg_pulan_multi, R.drawable.ic_course_bg_qing_multi,
            R.drawable.ic_course_bg_tao_multi, R.drawable.ic_course_bg_tuhuang_multi, R.drawable.ic_course_bg_zi_multi
    };

    public CourseView(Context context) {
        this(context, null);
    }

    public CourseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CourseView);
        int n = a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CourseView_course_start:
                    start = a.getInt(R.styleable.CourseView_course_start,1);
                    break;
                case R.styleable.CourseView_course_end:
                    end = a.getInt(R.styleable.CourseView_course_end,1);
                    break;
                case R.styleable.CourseView_course_classes:
                    classes = a.getString(R.styleable.CourseView_course_classes);
                    break;
                case R.styleable.CourseView_course_start_week:
                    start_week = a.getInt(R.styleable.CourseView_course_start_week,1);
                    break;
                case R.styleable.CourseView_course_end_week:
                    end_week = a.getInt(R.styleable.CourseView_course_end_week,1);
                    break;
                case R.styleable.CourseView_course_title:
                    title = a.getString(R.styleable.CourseView_course_title);
                    break;
                case R.styleable.CourseView_course_dayOfWeek:
                    dayOfWeek = a.getInt(R.styleable.CourseView_course_dayOfWeek,0);
                    break;
            }
        }
        a.recycle();
        this.setBackgroundResource(colors[bgResIndex]);
        this.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6.0f, getResources().getDisplayMetrics()));
        this.setTextColor(Color.parseColor("#D5FFFFFF"));
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
//                        getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
//                        getBackground().clearColorFilter();
                        setAlpha(1.0f);
                        break;
                    case MotionEvent.ACTION_CANCEL:
//                        getBackground().clearColorFilter();
                        setAlpha(1.0f);
                        break;
                }
                return false;
            }
        });

    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public int getStart_week() {
        return start_week;
    }

    public void setStart_week(int start_week) {
        this.start_week = start_week;
    }

    public int getEnd_week() {
        return end_week;
    }

    public void setEnd_week(int end_week) {
        this.end_week = end_week;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
