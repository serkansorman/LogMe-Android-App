package com.example.maddi.logme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.view.animation.AnticipateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;


public class SummaryActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener  {


    private DecoView mDecoView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final String[] days = { "Week","Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday"};
    private ArrayAdapter<String> dataAdapterForDays;


    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;

    public static int weekWalkStep = 24523;
    public static int weekClimbStep = 3321;
    public static int weekRunStep = 12232;

    private final float mSeriesMax = 50000;
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

        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();

        // Setup events to be fired on a schedule
        createEvents();


        Spinner spin =  findViewById(R.id.spinner);

        dataAdapterForDays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        spin.setAdapter(dataAdapterForDays);

        dataAdapterForDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                if(!item.toString().equals("Week")){
                    Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT).show();
                    mDecoView.addEvent(new DecoEvent.Builder(4345)
                            .setIndex(mSeries1Index)
                            .build());

                    mDecoView.addEvent(new DecoEvent.Builder(2453)
                            .setIndex(mSeries2Index)
                            .build());


                    mDecoView.addEvent(new DecoEvent.Builder(224)
                            .setIndex(mSeries3Index)
                            .build());

                    final TextView textToGo = findViewById(R.id.textRemaining);
                    final TextView textPercentage = findViewById(R.id.textPercentage);

                    textToGo.setText(String.format("%.0f Steps to goal", mSeriesMax - 7022));
                    float percentFilled = 7022 / mSeriesMax;
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
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if(flag != 2){
                    float percentFilled = (((weekWalkStep + weekClimbStep + weekRunStep) - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    textPercentage.setText(String.format("%.2f%%", percentFilled * 100f));

                    ++flag;
                }

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        final TextView textToGo = findViewById(R.id.textRemaining);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if(flag != 2) {
                    textToGo.setText(String.format("%.0f Steps to goal", seriesItem.getMaxValue() - (weekWalkStep + weekClimbStep + weekRunStep)));
                    ++flag;
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        final TextView textActivity1 = findViewById(R.id.textActivity1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity1.setText(String.format("%.0f Step", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity2 = findViewById(R.id.textActivity2);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity2.setText(String.format("%.0f Step", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textActivity3 = findViewById(R.id.textActivity3);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.0f Step", currentPosition));
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

        mDecoView.addEvent(new DecoEvent.Builder(weekWalkStep)
                .setIndex(mSeries1Index)
                .setDelay(1500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(2000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(weekRunStep)
                .setIndex(mSeries2Index)
                .setDelay(2500)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(3000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(weekClimbStep)
                .setIndex(mSeries3Index)
                .setDelay(3500)
                .build());
        /*
        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries3Index).setDelay(9000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries2Index).setDelay(9000).build());
    */
        /*mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(9000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                    }
                })
                .build());*/

        /*mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(14500)
                .setDuration(1500)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                    }
                })
                .build());*/

        //resetText();
    }

    private void resetText() {
        ((TextView) findViewById(R.id.textActivity1)).setText("");
        ((TextView) findViewById(R.id.textActivity2)).setText("");
        ((TextView) findViewById(R.id.textActivity3)).setText("");
        ((TextView) findViewById(R.id.textPercentage)).setText("");
        ((TextView) findViewById(R.id.textRemaining)).setText("");
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