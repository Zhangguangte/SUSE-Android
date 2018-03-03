package com.agmcs.ssuussee.model;

/**
 * Created by agmcs on 2015/5/23.
 */
public class Course {
    private int day_of_week;
    private int start_week;
    private int end_week;
    private int start;
    private int end;
    private String title;
    private String teacher;
    private String classes;
    private int bgResIndex;

    public int getBgResIndex() {
        return bgResIndex;
    }

    public void setBgResIndex(int bgResIndex) {
        this.bgResIndex = bgResIndex;
    }




    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
