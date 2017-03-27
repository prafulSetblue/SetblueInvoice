package com.setblue.invoice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.setblue.invoice.utils.MySessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private MySessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        super.onCreate(savedInstanceState);
       //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {

            setContentView(R.layout.activity_splash);
            sessionManager = new MySessionManager(this);
            Timer t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {

                    if(sessionManager.getUserId().equals("")) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }, 1800);//3000

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
