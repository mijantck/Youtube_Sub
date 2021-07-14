package com.mrsoftit.youtubesub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.mrsoftit.youtubesub.R;
import com.mrsoftit.youtubesub.youtubefragment.AddFragment;
import com.mrsoftit.youtubesub.youtubefragment.LikeFragment;
import com.mrsoftit.youtubesub.youtubefragment.SubscriberFragment;
import com.mrsoftit.youtubesub.youtubefragment.ViewFragment;
import com.mrsoftit.youtubesub.youtubefragment.YoutubeCampaignFragment;
import com.zagori.bottomnavbar.BottomNavBar;

public class MainActivity extends AppCompatActivity {
    private BottomNavBar.OnBottomNavigationListener mOnBottomNavItemSelectedListener =
            new BottomNavBar.OnBottomNavigationListener() {
                Fragment selectedFragment = null;
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.youtube_view:
                            selectedFragment = new ViewFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();

                            Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.youtube_like:
                            selectedFragment = new LikeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            return true;
                        case R.id.youtube_add:

                            selectedFragment = new YoutubeCampaignFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            return true;
                        case R.id.youtube_coin:
                            selectedFragment = new AddFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            return true;
                        case R.id.youtube_sub:
                            selectedFragment = new SubscriberFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();


                            return true;
                    }

                    return false;
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        String showFragmenType = intent.getStringExtra("fragname");
        if (showFragmenType == null){
            showFragmenType="open";
        }
        BottomNavBar bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setBottomNavigationListener(mOnBottomNavItemSelectedListener);
       //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            if (showFragmenType.equals("like")){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LikeFragment()).commit();
            }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewFragment()).commit();
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        Log.d("sdfhfds", "onNewIntent: ");
        super.onNewIntent(intent);
    }
}