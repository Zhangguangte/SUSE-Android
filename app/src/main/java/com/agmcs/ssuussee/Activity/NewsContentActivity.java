package com.agmcs.ssuussee.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.agmcs.ssuussee.Adapter.NewsContentDataAdapter;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NewsItemBiz;
import com.agmcs.ssuussee.model.Newsdata;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class NewsContentActivity extends AppCompatActivity {

    @InjectView(R.id.toolBar_content)
    Toolbar toolbar;
    @InjectView(R.id.content_recyclerview)
    RecyclerView content_view;
    @InjectView(R.id.web)
    WebView web;
    @InjectView(R.id.progress)
    ProgressBar progressBar;


    public static final String URL_DATA = "url_data";
    public static final String TITLE = "title";
    String url_data;

    private List<Newsdata> news_llist = new ArrayList<Newsdata>();
    private NewsItemBiz newsItemBiz = new NewsItemBiz();
    private NewsContentDataAdapter adapter;
    private String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url_data = getIntent().getStringExtra(URL_DATA);
        String title = getIntent().getStringExtra(TITLE);
        setTitle(title);
        adapter = new NewsContentDataAdapter(news_llist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NewsContentActivity.this,LinearLayoutManager.VERTICAL,false);
        content_view.setLayoutManager(layoutManager);
        content_view.setAdapter(adapter);

        new LoadDataTasks().execute(url_data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url_data);
            intent.setData(content_url);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class LoadDataTasks extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            Log.d("hihi123", "加载文章" + url);
            try {
//                NewsPage page = newsItemBiz.getNews(url);
//                news_llist = page.getNews();
                html = newsItemBiz.getNewsHtml(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            adapter.reAdd(news_llist);
//            adapter.notifyDataSetChanged();
            WebSettings settings = web.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDefaultFontSize(19);
//            settings.setJavaScriptEnabled(true);
            web.setVerticalScrollBarEnabled(false);
            web.setHorizontalScrollBarEnabled(false);
            web.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            web.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
                        web.goBack(); //goBack()表示返回WebView的上一页面
                        return true;
                    }
                    return false;
                }
            });
//            web.loadData(html, "text/html", "utf-8");
            web.loadDataWithBaseURL(null, html, "text/html","UTF-8", null);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}


