package com.agmcs.ssuussee.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.agmcs.ssuussee.CommonException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by agmcs on 2015/5/20.
 */

public class JWGLUtils {
    private static JWGLUtils ourInstance = null;

    private static final String login_url = "http://61.139.105.138/default_ysdx.aspx";
    private HttpClient client;
    private String username;
    private String password;
    private String name;

    private String VIEWSTATE = null;

    private String GRADE_VIEWSTATE =null;

    public static JWGLUtils getInstance() {
        if(ourInstance == null){
            ourInstance = new JWGLUtils();
        }
        return ourInstance;
    }

    public void reSetInfo(String username,String password){
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000); //设置连接超时
        HttpConnectionParams.setSoTimeout(params, 10000); //设置请求超时
        this.client = new DefaultHttpClient(params);
        this.client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
        this.username = username;
        this.password = password;
        this.name = null;
        this.VIEWSTATE = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private JWGLUtils() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000); //设置连接超时
        HttpConnectionParams.setSoTimeout(params, 10000); //设置请求超时
        client = new DefaultHttpClient(params);
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
        this.username = null;
        this.password = null;
        this.name = null;
        this.VIEWSTATE = null;
    }

    public Bitmap getImgBitmap(){
        InputStream instream;
        HttpGet get = new HttpGet("http://61.139.105.138/CheckCode.aspx");
        try {
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    return BitmapFactory.decodeStream(instream);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("hihi1996", e.toString());
        }finally {
            get.abort();
        }
        return null;
    }

    public void getRequestInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            HttpGet get = new HttpGet("http://61.139.105.138");
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String line = null;
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gbk"));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            }else{
                try {
                    throw new CommonException("链接失败");
                } catch (CommonException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            try {
                throw new CommonException("链接失败");
            } catch (CommonException e1) {
                e1.printStackTrace();
            }
        }
        Document doc = Jsoup.parse(sb.toString());
        if(doc != null){
            this.VIEWSTATE = doc.getElementsByTag("input").first().attr("value").replace(" ","");
        }
    }

    public boolean login(String text){
        StringBuilder sb = new StringBuilder();
        try {
            getRequestInfo();
            HttpPost post = new HttpPost("http://61.139.105.138/default2.aspx");
            post.setHeader("Accept","text/html, application/xhtml+xml, */*");
            post.setHeader("Host", "61.139.105.138");
            post.setHeader("Referer", "http://61.139.105.138/default_ysdx.aspx");
            post.setHeader("Accept-Encoding", "gzip, deflate");
            post.setHeader("Connection", "Keep-Alive");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("__VIEWSTATE",VIEWSTATE));
            pairs.add(new BasicNameValuePair("txtUserName",username));
            pairs.add(new BasicNameValuePair("TextBox2",password));
            pairs.add(new BasicNameValuePair("txtSecretCode",text));
            pairs.add(new BasicNameValuePair("RadioButtonList1","%D1%A7%C9%FA"));
            pairs.add(new BasicNameValuePair("Button1",""));
            pairs.add(new BasicNameValuePair("lbLanguage",""));
            pairs.add(new BasicNameValuePair("hidPdrs",""));
            pairs.add(new BasicNameValuePair("hidsc",""));
            post.setEntity(new UrlEncodedFormEntity(pairs,"utf-8"));
            HttpResponse response = client.execute(post);


            if(response.getStatusLine().getStatusCode() == 200){
                HttpGet get = new HttpGet("http://61.139.105.138/xs_main.aspx?xh=" + username);
                response =  client.execute(get);
                HttpEntity entity = response.getEntity();
                if(entity != null){
                    String line = null;
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gbk"));
                    while ((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    post.abort();
                    if(sb.toString().contains("language='javascript'>alert")){
                        return false;
                    }else{
                        Pattern p = Pattern.compile("<span id=\"xhxm\">(.+?)同学</span>");
                        Matcher matcher = p.matcher(sb.toString());
                        if(matcher.find()){
                            name = matcher.group(1);
                            name = URLEncoder.encode(name,"utf-8");
                        }else{
                            return false;
                        }
                        return true;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hihi1996", e.toString());
        }
        return false;
    }

    public String queryCurSchedules(){
        StringBuilder sb = new StringBuilder();
        try {
            HttpGet get = new HttpGet("http://61.139.105.138/xskbcx.aspx?xh=" + username + "&xm=" + name + "&gnmkdm=N121603");
            get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
            get.addHeader("Referer", "http://61.139.105.138/xs_main.aspx?xh=" + username);
            get.addHeader("Accept-Language", "zh-CN");
            get.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
            get.addHeader("Accept-Encoding", "gzip");
            get.addHeader("Host", "61.139.105.138");
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gbk"));
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            get.abort();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hihi1996", e.toString());
        }
        return null;
    }


    public String queryCurGeadeAndSaveViewstate(){
        StringBuilder sb = new StringBuilder();
        try {
            HttpGet get = new HttpGet("http://61.139.105.138/xscj_gc.aspx?xh=" + username + "&xm=" + name + "&gnmkdm=N121605");
            get.addHeader("Accept","text/html, application/xhtml+xml, */*");
            get.addHeader("Referer","http://61.139.105.138/xs_main.aspx?xh=" + username);
            get.addHeader("Accept-Language","zh-CN");
            get.addHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            get.addHeader("Accept-Encoding","gzip, deflate");
            get.addHeader("Host","61.139.105.138");
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gbk"));
            String line = null;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Log.d("hihi1996",response.getStatusLine().getStatusCode() + "##" + sb.toString());
            //解析VIEWSTATE并保存
            Document doc = Jsoup.parse(sb.toString());
            if(doc != null){
                this.GRADE_VIEWSTATE = doc.getElementsByTag("input").get(0).attr("value").replace(" ","");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String query_grade(String xn,int xq){
        StringBuilder sb =new StringBuilder();
        try{
            HttpPost post = new HttpPost("http://61.139.105.138/xscj_gc.aspx?xh=" + username +"&xm=" + name + "&gnmkdm=N121605");
            System.out.println("http://61.139.105.138/xscjcx_dq.aspx?xh=" + username +"&xm=" + name + "&gnmkdm=N121605");
            post.addHeader("Accept","text/html, application/xhtml+xml, */*");
            post.addHeader("Referer","http://61.139.105.138/xscjcx_dq.aspx?xh=" + username +"&xm=" + name + "&gnmkdm=N121605");
            post.addHeader("Accept-Language","zh-CN");
            post.addHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            post.addHeader("Host","61.139.105.138");
            post.addHeader("Accept-Encoding","gzip");

            List<NameValuePair> pairs= new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("__EVENTTARGET",""));
            pairs.add(new BasicNameValuePair("__EVENTARGUMENT",""));
            pairs.add(new BasicNameValuePair("__VIEWSTATE",GRADE_VIEWSTATE));
            pairs.add(new BasicNameValuePair("ddlxn",xn));
            pairs.add(new BasicNameValuePair("ddlxq", String.valueOf(xq)));
            pairs.add(new BasicNameValuePair("btnCx","+%B2%E9++%D1%AF+"));

            post.setEntity(new UrlEncodedFormEntity(pairs,"utf-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"gbk"));
            String line = null;

            while((line = reader.readLine())!=null){
                sb.append(line);
            }

            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }



}
