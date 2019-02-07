package com.example.android.fitnessapp2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayReading extends AppCompatActivity {

    ListView sleepList;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reading);

        sqLiteHelper = new SQLiteHelper(this);
        sleepList = (ListView)findViewById(R.id.displayResult);
        final ArrayList<String> myList = new ArrayList<>();
        Cursor data = sqLiteHelper.getSleepResultData();

        if(data.getCount()==0){
            Toast.makeText(this,"Database is empty",Toast.LENGTH_SHORT).show();

        }
        else{
            while (data.moveToNext()){
                myList.add("Email: "+ data.getString(1)+"\nDate: "+data.getString(2)+"\nResult: "+ data.getString(4));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,myList);
                sleepList.setAdapter(listAdapter);
            }
        }
    }
}
