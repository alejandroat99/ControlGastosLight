    package com.example.controlgastoslight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

    public class InitSplash extends AppCompatActivity {
    // Splash duration
    private final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load splash screen
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(InitSplash.this, MainActivity.class);
                startActivity(intent);
            };
        }, SPLASH_DURATION);
    }
}