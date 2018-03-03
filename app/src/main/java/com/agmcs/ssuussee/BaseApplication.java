package com.agmcs.ssuussee;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.agmcs.ssuussee.db.Dao;
import com.agmcs.ssuussee.model.Book;
import com.agmcs.ssuussee.model.Course;
import com.agmcs.ssuussee.model.Grade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by agmcs on 2015/5/22.
 */
public class BaseApplication extends Application {
    String student_num = null;
    String password = null;
    String password_tsg = null;
    int term_start_week;
    List<Course> courseList = null;
    List<Book> bookList = new ArrayList<Book>();
    List<Grade> gradeList = new ArrayList<Grade>();



    String student_num_tsg = null;
    SharedPreferences spf = null;

    public SharedPreferences getSpf() {
        return spf;
    }

    public void setSpf(SharedPreferences spf) {
        this.spf = spf;
    }

    @Override
    public void onCreate() {
        Log.d("hihi123", "create");
        super.onCreate();
        spf = PreferenceManager.getDefaultSharedPreferences(this);
        password = spf.getString("password", "");
        password_tsg = spf.getString("password_tsg","");
        student_num = spf.getString("student_num", "");
        student_num_tsg = spf.getString("student_num_tsg", "");
        term_start_week = spf.getInt("term_start_week", 0);
        Dao dao = Dao.getInstanse(this);


        courseList = dao.getCourseList();
        bookList = dao.getBookList();
        gradeList = dao.getGradeList();
        Log.d("hihi1996","create finish");
    }


    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    //save by SharedPreferences
    public int getCurrentWeek() {
//        Date now = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(term_start_week);
//        long time = now.getTime() - c.getTimeInMillis();
//        int week = (int)Math.floor(time / (1000 * 60 * 60 * 24.0 * 7));
//        Log.d("hihi1995","GETCURRENTWEEK" + week);
//        if(week>25||week<1){
//            return 1;
//        }
        Calendar cl = Calendar.getInstance();
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.SECOND, 0);
        cl.set(Calendar.MINUTE,0);

        return cl.get(Calendar.WEEK_OF_YEAR) - term_start_week;
    }

    public void saveCurretWeek(int week) {
//        Log.d("hihi1995","save CurrentWeek" + week);
//        if(week<1 || week>25){
//            return;
//        }
//        Calendar now = Calendar.getInstance();
//        now.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
//        long timeInMillis = Math.round(now.getTimeInMillis() - (1000 * 60 * 60 * 24.0) * 7 * week);
//        Calendar start_date = Calendar.getInstance();
//        start_date.setTimeInMillis(timeInMillis);
//        this.term_start_week = start_date.getTimeInMillis();

        Calendar cl = Calendar.getInstance();
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        cl.set(Calendar.HOUR_OF_DAY,0);
        cl.set(Calendar.SECOND, 0);
        cl.set(Calendar.MINUTE, 0);

        int week_of_year = cl.get(Calendar.WEEK_OF_YEAR);
        this.term_start_week = week_of_year - week;
        spf.edit().putInt("term_start_week", term_start_week).apply();
    }

    public void saveStudent_num(String student_num) {
        this.student_num = student_num;
        spf.edit().putString("student_num",student_num).apply();
    }

    public void savePassword(String password) {
        this.password = password;
        spf.edit().putString("password",password).apply();
    }

    public void savePassword_tsg(String password_tsg) {
        this.password_tsg = password_tsg;
        spf.edit().putString("password_tsg",password_tsg).apply();
    }


    //getters

    public String getStudent_num() {
        return student_num;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_tsg() {
        return password_tsg;
    }


    public String getStudent_num_tsg() {
        return student_num_tsg;
    }

    public void saveStudent_num_tsg(String student_num_tsg) {
        this.student_num_tsg = student_num_tsg;
        spf.edit().putString("student_num_tsg",student_num_tsg).apply();
    }
}
