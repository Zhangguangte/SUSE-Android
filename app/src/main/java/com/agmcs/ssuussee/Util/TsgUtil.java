package com.agmcs.ssuussee.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agmcs on 2015/5/20.
 */
public class TsgUtil {

    private String username;
    private String password;
    private HttpClient client = null;

    private String VIEWSTATE = null;
    private String VIEWSTATEGENERATOR = null;
    private String EVENTVALIDATION = null;

    public TsgUtil(String username, String password) {
        this.username = username;
        this.password = password;
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000); //设置连接超时
        HttpConnectionParams.setSoTimeout(params, 10000); //设置请求超时
        client = new DefaultHttpClient(params);
    }

    //    获得VIEWSTATE等信息
    public void getRequestInfo(){
        StringBuilder sb = new StringBuilder();
        try {
            HttpGet httpgets = new HttpGet("http://61.139.105.141:8080/ReadTable.aspx");
            HttpResponse response = client.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instreams));
                String line = null;
                sb = new StringBuilder();
                while ((line = br.readLine())!=null){
                    sb.append(line);
                }
                httpgets.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        Document doc = Jsoup.parse(sb.toString());
        this.VIEWSTATE = doc.getElementById("__VIEWSTATE").attr("value").replace(" ","");
        this.VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").attr("value").replace(" ", "");
        this.EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").attr("value").replace(" ", "");
    }

    public String login(){
        StringBuilder sb = new StringBuilder();
        try {
            if(VIEWSTATE == null || VIEWSTATEGENERATOR == null || EVENTVALIDATION == null){
                getRequestInfo();
            }
            HttpPost post = new HttpPost("http://61.139.105.141:8080/login.aspx");
            post.setHeader("Accept","text/html,application/xhtml+xml,*/*");
            post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
            post.setHeader("Connection","keep-alive");
            post.setHeader("Content-Type","application/x-www-form-urlencoded");
            post.setHeader("Origin","http://61.139.105.141:8080");
            post.setHeader("Referer","http://61.139.105.141:8080/login.aspx");
            post.setHeader("Host","61.139.105.141:8080");
            post.setHeader("Accept-Language","zh-CN");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("__VIEWSTATE",VIEWSTATE));
            pairs.add(new BasicNameValuePair("__EVENTVALIDATION",EVENTVALIDATION));
            pairs.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", VIEWSTATEGENERATOR));
            pairs.add(new BasicNameValuePair("TxtUserName", username));
            pairs.add(new BasicNameValuePair("TxtPassWord", password));
            pairs.add(new BasicNameValuePair("ImageButton2.x", "53"));
            pairs.add(new BasicNameValuePair("ImageButton2.y","8"));
            post.setEntity(new UrlEncodedFormEntity(pairs,"utf-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instreams));
                String line = null;
                try {
                    while ((line = br.readLine())!=null){
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                post.abort();
            }
            if(sb.toString().contains("用户密码错误") || sb.toString().contains("用户名不存在")){
                return null;
            }else{
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean reNew(String url){
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine())!=null){
                    sb.append(line);
                }
                String text = sb.toString();
                if(text.indexOf("续借成功")!= -1){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getBookInfo(){
        return login();
    }
}
