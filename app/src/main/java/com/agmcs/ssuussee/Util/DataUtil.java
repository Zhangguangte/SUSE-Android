package com.agmcs.ssuussee.Util;



import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by agmcs on 2015/5/19.
 */
public class DataUtil {

    public static String Httpget(String url_data){
        StringBuffer sb = new StringBuffer();
        try{
            URL url = new URL(url_data);
            URLConnection conn = (URLConnection)url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("hihi123", e.toString());
        }
        return sb.toString();
    }


}
