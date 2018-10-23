package com.example.android.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements FragmentActionListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DataBaseHelper db;
    Button add, delete;
    ListView listview;
    ArrayList<String> lists= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= new DataBaseHelper(this);

        add=findViewById(R.id.add);
        delete=findViewById(R.id.delete);

        fragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.activity_main_landscape)!= null){
            // call the main fragment for container 1
            addMainFragment();
            //  move(1);
        }
        else {

            // Pass the  arrary to the constructor.
            // final ListAdapter customListAdapter= new CustomAdapter(this,lists);
            final ListAdapter customListAdapter = new CustomAdapter(this, lists);
            final ListView customListView = (ListView) findViewById(R.id.activity_list);
            customListView.setAdapter(customListAdapter);

            viewData(customListAdapter);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedItems.res.size() == 0) {
                        Toast.makeText(MainActivity.this, "NOTHING SELECTED TO DELETE", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < checkedItems.res.size(); i++) {
                            String deleteName = checkedItems.res.get(i);
                            db.delete(deleteName);
                            db.delete_from_user(deleteName);
                            db.delete_from_relation(deleteName);
                        }
                        ((CustomAdapter) customListAdapter).notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "res size is: " + checkedItems.res.size(), Toast.LENGTH_LONG)
                                .show();
                        checkedItems.res.clear();
                        onRestart();
                    }
                }
            });

            customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {       //SHOW FUNCTION CALLED

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    viewProfile(lists.get(position));
                }
            });
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        final ListAdapter customListAdapter= new CustomAdapter(this,lists);
        final ListView customListView = (ListView) findViewById(R.id.activity_list);
        customListView.setAdapter(customListAdapter);

        viewData(customListAdapter);
    }
    //FUNCTION TO CALL THE ACTIVITY CONTACT DETAILS UPON CLICKING add IN THE MAIN FILE
    public void addContact(View v)
    {
        Intent i= new Intent(this,ContactDetails.class );
        i.putExtra("list", lists);
        startActivity(i);

    }

    public void viewProfile(String s)
    {
        Intent i1= new Intent(this,Profile.class );
        i1.putExtra("contactname", s);
        startActivity(i1);
    }

    // FUNCTION TO VIEW THE DATA PRESENT IN THE MAIN LISTVIEW FROM THE DATABASE
    private void viewData(ListAdapter c) {
        Cursor cursor= db.viewData();
        lists.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                lists.add(cursor.getString(0));
            }
            if(lists.size()==0)
            {
                Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            ((CustomAdapter) c).notifyDataSetChanged();
        }
    }


    @Override
    public void refresh_main_list()
    {
        addMainFragment();
    }

    @Override
    public void call_profile_fragment(String s)
    {
        Log.i("MOVE", "calling the profile fragment ");
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment= new ProfileFragment();
        profileFragment.setFragmentActionListener(this);
        Bundle bundle= new Bundle();
        bundle.putString(KEY_SELECTED_USER, s);
        profileFragment.setArguments(bundle);
        fragmentManager.popBackStack();
        fragmentTransaction.add(R.id.fragmentContainer2,profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void addMainFragment()
    {
        Log.i("MOVE", "calling the MAIN fragment ");
        fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment main= new MainFragment();
        main.setFragmentActionListener(this);

        fragmentTransaction.add(R.id.fragmentContainer,main);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void addContactsFragment(){
        Log.i("MOVE", "calling the add contacts fragment ");
        fragmentTransaction = fragmentManager.beginTransaction();
        ContactsFragment contactsFragment= new ContactsFragment();
        contactsFragment.setFragmentActionListener(this);

        fragmentTransaction.add(R.id.fragmentContainer2,contactsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void move(String x)
    {
        addContactsFragment();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        if(newConfig.orientation ==Configuration.ORIENTATION_LANDSCAPE){
            Log.i("OrintationChange", "landscape");
            addMainFragment();
        }
    }
}