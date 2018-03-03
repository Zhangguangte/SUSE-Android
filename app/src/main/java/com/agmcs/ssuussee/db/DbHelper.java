package com.agmcs.ssuussee.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agmcs on 2015/5/20.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NEWS_NAME = "dasabi_db";
    public static final int DB_VERSION = 1;
    private static final String CREATE_NEWS_DB = "create table " + DB_NEWS_NAME +"(" +
            "id integer primary key autoincrement," +
            "title text," +
            "link text," +
            "date text," +
            "content text," +
            "newstype integer);";

    public static final String DB_BOOK_NAME = "book";
    private static final String CREATE_BOOK_DB = "create table " + DB_BOOK_NAME+"("+
            "id integer primary key autoincrement," +
            "title text," +
            "end_date long," +
            "url text);";

    public static final String DB_SCHEDULES_NAME = "schedules";
    private static final String CREATE_SCHEDULES_DB = "create table " + DB_SCHEDULES_NAME+"("+
            "id integer primary key autoincrement," +
            "day_of_week int," +
            "start_week int," +
            "end_week int," +
            "start int," +
            "end int," +
            "title text," +
            "teacher text," +
            "classes text," +
            "bg_res_id int);";

    public static final String DB_GRADE_NAME = "grades";
    private static final String CREATE_GRADE_DB = "create table " + DB_GRADE_NAME+"("+
            "id integer primary key autoincrement," +
            "term text," +
            "school_year text," +
            "title text," +
            "credit text," +
            "bg int," +
            "normal_grade text," +
            "final_grade text);";

    public DbHelper(Context context) {
        super(context, DB_NEWS_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_DB);
        db.execSQL(CREATE_BOOK_DB);
        db.execSQL(CREATE_SCHEDULES_DB);
        db.execSQL(CREATE_GRADE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
