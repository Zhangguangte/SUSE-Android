package com.agmcs.ssuussee.Util;

import com.agmcs.ssuussee.model.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by agmcs on 2015/5/21.
 */
public class TsgBookBiz {

    public List<Book> getBookList(TsgUtil tsgUtil){
        Calendar calendar = Calendar.getInstance();
        List<Book> book_list = new ArrayList<Book>();
        String htmlStr = tsgUtil.getBookInfo();
        if(htmlStr == null){
            return book_list;
        }


        Book book;
        Document doc = Jsoup.parse(htmlStr);
        Element table = doc.getElementById("DataGrid1");
        if(table == null){
            return book_list;
        }
        Elements infos = table.getElementsByTag("tr");
        if(infos.size() > 1){
            for(int i=1;i<infos.size();i++){
                Element info = infos.get(i);
                Elements tds = info.getElementsByTag("td");
                String title = tds.get(1).text();
                book = new Book();
                book.setTitle(title);

                String data = tds.get(3).text();
                String[] data_info = data.split("-");
                calendar.set(Integer.parseInt(data_info[0]), Integer.parseInt(data_info[1])-1, Integer.parseInt(data_info[2]));
                book.setEnd(calendar.getTimeInMillis());


                String url = "http://61.139.105.141:8080/" + tds.get(4).getElementsByTag("a").attr("href");
                book.setUrl(url.replace(" ",""));
                book_list.add(book);
            }
        }
        return book_list;

    }
}
