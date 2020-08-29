package com.example.file.digitalfuelanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            new Thread() {
                public void run() {
                    int waited = 0;
                    while (waited < 2500) {
                        try {
                            Thread.sleep(100);
                            waited += 100;
                        }
                         catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                }
            }.start();
        }
}