package com.agmcs.ssuussee.Adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.model.Grade;

import java.util.List;
import java.util.Random;

/**
 * Created by agmcs on 2015/5/27.
 */
public class GradesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Grade> gradeList = null;
    Random random = new Random();

    private static final String[] COLORS = new String[]{"#EF5350","#78909C","#BDBDBD","#FF7043","#FFCA28","#66BB6A","#EC407A","#5C6BC0"};

    public GradesAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item,null,false);

        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        ((GradeViewHolder) holder).title.setText(grade.getTitle());
        ((GradeViewHolder) holder).time.setText(grade.getSchool_year() + "年 第" + grade.getTerm() + "学期");
        ((GradeViewHolder) holder).credit.setText("学分: " + grade.getCredit());
        ((GradeViewHolder) holder).normal_grade.setText("平时成绩: " + grade.getNormal_grade());
        ((GradeViewHolder) holder).final_grade.setText("期末成绩: " + grade.getFinal_grade());
        ((GradeViewHolder) holder).card.setCardBackgroundColor(Color.parseColor(COLORS[grade.getBg()]));
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    class GradeViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView credit;
        TextView normal_grade;
        TextView final_grade;
        CardView card;

        public GradeViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.grade_title);
            time = (TextView)itemView.findViewById(R.id.grade_time);
            credit = (TextView)itemView.findViewById(R.id.grade_credit);
            normal_grade = (TextView)itemView.findViewById(R.id.grade_normal_grade);
            final_grade = (TextView)itemView.findViewById(R.id.grade_final_grade);
            card = (CardView)itemView.findViewById(R.id.grade_card);
        }
    }


    public void reAdd(List<Grade> list){
        gradeList.clear();
        gradeList.addAll(list);
        this.notifyDataSetChanged();
    }
}
