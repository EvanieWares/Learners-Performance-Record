package com.evanie.lprmaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    TextView appName;
    private static int splash_timeout = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.tvAppName);

        new Handler().postDelayed(() -> {
            Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(splashIntent);
            finish();
        },splash_timeout);
    }
}