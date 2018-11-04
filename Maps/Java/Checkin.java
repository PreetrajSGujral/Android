package com.example.android.mapsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Checkin extends AppCompatActivity {
    ArrayList<String> relationList= new ArrayList<>();
    DataBaseHelper db;
    ListView listView;

    String full;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        listView= (ListView)findViewById(R.id.list);
        db= new DataBaseHelper(this);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(Checkin.this, android.R.layout.simple_list_item_1, relationList);
        listView.setAdapter(adapter);
        viewData(adapter);
    }

    //LIST OF CHECK IN LOCATIONS
    public void viewData(ArrayAdapter adapter)
    {
        Cursor cursor= db.viewData();
        relationList.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                relationList.add(cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(3)+", "+cursor.getString(4)+", "+cursor.getString(5));
            }
            if(relationList.size()==0)
            {
                Toast.makeText(Checkin.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
