package com.example.android.mapsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MarkerList extends AppCompatActivity {
    ArrayList<String> markers;
    DataBaseHelper mdb;
    ListView MlistView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_list);
        MlistView = (ListView) findViewById(R.id.markedLocations);
        mdb = new DataBaseHelper(this);
        markers= new ArrayList<>();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MarkerList.this, android.R.layout.simple_list_item_1, markers);
        MlistView.setAdapter(adapter1);
        MviewData(adapter1);
    }
    public void MviewData(ArrayAdapter adapter)
    {
        Cursor cursor= mdb.viewMarkerData();
        markers.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                markers.add(cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2));
            }
            if(markers.size()==0)
            {
                Toast.makeText(MarkerList.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
