package com.example.maddi.logme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by maddi on 4/20/2016.
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public static float stepMax = 0f;
    public static float calorieMax = 0f;
    ArrayList<Integer> activityImageList;
    ArrayList<String> activityList;
    ImageView status_image;
    TextView status_text;


    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Random rand = new Random();

            int n = rand.nextInt(5);

            status_image.setImageResource(activityImageList.get(n));
            status_text.setText(activityList.get(n));

            mHandler.postDelayed(this, 2000);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mHandler.post(runnable);

        status_image = (ImageView) findViewById(R.id.status_imageview);
        status_text = (TextView) findViewById(R.id.status_text);


        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        pulse.setRepeatCount(Animation.INFINITE);
        status_image.startAnimation(pulse);

        activityImageList = new ArrayList<>();
        activityList = new ArrayList<>();

        activityImageList.add(R.drawable.walk);
        activityImageList.add(R.drawable.run);
        activityImageList.add(R.drawable.climbing);
        activityImageList.add(R.drawable.sitting);
        activityImageList.add(R.drawable.sleep);

        activityList.add("Walking");
        activityList.add("Running");
        activityList.add("Climbing");
        activityList.add("Sitting");
        activityList.add("Sleeping");



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



        // Setting Steps and Calories
        stepMax = SetGoalActivity.mSeries;
        if (stepMax == 0) {
            //stepMax = ;
        }
        calorieMax = SetGoalActivity.mSeries1;
        Log.d("SetGoal mseries", String.valueOf(SetGoalActivity.mSeries));
        if (calorieMax == 0) {
            //calorieMax = ;
        }


        // Animation
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, 180);
        translation.setStartOffset(100);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());

        TranslateAnimation translation1;
        translation1 = new TranslateAnimation(0f, 0F, 0f, 220);
        translation1.setStartOffset(100);
        translation1.setDuration(2000);
        translation1.setFillAfter(true);
        translation1.setInterpolator(new BounceInterpolator());




        // On Click Listeners for Activities
        final ImageView food_summary = (ImageView) findViewById(R.id.step_counter);
        food_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StepCounterActivity.class);
                startActivity(intent);
            }
        });


        final ImageView stair_counter = (ImageView) findViewById(R.id.stair_counter);
        stair_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClimbCounterActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

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
