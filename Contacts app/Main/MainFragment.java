package com.example.android.contacts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    View rootView;
    Context context;
    ListView listview;
    Button add, delete;
    DataBaseHelper db;
    ArrayList<String> flist= new ArrayList<>();

    FragmentActionListener fragmentActionListener;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        initUI();
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("OrientationChange","CountriesFragment onSaveInstanceState");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
    }
    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact List");

        final ListAdapter customAdapter= new CustomAdapterFragment(context,flist);
        final ListView customListView = (ListView)rootView.findViewById(R.id.Factivity_list);
        customListView.setAdapter(customAdapter);
        viewData(customAdapter);
    }


    private void initUI(){
        context  = getContext();
        add=(Button)rootView.findViewById(R.id.Fadd);
        delete=(Button)rootView.findViewById(R.id.Fdelete);
        db=new DataBaseHelper(context);

        final ListAdapter customAdapter= new CustomAdapterFragment(context,flist);
        final ListView customListView = (ListView)rootView.findViewById(R.id.Factivity_list);
        customListView.setAdapter(customAdapter);

        viewData(customAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentActionListener.move("x");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedItems.res.size() == 0) {
                    Toast.makeText(context, "NOTHING SELECTED TO DELETE", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < checkedItems.res.size(); i++) {
                        String deleteName = checkedItems.res.get(i);
                        db.delete(deleteName);
                        db.delete_from_user(deleteName);
                        db.delete_from_relation(deleteName);
                    }
                    ((CustomAdapterFragment) customAdapter).notifyDataSetChanged();
                    Toast.makeText(context, "res size is: " + checkedItems.res.size(), Toast.LENGTH_LONG)
                            .show();
                    checkedItems.res.clear();
                   onResume();
                }
            }
        });

        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {       //SHOW FUNCTION CALLED

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentActionListener.call_profile_fragment(flist.get(position).toString());
            }
        });
    }

    private void viewData(ListAdapter c) {
        Cursor cursor= db.viewData();
        flist.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(context, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                flist.add(cursor.getString(0));
            }
            if(flist.size()==0)
            {
                Toast.makeText(context, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            ((CustomAdapterFragment) c).notifyDataSetChanged();
        }
    }
}
