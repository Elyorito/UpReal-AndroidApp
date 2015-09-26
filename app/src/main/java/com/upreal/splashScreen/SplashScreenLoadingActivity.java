package com.upreal.splashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.upreal.R;
import com.upreal.home.HomeActivity;

/**
 * Created by Elyo on 01/03/2015.
 */
public class SplashScreenLoadingActivity extends Activity {

    private static int SPLASH_TIME = 1500;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashScreenLoadingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
