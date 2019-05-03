package com.example.maddi.logme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetGoalActivity extends AppCompatActivity {
    public static float mSeries = 0f;
    public static float mSeries1 = 0f;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setgoal);

        final EditText stepGoal = findViewById(R.id.et1);


        Button setgoal = findViewById(R.id.setgoal);
        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepGoal.getText().toString().length() == 0) {
                    stepGoal.setError("Set Steps Goal");
                    return;
                }


                Intent myIntent = new Intent(SetGoalActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
