package com.example.android.contacts;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactDetails extends AppCompatActivity {

    EditText name;
    EditText phone;
    Button add;
    ArrayList<String> relations= new ArrayList<>();
    ArrayList<String> ischecked= new ArrayList<>();
    String hisname, relationname;
    DataBaseHelper abc;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_add);

        name=findViewById(R.id.enterName);
        phone=findViewById(R.id.enterPhone);
        add=findViewById(R.id.addValues);

        abc= new DataBaseHelper(this);

        adapter= new ArrayAdapter<String>(ContactDetails.this, android.R.layout.simple_list_item_multiple_choice, relations);
        final ListView listView= (ListView) findViewById(R.id.relationlist);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        viewData2(adapter);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().length() <= 0) {
                    Toast.makeText(ContactDetails.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if( phone.getText().toString().length() <= 0)
                {
                    Toast.makeText(ContactDetails.this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String a=name.getText().toString(), b=phone.getText().toString();
                    Toast.makeText(ContactDetails.this, "NAME: "+a+"  PHONE: "+b, Toast.LENGTH_SHORT).show();
                    boolean isit=abc.insertData(a,b);
                    Toast.makeText(ContactDetails.this, " Has it been added? "+b, Toast.LENGTH_SHORT).show();
                    for(int j=0;j<ischecked.size();j++)
                    {
                        abc.insertRelation(a,ischecked.get(j));
                        abc.insertRelation(ischecked.get(j),a);
                    }
                    finish();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(listView.isItemChecked(i)) {

                    hisname = name.getText().toString();
                    relationname = relations.get(i);
                    /*abc.insertRelation(hisname, relationname);
                    abc.insertRelation(relationname, hisname);*/

                    String s=relations.remove(i);
                    relations.add(0,s);
                    listView.setItemChecked(0,true);
                    ischecked.add(s);

                    for(int i1=0;i1<relations.size();i1++)
                    {
                        if(ischecked.contains(relations.get(i1)))
                        {
                            listView.setItemChecked(i1,true);
                        }
                        else
                        {
                            listView.setItemChecked(i1,false);
                        }
                    }
                    listView.invalidateViews();
                }
                else {
                    /*String s1=relations.get(i);
                        ischecked.remove(s1);
                    listView.invalidateViews();*/
                    String s=relations.remove(i);
                    relations.add(s);
                    listView.setItemChecked(relations.size()-1,false);
                    ischecked.remove(s);

                    for(int i1=0;i1<relations.size();i1++)
                    {
                        if(ischecked.contains(relations.get(i1)))
                        {
                            listView.setItemChecked(i1,true);
                        }
                        else
                        {
                            listView.setItemChecked(i1,false);
                        }
                    }
                    listView.invalidateViews();
                }
            }

        });

    }
    private void viewData2(ArrayAdapter c) {
        Cursor cursor= abc.viewData();
        relations.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                relations.add(cursor.getString(0));
            }
            if(relations.size()==0)
            {
                Toast.makeText(ContactDetails.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            c.notifyDataSetChanged();
        }
    }
}
