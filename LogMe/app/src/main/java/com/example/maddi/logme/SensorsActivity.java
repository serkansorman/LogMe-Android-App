package com.example.maddi.logme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.natasa.progressviews.CircleProgressBar;

import java.util.Random;

/**
 * Created by maddi on 4/20/2016.
 */
public class SensorsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    float impulse;
    float acceleration;
    float temperature;

    private CircleProgressBar pulse_bar;
    private CircleProgressBar acce_bar;
    private CircleProgressBar temp_bar;

    float max_pulse = 150f;
    float max_acce = 100f;
    float max_temperature = 45f;
    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Random rand = new Random();
            int newPosition =  rand.nextInt((70) + 1) + 80;
            pulse_bar.setProgress((100 * (newPosition)) / max_pulse);
            pulse_bar.setText(String.valueOf(newPosition));

            newPosition = rand.nextInt(100);
            acce_bar.setProgress((100 * (newPosition)) / max_acce);
            acce_bar.setText(String.valueOf(newPosition));

            float randTemp =  35f + rand.nextFloat() * (5f);
            temp_bar.setProgress((randTemp * 100f) / max_temperature);
            temp_bar.setText(String.format("%.1f",randTemp));


            mHandler.postDelayed(this, 500);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_activity);

        mHandler.post(runnable);

        ImageView heart_image = (ImageView) findViewById(R.id.heart_imageview);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        pulse.setRepeatCount(Animation.INFINITE);
        heart_image.startAnimation(pulse);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        pulse_bar = (CircleProgressBar) findViewById(R.id.fats_progress);
        acce_bar = (CircleProgressBar) findViewById(R.id.carbs_progress);
        temp_bar = (CircleProgressBar) findViewById(R.id.protein_progress);


       /* acceleration = (float)51;//Food_MyRecyclerViewAdapter.totalcarbs;
        impulse = (float)87;//Food_MyRecyclerViewAdapter.totalfat;
        temperature = (float)36.4;//Food_MyRecyclerViewAdapter.totaltemp_bar;*/
        Log.d("Food Summary", String.valueOf(acceleration) + String.valueOf(impulse) + String.valueOf(temperature));

        // Animation
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, 180);
        translation.setStartOffset(100);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());

        TranslateAnimation translation1;
        translation1 = new TranslateAnimation(0f, 0F, 0f, 370);
        translation1.setStartOffset(100);
        translation1.setDuration(2000);
        translation1.setFillAfter(true);
        translation1.setInterpolator(new BounceInterpolator());


        pulse_bar.setWidthProgressBackground(40);
        pulse_bar.setWidthProgressBarLine(40);

        pulse_bar.setTextColor(Color.BLACK);
        pulse_bar.setTextSize(70);
        pulse_bar.setBackgroundColor(Color.LTGRAY);
        pulse_bar.setRoundEdgeProgress(true);
        pulse_bar.startAnimation(translation);


        acce_bar.startAnimation(translation);
        acce_bar.setWidthProgressBackground(40);
        acce_bar.setWidthProgressBarLine(40);

        acce_bar.setTextColor(Color.BLACK);
        acce_bar.setTextSize(70);
        acce_bar.setBackgroundColor(Color.LTGRAY);
        acce_bar.setRoundEdgeProgress(true);


        temp_bar.setWidthProgressBackground(40);
        temp_bar.setWidthProgressBarLine(40);

        temp_bar.setTextColor(Color.BLACK);
        temp_bar.setTextSize(70);
        temp_bar.setBackgroundColor(Color.LTGRAY);
        temp_bar.setRoundEdgeProgress(true);
        temp_bar.setAnimation(translation1);

    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item1:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.item2:
                intent = new Intent(this, SummaryActivity.class);
                startActivity(intent);
                break;
            case R.id.item3:
                intent = new Intent(this, SensorsActivity.class);
                startActivity(intent);
                break;
            case R.id.item4:
                intent = new Intent(this, BluetoothActivity.class);
                startActivity(intent);

            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
