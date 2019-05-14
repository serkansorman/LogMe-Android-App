package com.example.maddi.logme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class InfoActivity extends AppCompatActivity implements
        InfoFragment.OnFloatingButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinfo);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new InfoFragment())
                    .commit();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Information");
    }

    @Override
    public void onFloatingButtonClicked() {

        Bundle infos = getIntent().getExtras();

        String age = infos.getString("Age");
        String weight = infos.getString("Weight");
        String height = infos.getString("Height");
        int gender = infos.getInt("Gender");



        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Gender",gender); //int değer ekleniyor
        editor.putString("Weight",weight); //string değer ekleniyor
        editor.putString("Age",age); //string değer ekleniyor
        editor.putString("Height",height); //string değer ekleniyor
        editor.commit(); //Kayıt

       // Toast.makeText(this,age , Toast.LENGTH_LONG).show();


        Intent myIntent = new Intent(this, SetGoalActivity.class);
        startActivity(myIntent);
        finish();
    }
}
