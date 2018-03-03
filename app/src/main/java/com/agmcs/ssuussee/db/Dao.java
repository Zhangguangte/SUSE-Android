package com.agmcs.ssuussee.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.agmcs.ssuussee.model.Book;
import com.agmcs.ssuussee.model.Course;
import com.agmcs.ssuussee.model.Grade;
import com.agmcs.ssuussee.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agmcs on 2015/5/20.
 */
public class Dao {
    private static Dao dao;
    private SQLiteDatabase db;

    public Dao(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static Dao getInstanse(Context context){
        if(dao ==null){
            dao = new Dao(context);
        }
        return dao;
    }
    public void addNews(NewsItem newsItem){
        if(newsItem != null){
            ContentValues cv = new ContentValues();
            cv.put("title",newsItem.getTitle());
            cv.put("link",newsItem.getLink());
            cv.put("date",newsItem.getDate());
            cv.put("newstype", newsItem.getNewsType());
            db.insert(DbHelper.DB_NEWS_NAME, null, cv);

        }
    }

    public void addNewsList(List<NewsItem> datas){
        for(NewsItem news:datas){
            addNews(news);
        }
    }

    public void deleteAllNews(){
        db.delete(DbHelper.DB_NEWS_NAME,null,null);
    }

    public void deleteNews(int newsType){
        db.delete(DbHelper.DB_NEWS_NAME,"newstype = ?",new String[]{String.valueOf(newsType)});
    }

    public List<NewsItem> getNewsList(int newsType){
        Cursor cursor = null;
        List<NewsItem> list = new ArrayList<NewsItem>();
        NewsItem newsItem = null;
        String title;
        String link;
        String date;
        int newstype;
        try {
            cursor = db.query(DbHelper.DB_NEWS_NAME,null,"newstype = ?",new String[]{String.valueOf(newsType)},null,null,null);
            while (cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex("title"));
                link = cursor.getString(cursor.getColumnIndex("link"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                newstype = cursor.getInt(cursor.getColumnIndex("newstype"));
                newsItem = new NewsItem();
                newsItem.setTitle(title);
                newsItem.setLink(link);
                newsItem.setDate(date);
                newsItem.setNewsType(newstype);
                list.add(newsItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }

        }
        return list;
    }



    //Book

    public void addBook(Book book){
        if(book != null){
            ContentValues cv = new ContentValues();
            cv.put("title",book.getTitle());
            cv.put("url",book.getUrl());
            cv.put("end_date",book.getEnd());
            db.insert(DbHelper.DB_BOOK_NAME, null, cv);
        }
    }

    public void addBookList(List<Book> books){
        for(Book book:books){
            addBook(book);
        }
    }

    public void deleteAllBook(){
        db.delete(DbHelper.DB_BOOK_NAME,null,null);
    }


    public List<Book> getBookList(){
        Cursor cursor = null;
        List<Book> list = new ArrayList<Book>();
        Book book = null;
        String title;
        String url;
        long end;
        try {
            cursor = db.query(DbHelper.DB_BOOK_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex("title"));
                url = cursor.getString(cursor.getColumnIndex("url"));
                end = cursor.getLong(cursor.getColumnIndex("end_date"));
                book = new Book();
                book.setTitle(title);
                book.setUrl(url);
                book.setEnd(end);
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return list;
    }

    //Grade
    public void addGrade(Grade grade){
        if(grade != null){
            ContentValues cv = new ContentValues();
            cv.put("term", grade.getTerm());
            cv.put("school_year", grade.getSchool_year());
            cv.put("title", grade.getTitle());
            cv.put("credit", grade.getCredit());
            cv.put("normal_grade", grade.getNormal_grade());
            cv.put("bg",grade.getBg());
            cv.put("final_grade", grade.getFinal_grade());
            db.insert(DbHelper.DB_GRADE_NAME, null, cv);
        }
    }


    public void addGrades(List<Grade> gradeList){
        for(Grade grade: gradeList){
            addGrade(grade);
        }
    }

    public void deleteAllGrade(){
        Log.d("hihi1996", "delete");
        db.delete(DbHelper.DB_GRADE_NAME,null,null);
    }


    public List<Grade> getGradeList(){
        Grade grade = new Grade();
        List<Grade> list = new ArrayList<Grade>();
        Cursor cursor = null;
        String term;
        String school_year;
        String title;
        String credit;
        String normal_grade;
        String final_grade;
        int bg;
        try {
            cursor = db.query(DbHelper.DB_GRADE_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                term = cursor.getString(cursor.getColumnIndex("term"));
                school_year = cursor.getString(cursor.getColumnIndex("school_year"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                credit = cursor.getString(cursor.getColumnIndex("credit"));
                normal_grade = cursor.getString(cursor.getColumnIndex("normal_grade"));
                final_grade = cursor.getString(cursor.getColumnIndex("final_grade"));
                bg = cursor.getInt(cursor.getColumnIndex("bg"));
                grade = new Grade();
                grade.setTerm(term);
                grade.setBg(bg);
                grade.setSchool_year(school_year);
                grade.setTitle(title);
                grade.setCredit(credit);
                grade.setNormal_grade(normal_grade);
                grade.setFinal_grade(final_grade);
                list.add(grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor !=null){
                cursor.close();
            }
        }
        return list;
    }


    //Schedules
    public void addCourse(Course course){
        if(course != null){
            ContentValues cv = new ContentValues();
            cv.put("day_of_week",course.getDay_of_week());
            cv.put("start_week",course.getStart_week());
            cv.put("end_week",course.getEnd_week());
            cv.put("end_week",course.getEnd_week());
            cv.put("start",course.getStart());
            cv.put("end",course.getEnd());
            cv.put("title",course.getTitle());
            cv.put("teacher",course.getTeacher());
            cv.put("classes",course.getClasses());
            cv.put("bg_res_id",course.getBgResIndex());
            db.insert(DbHelper.DB_SCHEDULES_NAME, null, cv);
        }
    }


    public void addCourses(List<Course> courseList){
        for(Course course:courseList){
            addCourse(course);
        }
    }

    public void deleteAllCourse(){
        db.delete(DbHelper.DB_SCHEDULES_NAME,null,null);
    }


    public List<Course> getCourseList(){
        Cursor cursor = null;
        List<Course> list = new ArrayList<Course>();
        Course course = null;
        String title;
        String teacher;
        String classes;
        int bg_res_id;
        int day_of_week;
        int start_week;
        int end_week;
        int start;
        int end;
        try {
            cursor = db.query(DbHelper.DB_SCHEDULES_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex("title"));
                teacher = cursor.getString(cursor.getColumnIndex("teacher"));
                classes = cursor.getString(cursor.getColumnIndex("classes"));
                day_of_week = cursor.getInt(cursor.getColumnIndex("day_of_week"));
                start_week = cursor.getInt(cursor.getColumnIndex("start_week"));
                end_week = cursor.getInt(cursor.getColumnIndex("end_week"));
                start = cursor.getInt(cursor.getColumnIndex("start"));
                end = cursor.getInt(cursor.getColumnIndex("end"));
                bg_res_id = cursor.getInt(cursor.getColumnIndex("bg_res_id"));
                course = new Course();
                course.setTitle(title);
                course.setDay_of_week(day_of_week);
                course.setTeacher(teacher);
                course.setClasses(classes);
                course.setStart_week(start_week);
                course.setEnd_week(end_week);
                course.setStart(start);
                course.setEnd(end);
                course.setBgResIndex(bg_res_id);
                list.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return list;
    }
}
