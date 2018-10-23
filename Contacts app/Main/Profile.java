package com.example.android.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    DataBaseHelper db;
    TextView names;
    TextView phone;
    ArrayList<String> relationList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_profile);

        Intent i= getIntent();
        String x=i.getStringExtra("contactname");

        names=(TextView) findViewById(R.id.showname);
        phone=(TextView) findViewById(R.id.showphone);
        db= new DataBaseHelper(this);
        names.setText(x);
        getphone(x);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(Profile.this, android.R.layout.simple_list_item_1, relationList);
        final ListView listView= (ListView) findViewById(R.id.relationlist);
        listView.setAdapter(adapter);
        viewData(adapter, x);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=listView.getItemAtPosition(i).toString();
                viewProfile(s);
            }
        });

    }
    public void getphone(String x)
    {
        Cursor cursor= db.getphone(x);
        while(cursor.moveToNext())
        {
            phone.setText(cursor.getString(1));
        }
    }
    public void viewData(ArrayAdapter adapter, String username)
    {
        Cursor cursor= db.viewRelation(username);
        relationList.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                relationList.add(cursor.getString(1));
            }
            if(relationList.size()==0)
            {
                Toast.makeText(Profile.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void viewProfile(String s)
    {
        String name =s;
        Intent i1= new Intent(this,Profile.class );
        i1.putExtra("contactname", name);
        startActivity(i1);
        finish();
    }
}
