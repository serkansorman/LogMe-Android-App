package com.example.maddi.logme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by maddi on 4/23/2016.
 */
public class AppIntroActivity extends AppIntro {


    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("LogMe", "Detect Your Activity", R.drawable.appintrorun, getResources().getColor(R.color.appintro1)));
        addSlide(AppIntroFragment.newInstance("LogMe", "See Your Current Pulse and Body Temperature", R.drawable.task, getResources().getColor(R.color.appintro3)));
        addSlide(AppIntroFragment.newInstance("LogMe", "Access Your Activity Statistics ", R.drawable.statistic, getResources().getColor(R.color.appintro4)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#F44336"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        Intent i = new Intent(AppIntroActivity.this, InfoActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        Intent i = new Intent(AppIntroActivity.this, InfoActivity.class);
        startActivity(i);
        finish();

        Toast.makeText(getApplicationContext(), "Welcome to Log-Me", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.

    }

}