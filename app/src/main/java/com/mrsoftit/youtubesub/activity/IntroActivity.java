package com.mrsoftit.youtubesub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.mrsoftit.youtubesub.R;

import static com.mrsoftit.youtubesub.activity.SplashActivity.MY_PREFS_NAME;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Videos View", "Video View  from anywhere and\n" +
                        "anytime learn at your own pace.",
                R.drawable.into_one, ContextCompat.getColor(getApplicationContext(), R.color.intro_color)));

        // below line is for creating second slide.
        addSlide(AppIntroFragment.newInstance("Earn money ", "Get money  based on your \n" +
                        "coin collection ",
                R.drawable.intro_two, ContextCompat.getColor(getApplicationContext(), R.color.intro_color)));

        // below line is use to create third slide.
        addSlide(AppIntroFragment.newInstance("Like Video  ", "Take the best online youtube\n" +
                        " like promotion app.",
                R.drawable.intro_tre, ContextCompat.getColor(getApplicationContext(), R.color.intro_color)));

        addSlide(AppIntroFragment.newInstance("Subscriber", "subscribe from trustet  subscriber",
                R.drawable.intreo_four, ContextCompat.getColor(getApplicationContext(), R.color.intro_color)));
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("intro", "ok");
        editor.apply();
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("intro", "ok");
        editor.apply();
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
    }
}