package com.agmcs.ssuussee.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.agmcs.ssuussee.R;

import java.util.List;

/**
 * Created by agmcs on 2015/5/23.
 */
public class TimeTable extends ViewGroup {

    private List<CourseView> list;
    int each_width;
    int devideLine = 3;
    int current_week = 1;

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

    public interface ClickListener{
        void onClick(int list);
    }

    ClickListener mlickListener;


    public TimeTable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTable(Context context) {
        this(context, null);
    }

    public TimeTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCurrent_week(int current_week) {
        this.current_week = current_week;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public void setMlickListener(ClickListener mlickListener) {
        this.mlickListener = mlickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WindowManager manager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        each_width = (int)((width - 8 * devideLine) / 7);
        int height = 15*devideLine + 14 * each_width;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("hihi1995", "onlayout");
        int cCount = getChildCount();
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        //记录
        int[][] book = new int[8][15];

        for(int i=0;i<cCount; i++){
            final CourseView view = (CourseView)getChildAt(i);
            int dayOfWeek = view.getDayOfWeek();
            int start = view.getStart();
            int end = view.getEnd();

            //当前周不上的课程涂灰
            if(!isCurrentWeekCourse(view)){
                view.setBackgroundResource(R.drawable.ic_course_bg_hui);
                view.setTextColor(Color.parseColor("#D5777777"));
            }else{
                view.setBackgroundResource(colors[view.getBgResIndex()]);
                view.setTextColor(Color.parseColor("#D5FFFFFF"));
            }


            //计算坐标
            left = (dayOfWeek+1)*devideLine + dayOfWeek * each_width;
            right = left + each_width;
            top = start * devideLine + (start-1)*each_width;
            bottom = top + (end - start +1)*each_width + (end-start-1)*devideLine;



            for(int j=start;j<=end;j++) {
                if (book[dayOfWeek][j] != 0) {//有冲突
                    int course_id_list = book[dayOfWeek][j] * 100 + i;
                    int course_id = 0;
                    boolean haveCourseInCurrentWeek = false;
                    while (course_id_list != 0) {
                        course_id = course_id_list % 100;
                        course_id_list = course_id_list / 100;
                        CourseView courseView = (CourseView) getChildAt(course_id);
                        //有冲突,并且这节课是这周上的
                        if (isCurrentWeekCourse(courseView)) {
                            haveCourseInCurrentWeek = true;
                            courseView.setVisibility(VISIBLE);
                            courseView.setBackgroundResource(multi_colors[courseView.getBgResIndex()]);
                            final int num = book[dayOfWeek][j] * 100 + i;
                            if (mlickListener != null) {
                                courseView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mlickListener.onClick(num);
                                    }
                                });
                            }
                        } else {//不是这周上的课
                            courseView.setVisibility(GONE);
                            final int num = book[dayOfWeek][j] * 100 + i;
                            if (mlickListener != null) {
                                courseView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mlickListener.onClick(num);
                                    }
                                });
                            }
                        }
                    }
                    if (!haveCourseInCurrentWeek) {
                        view.setVisibility(VISIBLE);
                    }
                } else {//没有冲突的课
                    final int num = i;
                    if (mlickListener != null) {
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mlickListener.onClick(num);
                            }
                        });
                    }
                }

            }

            //记录这节课到Book
            for(int j=start;j<=end;j++){
                book[dayOfWeek][j] = book[dayOfWeek][j] * 100 + i;
            }


            view.layout(left, top, right, bottom);
        }


    }

    private boolean isCurrentWeekCourse(CourseView view){
        return (view.getStart_week()<=current_week && view.getEnd_week()>=current_week);
    }
}
