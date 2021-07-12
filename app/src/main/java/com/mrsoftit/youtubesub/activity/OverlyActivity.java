package com.mrsoftit.youtubesub.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mrsoftit.youtubesub.service.MainService;


public class OverlyActivity extends AppCompatActivity {

    String url ;
    String count ;

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            count = extras.getString("count");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Launch service right away - the user has already previously granted permission
                    launchMainService(url,count);
                }
                else {

                    // Check that the user has granted permission, and prompt them if not
                    checkDrawOverlayPermission();
                }
            }

            //The key argument here must match that used in the other activity
        }

    }

    private void launchMainService(String url,String count) {

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(url)) );

       /* try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
        }*/

        Intent svc = new Intent(this, MainService.class);
        svc.putExtra("count",count);
        stopService(svc);
        startService(svc);

        finish();
    }

    private final static int REQUEST_CODE = 10101;

    private void checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                // If not, form up an Intent to launch the permission request
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                // Launch Intent, with the supplied request code
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {

            // Double-check that the user granted it, and didn't just dismiss the request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {

                    // Launch the service
                    launchMainService(url,count);
                }
                else {

                    Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}