package com.agmcs.ssuussee.Util;

import android.text.TextUtils;
import android.util.Log;

import com.agmcs.ssuussee.model.NewsItem;
import com.agmcs.ssuussee.model.NewsPage;
import com.agmcs.ssuussee.model.Newsdata;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agmcs on 2015/5/19.
 */
public class NewsItemBiz {
    public List<NewsItem> getNewsList(int news_type, int current_page){
        String urldata = UrlUtil.generateURL(news_type, current_page);
        Log.d("hihi123", urldata);

        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        String htmlStr = DataUtil.Httpget(urldata);
        if(htmlStr == null){
            return newsItems;
        }
        NewsItem newsItem = null;
        Document doc = Jsoup.parse(htmlStr);

        Element div = doc.getElementsByClass("news_list_2").first();
        Elements lis = div.getElementsByTag("li");

        Elements liss = doc.getElementsByClass("news_list_2")
                .first()
                .getElementsByTag("ul")
                .first()
                .getElementsByTag("li");
        for(int i=0; i<lis.size(); i++){
            Element li = lis.get(i);
            Element li_a_ele = li.child(0);
            String url = "http://www.suse.edu.cn/" + li_a_ele.attr("href");
            Element span = li_a_ele.child(0);
            String title = span.text();
            Element em = li_a_ele.child(1);
            String date = "";
            date = em.text();
            newsItem = new NewsItem();

            newsItem.setDate(date);
            newsItem.setTitle(title);
            newsItem.setLink(url);
            newsItem.setNewsType(news_type);
            newsItems.add(newsItem);
        }
        return newsItems;
    }


    public NewsPage getNews(String url_data){
        String htmlStr = DataUtil.Httpget(url_data);
        Log.d("hihi123", htmlStr);
        Newsdata newsdata = new Newsdata();
        List<Newsdata> news_list = new ArrayList<Newsdata>();
        Document doc = Jsoup.parse(htmlStr);
        Element title_div = doc.getElementsByClass("newsTit").first();
        String title = title_div.text();
        Element page_div = doc.getElementsByClass("showus").first();


        Elements contents = page_div.children();
        for(int i=0;i<contents.size();i++){
            Element element = contents.get(i);


            if(element.tagName() == "p"){
                Elements imgs = element.getElementsByTag("img");
                if(imgs.size()>0){

                    for(Element img:imgs){
                        String src = img.attr("src");
                        if(src != ""){
                            newsdata = new Newsdata();
                            newsdata.setNews_type(Newsdata.Type.IMG);
                            newsdata.setImageLink(src);
                            news_list.add(newsdata);
                        }
                        imgs.remove();
                    }
                }
                String content = element.text();
                if(!TextUtils.isEmpty(content.replace(" ", ""))){
                    newsdata = new Newsdata();
                    newsdata.setNews_type(Newsdata.Type.CONTENT);
                    newsdata.setContent(content);
                    news_list.add(newsdata);
                }
            }else if(element.tagName() == "img"){
                String src = element.attr("src");
                if(src != ""){
                    newsdata = new Newsdata();
                    newsdata.setNews_type(Newsdata.Type.IMG);
                    newsdata.setImageLink(src);
                    news_list.add(newsdata);
                }
            }else if(element.tagName() == "table"){
                String content = element.outerHtml();
                if(!TextUtils.isEmpty(content.replace(" ", ""))){
                    newsdata = new Newsdata();
                    newsdata.setNews_type(Newsdata.Type.CONTENT);
                    newsdata.setContent(content);
                    news_list.add(newsdata);
                }
            }
        }
        NewsPage newsPage = new NewsPage();
        newsPage.setNews(news_list);

        newsPage.setHtml(page_div.outerHtml());


        return newsPage;
    }

    public String getNewsHtml(String url){
        String htmlStr = DataUtil.Httpget(url);
        Document doc = Jsoup.parse(htmlStr);
        Element page_div = doc.getElementsByClass("showus").first();

        Elements srcs = page_div.getElementsByAttribute("src");
        for(Element each : srcs){
            String attr = each.attr("src");
            if(attr != "" && !attr.startsWith("http")){
                each.attr("src", "http://www.suse.edu.cn" + attr);
            }
            each.attr("style","width:100%;height:auto");
        }
        Elements hrefs = page_div.getElementsByAttribute("href");
        for(Element each : hrefs){

            String attr = each.attr("href");
            if(attr != "" && !attr.startsWith("http")){
                each.attr("href", "http://www.suse.edu.cn" + attr);
            }
//            each.removeAttr("target");
//            each.attr("style","width:100%;height:auto");
        }
        return page_div.outerHtml();
    }
}
