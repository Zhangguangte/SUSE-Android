package com.agmcs.ssuussee.Util;

import android.util.Log;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.model.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by agmcs on 2015/5/23.
 */
public class SchedulesBiz {
    public static List<Course> getCourse(JWGLUtils jwglUtils){
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
        int color =0;
        List<Course> courses = new ArrayList<Course>();
        Course course;
//        jwglUtils.login(" ");
        String htmlStr = jwglUtils.queryCurSchedules();
        if(htmlStr == null){
            Log.d("hihi1996", "null");
            return null;
        }
        Log.d("hihi1996", htmlStr);
        Integer[] flags = new Integer[]{0,0,0,0,0,0,0};//当前行是否跳过
        int currentDay = 0;

        Document doc = Jsoup.parse(htmlStr);
        Element table = doc.getElementById("Table1").getElementsByTag("tbody").first();
        Elements trs = table.getElementsByTag("tr");
        Pattern p = Pattern.compile("第([0-9]*?)-([0-9]*?)周");
        for(int i=2;i<trs.size();i++){
            Element tr = trs.get(i);
            Elements tds = tr.getElementsByAttributeValue("align", "Center");
            currentDay = 0;
            for(int j=0; j<tds.size(); j++){
                Element td = tds.get(j);
                int rowSpan;
                if(td.attr("rowspan")!=""){
                    rowSpan = Integer.parseInt(td.attr("rowspan"));
                }else{
                    rowSpan = 1;
                }
                //find current day of week
                for(int index=currentDay; index<flags.length; index++){
                    if(flags[index]>0){
                        flags[index] -= 1;
                    }else{
                        currentDay = index;
                        break;
                    }
                }
                flags[currentDay] = rowSpan - 1;
                //find end;
//                System.out.println(td.text() + "这节课第几节" +(i-1) + " 到第几节" + (i + rowSpan-2));
                //todo course Biz
                String data = td.html();
                if(!data.contains("&nbsp;")){
                    String[] courseDatas = data.split("<br><br>");
                    for(String courseData :courseDatas) {
                        if (courseData.contains("<font")) {
                            continue;
                        }
                        String[] course_info = courseData.split("<br>");
//                        System.out.println(courseData  + " 这节课在星期" + (currentDay + 1) + "第几节" +(i-1) + " 到第几节" + (i + rowSpan-2));

                        course = new Course();
                        if (course_info.length == 5) {
                            Matcher matcher = p.matcher(course_info[2]);

                            course.setTitle(course_info[0]);
                            course.setTeacher(course_info[3]);
                            course.setClasses(course_info[4]);
                            course.setDay_of_week(currentDay);
                            course.setStart(i-1);
                            course.setEnd(i + rowSpan-2);
                            if(matcher.find()){
                                course.setStart_week(Integer.parseInt(matcher.group(1)));
                                course.setEnd_week(Integer.parseInt(matcher.group(2)));
                            }
//                            Log.d("hihi1994", course_info[0] + "  " + course_info[2] + "  " + course_info[3] + "  " + course_info[4]);
                        } else if (course_info.length == 4) {
                            Matcher matcher = p.matcher(course_info[1]);
                            course.setTitle(course_info[0]);
                            course.setTeacher(course_info[2]);
                            course.setClasses(course_info[3]);
                            course.setDay_of_week(currentDay);
                            course.setStart(i - 1);
                            course.setEnd(i + rowSpan-2);
                            if(matcher.find()){
                                course.setStart_week(Integer.parseInt(matcher.group(1)));
                                course.setEnd_week(Integer.parseInt(matcher.group(2)));
                            }
//                            Log.d("hihi1994",course_info[0] + "  " + course_info[1] + "  " + course_info[2] + "  " + course_info[3]);
                        }
                        course.setBgResIndex(color % 14);
                        color++;
                        //合并连堂的课程
                        for(Course c :courses){
                            if (c.getTitle().equals(course.getTitle())){
                                course.setBgResIndex(c.getBgResIndex());
                                color--;
                                //同一天的同一堂课
                                if(c.getDay_of_week() == course.getDay_of_week() && c.getClasses().equals(course.getClasses())){
                                    if(c.getStart_week() == course.getStart_week() && c.getEnd_week() == c.getEnd_week()){
                                        if(c.getEnd()+1 == course.getStart()){
                                            course.setStart(c.getStart());
                                            courses.remove(c);
                                        }else if(c.getStart()+1 == course.getEnd()){
                                            course.setEnd(c.getEnd());
                                            courses.remove(c);
                                        }else if(c.getStart() == course.getStart() && c.getEnd() == course.getEnd()){
                                            courses.remove(c);
                                        }
                                    }
                                }
                                break;
                            }
                        }

                        courses.add(course);
                    }
                }
                currentDay += 1;
            }
        }
        return courses;
    }


}
