package com.agmcs.ssuussee.model;

/**
 * Created by agmcs on 2015/5/27.
 */
public class Grade {
    String term;
    String school_year;
    String title;
    String credit;
    String normal_grade;
    String final_grade;
    int bg;

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSchool_year() {
        return school_year;
    }

    public void setSchool_year(String school_year) {
        this.school_year = school_year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getNormal_grade() {
        return normal_grade;
    }

    public void setNormal_grade(String normal_grade) {
        this.normal_grade = normal_grade;
    }

    public String getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(String final_grade) {
        this.final_grade = final_grade;
    }
}
