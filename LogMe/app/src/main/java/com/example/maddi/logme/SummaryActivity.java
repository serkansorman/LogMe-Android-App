package com.example.maddi.logme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maddi.logme.API.Models.ActivityCounter;
import com.example.maddi.logme.API.Response.ActivityCounterResponse;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SummaryActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener  {


    private DecoView mDecoView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final String[] days = { "Week","Saturday","Sunday","Monday", "Tuesday", "Wednesday", "Thursday","Friday"};
    private ArrayAdapter<String> dataAdapterForDays;


    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;

    private int walkCount = 0;
    private int runCount = 0;
    private int climbCount = 0;

    private float dailyRun = 0,dailyClimb = 0,dailyWalk = 0;

    private float mSeriesMax = 10000;
    List<ActivityCounter> activityCounters;

    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);

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

        mDecoView = findViewById(R.id.dynamicArcView);
        makeCall();




        Spinner spin =  findViewById(R.id.spinner);

        dataAdapterForDays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        spin.setAdapter(dataAdapterForDays);

        dataAdapterForDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);


                if(pos != 0){
                    dailyClimb = activityCounters.get(pos - 1).up_count + activityCounters.get(pos - 1).down_count;
                    dailyRun = activityCounters.get(pos - 1).run_count;
                    dailyWalk = activityCounters.get(pos - 1).walk_count;

                }
                else{

                    dailyWalk = walkCount;
                    dailyClimb = climbCount;
                    dailyRun = runCount;

                }

                if((dailyWalk + dailyClimb + dailyRun) != 0){
                    Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT).show();
                    mDecoView.addEvent(new DecoEvent.Builder(dailyWalk)
                            .setIndex(mSeries1Index)
                            .build());

                    mDecoView.addEvent(new DecoEvent.Builder(dailyClimb)
                            .setIndex(mSeries2Index)
                            .build());


                    mDecoView.addEvent(new DecoEvent.Builder(dailyRun)
                            .setIndex(mSeries3Index)
                            .build());


                    final TextView textPercentage = findViewById(R.id.textPercentage);


                    float percentFilled = (dailyClimb + dailyRun + dailyWalk) / mSeriesMax;
                    textPercentage.setText(String.format("%.2f%%", percentFilled * 100f));



                }



            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textPercentage = findViewById(R.id.textPercentage);
        final TextView textToGo = findViewById(R.id.textRemaining);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {


            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
                if(flag != 1){
                    float percentFilled = (((walkCount + runCount + climbCount) - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    textPercentage.setText(String.format("%.2f%%", percentFilled * 100f));
                    textToGo.setText("of Week");
                    ++flag;
                }
            }
        });



        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        final TextView textActivity1 = findViewById(R.id.textActivity1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity1.setText(String.format("%.2f%%", 100 * dailyWalk / (dailyWalk + dailyClimb + dailyRun)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity2 = findViewById(R.id.textActivity2);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity2.setText(String.format("%.2f%%", 100 * dailyClimb / (dailyWalk + dailyClimb + dailyRun)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity3 = findViewById(R.id.textActivity3);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.2f%%", 100 * dailyRun / (dailyWalk + dailyClimb + dailyRun)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(1000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(walkCount)
                .setIndex(mSeries1Index)
                .setDelay(1500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(2000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(runCount)
                .setIndex(mSeries2Index)
                .setDelay(2500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(3000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(climbCount)
                .setIndex(mSeries3Index)
                .setDelay(3500)
                .build());

    }



    private void setCounters(){
        for(ActivityCounter activityCounter : activityCounters){
            walkCount += activityCounter.walk_count;
            runCount += activityCounter.run_count;
            climbCount += (activityCounter.up_count + activityCounter.down_count);
        }
    }


    private void makeCall() {
        Call<ActivityCounterResponse> request = MainApplication.getApiInterface(this, null).getLineChartData();

        request.enqueue(new Callback<ActivityCounterResponse>() {
            @Override
            public void onResponse(Call<ActivityCounterResponse> call, Response<ActivityCounterResponse> response) {
                int code = response.code();
                ActivityCounterResponse res = response.body();

                activityCounters = res.data;

                setCounters();

                mSeriesMax = walkCount + climbCount + runCount;

                dailyWalk = walkCount;
                dailyClimb = climbCount;
                dailyRun = runCount;

                // Create required data series on the DecoView
                createBackSeries();
                createDataSeries1();
                createDataSeries2();
                createDataSeries3();

                // Setup events to be fired on a schedule
                createEvents();


               // Toast.makeText(getApplicationContext(), "" + activityCounters.get(0).run_count, Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onFailure(Call<ActivityCounterResponse> call, Throwable t) {
                //wifi_text.setText("SERVER ERROR");
            }
        });
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