package com.example.android.fitnessapp2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSleep extends AppCompatActivity {
    TextView datesleep, resSleep;
    Button showMoreSleep;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sleep);

        //Sleep
        datesleep = (TextView)findViewById(R.id.sleep_date);
        resSleep = (TextView)findViewById(R.id.result);
        showMoreSleep = (Button)findViewById(R.id.ShowMoreSleep);
        sqLiteHelper=new SQLiteHelper(this);

        Cursor sleep = sqLiteHelper.getRecentSleep();
        Toast.makeText(this, "Count"+sleep.getCount(), Toast.LENGTH_SHORT).show();


        if(sleep.getCount() == 0){
            Toast.makeText(this,"Database is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            datesleep.setText("Date: "+ sleep.getString(2));
            resSleep.setText("Result: "+sleep.getString(4));
        }

        showMoreSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sleepInt = new Intent(ViewSleep.this, DisplayReading.class);
                startActivity(sleepInt);
            }
        });
    }
}
