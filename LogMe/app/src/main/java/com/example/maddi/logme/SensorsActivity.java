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

    private CircleProgressBar fats;
    private CircleProgressBar carbs;
    private CircleProgressBar protein;

    float max_fat = 200f;
    float max_carbs = 100f;
    float max_protein = 45f;
    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Random rand = new Random();
            int newPosition = rand.nextInt(150);
            fats.setProgress((100 * (newPosition)) / max_fat);
            fats.setText(String.valueOf(newPosition));

            newPosition = rand.nextInt(100);
            carbs.setProgress((100 * (newPosition)) / max_carbs);
            carbs.setText(String.valueOf(newPosition));

            newPosition = rand.nextInt(45);
            protein.setProgress((100 * (newPosition)) / max_protein);
            protein.setText(String.valueOf(newPosition));


            mHandler.postDelayed(this, 1000);
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

        fats = (CircleProgressBar) findViewById(R.id.fats_progress);
        carbs = (CircleProgressBar) findViewById(R.id.carbs_progress);
        protein = (CircleProgressBar) findViewById(R.id.protein_progress);


       /* acceleration = (float)51;//Food_MyRecyclerViewAdapter.totalcarbs;
        impulse = (float)87;//Food_MyRecyclerViewAdapter.totalfat;
        temperature = (float)36.4;//Food_MyRecyclerViewAdapter.totalprotein;*/
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


        fats.setWidthProgressBackground(40);
        fats.setWidthProgressBarLine(40);

        fats.setTextColor(Color.BLACK);
        fats.setTextSize(70);
        fats.setBackgroundColor(Color.LTGRAY);
        fats.setRoundEdgeProgress(true);
        fats.startAnimation(translation);


        carbs.startAnimation(translation);
        carbs.setWidthProgressBackground(40);
        carbs.setWidthProgressBarLine(40);

        carbs.setTextColor(Color.BLACK);
        carbs.setTextSize(70);
        carbs.setBackgroundColor(Color.LTGRAY);
        carbs.setRoundEdgeProgress(true);


        protein.setWidthProgressBackground(40);
        protein.setWidthProgressBarLine(40);

        protein.setTextColor(Color.BLACK);
        protein.setTextSize(70);
        protein.setBackgroundColor(Color.LTGRAY);
        protein.setRoundEdgeProgress(true);
        protein.setAnimation(translation1);

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
