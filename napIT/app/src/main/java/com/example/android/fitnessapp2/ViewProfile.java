package com.example.android.fitnessapp2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity {

    ListView lview;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        lview = (ListView) findViewById(R.id.viewlist);
        sqLiteHelper = new SQLiteHelper(this);
        final ArrayList<String> myList = new ArrayList<>();
        Cursor data = sqLiteHelper.getProfileData();
        if (data.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();

        } else {
            while (data.moveToNext()) {
                myList.add("Name: " + data.getString(1) + "\nEmail: " + data.getString(2) + "\nGender: " + data.getString(3) + "\nAge: " + data.getString(4) + "\nHeight: " + data.getString(5) + "\nWeight: " + data.getString(6)+"\nLocation: "+data.getString(7));
                //   myList.add(data.getString(2));


                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
                lview.setAdapter(listAdapter);
            }
        }

    }
    }

