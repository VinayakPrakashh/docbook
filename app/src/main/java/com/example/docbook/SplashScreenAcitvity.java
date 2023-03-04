package com.example.docbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashScreenAcitvity extends AppointmentActivity{

    private static final int SPLASH_TIME_OUT = 2000; // 2 seconds
    private CountDownTimer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashTimer = new CountDownTimer(SPLASH_TIME_OUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // do nothing
            }

            @Override
            public void onFinish() {
                // Start the MainActivity and close the SplashActivity
                Intent intent = new Intent(SplashScreenAcitvity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        splashTimer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cancel the CountDownTimer to avoid memory leaks
        if (splashTimer != null) {
            splashTimer.cancel();
            splashTimer = null;
        }
    }
}
