package com.example.android.fitnessapp2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewExercise extends AppCompatActivity {

    TextView steps,miles,cals,dur,date;
    TextView datesleep, resSleep;
    Button showMoreSleep,showMoreExercise;
    SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);
        steps=(TextView)findViewById(R.id.steps);
        miles=(TextView)findViewById(R.id.miles);
        cals=(TextView)findViewById(R.id.calories);
        dur=(TextView)findViewById(R.id.duration);
        date=(TextView)findViewById(R.id.date);

        //Sleep
        //datesleep = (TextView)findViewById(R.id.sleep_date);
        //resSleep = (TextView)findViewById(R.id.result);
        //showMoreSleep = (Button)findViewById(R.id.ShowMoreSleep);
        showMoreExercise=(Button)findViewById(R.id.ShowMore);
        sqLiteHelper=new SQLiteHelper(this);
        //final ArrayList<String> myList = new ArrayList<>();
        Cursor data = sqLiteHelper.getRecentEx();

        //Sleep
        //Cursor sleep = sqLiteHelper.getRecentSleep();
        //Toast.makeText(this, "Count"+sleep.getCount(), Toast.LENGTH_SHORT).show();

        if(data.getCount()==0){
            Toast.makeText(this,"Database is empty",Toast.LENGTH_SHORT).show();

        }
        else{
           // myList.add("Email: "+ data.getString(1)+"\nSteps: "+data.getString(2)+"\nMiles: "+ data.getString(3)+"\nCalories: "+ data.getString(4)+"\nDuration: "+data.getString(5)+"\nDate: "+data.getString(6));
            date.setText("Date: "+ data.getString(6));
            steps.setText("Steps: "+data.getString(2));
            miles.setText("Miles: "+data.getString(3));
            cals.setText("Calories: "+data.getString(4));
            dur.setText("Duration: "+data.getString(5));

        }
        showMoreExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewExercise.this,ViewList.class);
                startActivity(i);
            }
        });

        /*if(sleep.getCount() == 0){
            Toast.makeText(this,"Database is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            datesleep.setText("Date: "+ sleep.getString(2));
            resSleep.setText("Result: "+sleep.getString(4));
        }

        showMoreSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sleepInt = new Intent(ViewExercise.this, DisplayReading.class);
                startActivity(sleepInt);
            }
        });*/

    }
}
