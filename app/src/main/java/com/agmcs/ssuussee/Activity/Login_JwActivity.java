package com.agmcs.ssuussee.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.GradeBiz;
import com.agmcs.ssuussee.Util.JWGLUtils;
import com.agmcs.ssuussee.Util.SchedulesBiz;
import com.agmcs.ssuussee.db.Dao;
import com.agmcs.ssuussee.model.Course;
import com.agmcs.ssuussee.model.Grade;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Login_JwActivity extends AppCompatActivity {


    public final static int TYPE_SCHEDULES = 0;
    public final static int TYPE_GRADE = 1;

    public final static String TYPE_KEY = "type";



    public final static int WRONG_INFO = 0;
    public final static int READ_ERROR = 1;
    public final static int READ_SUCCESS = 2;

    private int type;
    @InjectView(R.id.login_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.login_dialog_yzm_img1)
    ImageView yzmImage;
    @InjectView(R.id.login_dialog_yzm1)
    EditText yzm_edit;
    @InjectView(R.id.login_dialog_password1)
    EditText passwordEt;
    @InjectView(R.id.login_student_num1)
    EditText studnet_num;

    @InjectView(R.id.submit_btn)
    Button btn;

    @InjectView(R.id.login_spinner_box)
    LinearLayout spinner_box;
    @InjectView(R.id.login_xn)
    Spinner xn_spinner;
    @InjectView(R.id.login_xq)
    Spinner xq_spinner;

    Bitmap bitmap;

    BaseApplication app;
    Dao dao;

    private JWGLUtils jwglUtils;
    private List<Course> list = new ArrayList<Course>();
    private List<Grade> gradeList = new ArrayList<Grade>();
    private String xn;
    private int xq;
    private ProgressDialog progressDialog;
    private ProgressDialog yzmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__jw);
        ButterKnife.inject(this);
        setTitle("教务管理");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        if(data == null){
            finish();
        }else{
            type = data.getIntExtra(TYPE_KEY,-1);
            if(type == -1){
                finish();
            }
        }

        if(type == TYPE_SCHEDULES){
            spinner_box.setVisibility(View.GONE);
        }else if(type == TYPE_GRADE){
            spinner_box.setVisibility(View.VISIBLE);
            xn_spinner.setAdapter(new ArrayAdapter<String>(Login_JwActivity.this,
                    android.R.layout.simple_list_item_1,
                    new String[]{"2011年", "2012年", "2013年", "2014年", "2015年"}));
            xq_spinner.setAdapter(new ArrayAdapter<String>(Login_JwActivity.this,
                    android.R.layout.simple_list_item_1,
                    new String[]{"上期", "下期"}));
            xn_spinner.setSelection(3);
            xq_spinner.setSelection(0);

            xq_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    xq = position + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            xn_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    xn = (2011 + position) + "-" + (2012 + position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



        jwglUtils = JWGLUtils.getInstance();
        app = (BaseApplication)getApplication();
        dao = Dao.getInstanse(this);

        //读取验证码
        new Load_Captcha().execute();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(studnet_num.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(passwordEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(yzm_edit.getWindowToken(), 0);

                String username = studnet_num.getText().toString();
                String password = passwordEt.getText().toString();
                String yzm = yzm_edit.getText().toString();
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(yzm)){
                    passwordEt.setText("");
                    yzm_edit.setText("");
                    
                    jwglUtils.setUsername(username);
                    jwglUtils.setPassword(password);
                    progressDialog = ProgressDialog.show(Login_JwActivity.this,"读取中..","...");
                    switch (type){
                        case TYPE_SCHEDULES:
                            new LoadSchedulesTask().execute(yzm);
                            break;
                        case TYPE_GRADE:
                            new LoadGradeTask().execute(yzm);
                            break;
                    }


                }
            }
        });
    }
    //从网络获取成绩
    class LoadGradeTask extends AsyncTask<String,Void,Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            if(!jwglUtils.login(params[0])){
                return WRONG_INFO;
            }
            jwglUtils.queryCurGeadeAndSaveViewstate();
            gradeList = GradeBiz.getGradeList(jwglUtils,xn,xq);
            dao.deleteAllGrade();
            dao.addGrades(gradeList);
            app.setGradeList(gradeList);
            return READ_SUCCESS;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result){
                case WRONG_INFO:
                    Toast.makeText(Login_JwActivity.this, "用户名或密码错误,请重试", Toast.LENGTH_SHORT).show();
                    new Load_Captcha().execute();
                    break;
                case READ_ERROR:
                    Toast.makeText(Login_JwActivity.this, "读取错误,请重试", Toast.LENGTH_SHORT).show();
                    new Load_Captcha().execute();
                    break;
                case READ_SUCCESS:
                    setResult(READ_SUCCESS);
                    finish();
                    break;
            }
        }
    }




    //从网络获取课程信息
    class LoadSchedulesTask extends AsyncTask<String,Void,Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            if(!jwglUtils.login(params[0])){
                return WRONG_INFO;
            }

            list = SchedulesBiz.getCourse(jwglUtils);

            if(list == null || list.size() == 0){
                return READ_ERROR;
            }

            dao.deleteAllCourse();
            dao.addCourses(list);
            app.setCourseList(list);
            return READ_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result){
                case WRONG_INFO:
                    Toast.makeText(Login_JwActivity.this, "用户名或密码错误,请重试", Toast.LENGTH_SHORT).show();
                    new Load_Captcha().execute();
                    break;
                case READ_ERROR:
                    Toast.makeText(Login_JwActivity.this, "读取错误,请重试", Toast.LENGTH_SHORT).show();
                    new Load_Captcha().execute();
                    break;
                case READ_SUCCESS:
                    setResult(READ_SUCCESS);
                    finish();
                    break;
            }
        }
    }


    class Load_Captcha extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            yzmDialog.dismiss();
            yzmImage.setImageBitmap(bitmap);
            btn.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            yzmDialog = ProgressDialog.show(Login_JwActivity.this,"正在获取验证码..","..");
        }

        @Override
        protected Void doInBackground(Void... params) {
            bitmap = jwglUtils.getImgBitmap();
            return null;
        }
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
