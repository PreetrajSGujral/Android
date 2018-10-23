package com.example.android.contacts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    Context context;
    View rootView;
    TextView f_t1;
    TextView f_showname;
    TextView f_t2;
    TextView f_showphone;
    TextView f_t3;

    String contact_phone;
    ArrayList<String> relationList= new ArrayList<>();

    DataBaseHelper dbh;

    FragmentActionListener fragmentActionListener;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        initUI();
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle= getArguments();
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact Details");
    }
    public void initUI()
    {
        context=getContext();
        f_t1=rootView.findViewById(R.id.one);
        f_t2=rootView.findViewById(R.id.two);
        f_t3=rootView.findViewById(R.id.three);
        f_showname=rootView.findViewById(R.id.showname);
        f_showphone=rootView.findViewById(R.id.showphone);
        dbh= new DataBaseHelper(context);

        Bundle bundle= getArguments();
        contact_phone=bundle.getString("KEY_SELECTED_USER");
        f_showname.setText(contact_phone);
        getphone(contact_phone);
       // Toast.makeText(context, "Value of phone is: "+contact_phone, Toast.LENGTH_SHORT).show();


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, relationList);
        final ListView listView= rootView.findViewById(R.id.relationlistF);
        listView.setAdapter(adapter);

        viewData(adapter, contact_phone);


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
        Cursor cursor= dbh.getphone(x);
        while(cursor.moveToNext())
        {
            f_showphone.setText(cursor.getString(1));
        }
    }
    public void viewData(ArrayAdapter adapter, String username)
    {
        Cursor cursor= dbh.viewRelation(username);
        relationList.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(context, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                relationList.add(cursor.getString(1));
            }
            if(relationList.size()==0)
            {
                Toast.makeText(context, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void viewProfile(String s)
    {
        fragmentActionListener.call_profile_fragment(s);

    }

}
