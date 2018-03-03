package com.agmcs.ssuussee.model;

import java.util.List;

/**
 * Created by agmcs on 2015/5/19.
 */
public class NewsPage {
    private List<Newsdata> news;
    private String nextUrl;
    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<Newsdata> getNews() {
        return news;
    }

    public void setNews(List<Newsdata> news) {
        this.news = news;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
