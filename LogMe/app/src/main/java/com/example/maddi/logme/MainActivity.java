package com.example.maddi.logme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    MainApplication application;

    private ArrayList<Integer> activityImageList;
    private ArrayList<String> activityList;
    private ImageView status_image;
    private TextView status_text;

    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(application.currentActivity != null){
                status_image.setImageResource(activityImageList.get(application.currentActivity.ordinal()));
                status_text.setText(activityList.get(application.currentActivity.ordinal()));
                status_text.setTextColor(Color.parseColor("#FF69C254"));
            }
            else{
                status_image.setImageResource(android.R.color.transparent);
                status_text.setText("Waiting status...");
                status_text.setTextColor(Color.parseColor("#FFDA181B"));

            }

            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        application.startBt();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        application = (MainApplication) this.getApplication();

        mHandler.post(runnable);

        status_image = findViewById(R.id.status_imageview);
        status_text = findViewById(R.id.status_text);


        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        pulse.setRepeatCount(Animation.INFINITE);
        status_image.startAnimation(pulse);

        activityImageList = new ArrayList<>();
        activityList = new ArrayList<>();

        activityImageList.add(R.drawable.standing);
        activityImageList.add(R.drawable.run);
        activityImageList.add(R.drawable.walk);
        activityImageList.add(R.drawable.sitting);
        activityImageList.add(R.drawable.climbing);
        activityImageList.add(R.drawable.climbdown);

        activityList.add("Standing");
        activityList.add("Running");
        activityList.add("Walking");
        activityList.add("Sitting");
        activityList.add("Climbing Up");
        activityList.add("Climbing Down");



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        drawerLayout = findViewById(R.id.drawer);
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
        final ImageView food_summary = findViewById(R.id.step_counter);
        food_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StepCounterActivity.class);
                startActivity(intent);
            }
        });


        final ImageView stair_counter = findViewById(R.id.stair_counter);
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
