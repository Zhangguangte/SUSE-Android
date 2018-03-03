package com.agmcs.ssuussee.Util;

import com.agmcs.ssuussee.model.Grade;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by agmcs on 2015/5/27.
 */
public class GradeBiz {
    public static List<Grade> getGradeList(JWGLUtils jwglUtils,String xn, int xq){
        Random random = new Random();
        List<Grade> gradeList = new ArrayList<Grade>();
        Grade grade;
        String htmlstr = jwglUtils.query_grade(xn,xq);
        if(htmlstr == null){
            return gradeList;
        }
        Document doc = Jsoup.parse(htmlstr);
        Element table = doc.getElementById("DataGrid1");
        if(table == null){
            return gradeList;
        }
        Elements infos = table.getElementsByTag("tr");
        if(infos.size() > 1){
            for(int i=1;i<infos.size();i++){
                Element info = infos.get(i);
                Elements tds = info.getElementsByTag("td");
                grade = new Grade();
                String school_year = tds.get(0).text();
                String term = tds.get(1).text();
                String title = tds.get(3).text();
                String credit = tds.get(6).text();
                String normal_grade = tds.get(7).text();
                String final_grade = tds.get(11).text();
                System.out.println(title);
                grade.setSchool_year(school_year);
                grade.setTerm(term);
                grade.setTitle(title);
                grade.setCredit(credit);
                grade.setNormal_grade(normal_grade);
                grade.setFinal_grade(final_grade);
                grade.setBg(random.nextInt(7));
                gradeList.add(grade);
            }
            return gradeList;
        }else{
            return gradeList;
        }
    }
}
