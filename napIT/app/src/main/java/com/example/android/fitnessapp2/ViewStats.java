package com.example.android.fitnessapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewStats extends AppCompatActivity {
    Button exercise, sleep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats);

        exercise= (Button)findViewById(R.id.viewExercise);
        sleep=(Button)findViewById(R.id.viewSleep);

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(ViewStats.this, ViewExercise.class);
                startActivity(i1);
            }
        });

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2= new Intent(ViewStats.this, ViewSleep.class);
                startActivity(i2);
            }
        });
    }
}
