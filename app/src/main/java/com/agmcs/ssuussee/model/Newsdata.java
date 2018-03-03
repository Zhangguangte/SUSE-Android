package com.agmcs.ssuussee.model;

/**
 * Created by agmcs on 2015/5/19.
 */
public class Newsdata {
    private String title;
    private int news_type;
    private String content;
    private String ImageLink;
    public static interface Type
    {
        public static final int TITLE = 1;
        public static final int IMG = 2;
        public static final int CONTENT = 3;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNews_type() {
        return news_type;
    }

    public void setNews_type(int news_type) {
        this.news_type = news_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    @Override
    public String toString() {
        return "News [title=" + title + ", content=" + content + ", type=" + news_type  + ", src=" + ImageLink +  "]";
    }
}
