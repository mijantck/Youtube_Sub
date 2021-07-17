package com.mrsoftit.youtubesub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.mrsoftit.youtubesub.R;

public class SplashActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN_TIME_OUT = 2000;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String inro = prefs.getString("intro", "No name defined");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (!inro.equals("ok")){
                    i = new Intent(SplashActivity.this, IntroActivity.class);
                }else {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }
                //Intent is used to switch from one activity to another.
                startActivity(i);
                //invoke the SecondActivity.
                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}