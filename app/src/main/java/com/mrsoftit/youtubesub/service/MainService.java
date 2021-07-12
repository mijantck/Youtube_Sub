package com.mrsoftit.youtubesub.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mrsoftit.youtubesub.R;


public class MainService extends Service implements View.OnTouchListener{

    private static final String TAG = "ttttttttttttt";
    String transferData;
    int countDwon ;
    int countDwon1 ;




    private WindowManager windowManager;

    float dX, dY;


    private View floatyView;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        transferData = intent.getStringExtra("count");
        countDwon =Integer.parseInt(transferData);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        addOverlayView();
    }

    private void addOverlayView() {

        final WindowManager.LayoutParams params;
        final WindowManager.LayoutParams params1;
        int layoutParamsType;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           // layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ;

            Toast.makeText(this, "8888", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "777777", Toast.LENGTH_SHORT).show();

            //  layoutParamsType = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParamsType = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutParamsType,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT );
        params1 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutParamsType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT );


        params.gravity = Gravity.BOTTOM | Gravity.START;
        params.x = 0;
        params.y = 0;

        params1.gravity = Gravity.BOTTOM | Gravity.START;
        params1.x = 0;
        params1.y = 0;


        FrameLayout interceptorLayout = new FrameLayout(this) {

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    // Check if the HOME button is pressed
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        Log.v(TAG, "BACK Button Pressed");
                        onDestroy();
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.mrsoftit.vioplus");
                        startActivity( launchIntent );
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        return true;
                    }

                }
                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event);
            }
        };
        LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        if (inflater != null) {
            floatyView = inflater.inflate(R.layout.floating_view, interceptorLayout);

            Button butttttttt = floatyView.findViewById(R.id.butttttttt);

            butttttttt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainService.this, "click", Toast.LENGTH_SHORT).show();
                }
            });
            windowManager.addView(floatyView, params);
            new CountDownTimer(2 * 1000 + 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }public void onFinish() {
                    windowManager.updateViewLayout(floatyView,params1);
                    new CountDownTimer(countDwon * 1000 + 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            int hours = seconds / (60 * 60);
                            int tempMint = (seconds - (hours * 60 * 60));
                            int minutes = tempMint / 60;
                            seconds = tempMint - (minutes * 60);

                            String ss = "TIME : " + String.format("%02d", hours)
                                    + ":" + String.format("%02d", minutes)
                                    + ":" + String.format("%02d", seconds);
                            butttttttt.setText(ss+"");

                        }public void onFinish() {
                            windowManager.updateViewLayout(floatyView,params1);

                            onFinish();
                            //  butttttttt.setVisibility(View.GONE);


                        }
                    }.start();

                }
            }.start();
            floatyView.setOnTouchListener(this);




        }
        else {
            Log.e("SAW-example", "Layout Inflater Service is null; can't inflate and display R.layout.floating_view");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (floatyView != null) {

            windowManager.removeView(floatyView);

            floatyView = null;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
       view.performClick();

        Log.v(TAG, "onTouch...");

        Toast.makeText(this, "cloo", Toast.LENGTH_SHORT).show();
        // Kill service
       // onDestroy();

        return true;

    }

}
