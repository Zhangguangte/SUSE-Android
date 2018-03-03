package com.agmcs.ssuussee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.agmcs.ssuussee.R;
import com.umeng.analytics.MobclickAgent;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout splash = (RelativeLayout)findViewById(R.id.splash);
        Animation alphaAnimation = new AlphaAnimation(0.2f,1.0f);
        alphaAnimation.setDuration(2000);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                JWGLUtils jwglUtils = new JWGLUtils();
//                jwglUtils.login("13101010412", "nishengri7");
//                jwglUtils.queryCurSchedules();
//
//            }
//        }).start();
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash.startAnimation(alphaAnimation);
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
