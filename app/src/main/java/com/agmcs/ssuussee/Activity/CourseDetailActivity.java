package com.agmcs.ssuussee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.agmcs.ssuussee.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseDetailActivity extends AppCompatActivity {
    @InjectView(R.id.course_detail_toobar)
    Toolbar toolbar;
    @InjectView(R.id.course_classes)
    TextView classesTv;

    @InjectView(R.id.course_teacher)
    TextView teacherTv;

    @InjectView(R.id.course_jie)
    TextView jieTv;

    @InjectView(R.id.course_week)
    TextView zouTv;

    @InjectView(R.id.course_title)
    TextView titleTv;

    public static final String TITLE = "title";
    public static final String TEACHER = "teacher";
    public static final String CLASSES = "classes";
    public static final String JIESU = "jiesu";
    public static final String ZOUSU = "zousu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("");
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = "";
        String teacher = "";
        String classes = "";
        String jiesu = "";
        String zousu = "";


        Intent i = getIntent();
        if(i != null){
            title = i.getStringExtra(TITLE);
            teacher = i.getStringExtra(TEACHER);
            classes = i.getStringExtra(CLASSES);
            jiesu = i.getStringExtra(JIESU);
            zousu = i.getStringExtra(ZOUSU);
            titleTv.setText(title);
            teacherTv.setText(teacher);
            classesTv.setText(classes);
            jieTv.setText(jiesu);
            zouTv.setText(zousu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
