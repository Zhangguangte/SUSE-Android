package com.agmcs.ssuussee.Util;

/**
 * Created by agmcs on 2015/5/19.
 */
public class UrlUtil {
    //新闻
    public static final String NEWS_LIST_URL = "http://www.suse.edu.cn/list-1-";
    //公告
    public static final String ANNOUNCEMENT_LIST_URL = "http://www.suse.edu.cn/list-2-";

    public static final String URL_END = ".aspx";


    public static String generateURL(int news_type,int current_page){

        String url = "";
        switch (news_type){
            case 0:
                url = NEWS_LIST_URL + current_page + URL_END;
                break;
            case 1:
                url = ANNOUNCEMENT_LIST_URL + current_page + URL_END;
                break;
        }
        return url;
    }
}
