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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    View rootView;
    Context context;
    EditText getname;
    EditText getphone;
    Button addperson;
    DataBaseHelper abc;
    String f_hisname, f_relationname;
    ArrayList<String> f_relations= new ArrayList<>();
    ArrayList<String> f_ischecked= new ArrayList<>();
    ArrayAdapter<String> f_adapter;


    FragmentActionListener fragmentActionListener;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact,container,false);
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
        //normal.setText(bundle.getString("KEY_SELECTED_COUNTRY"));
    }



    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    public void initUI()
    {
        context=getContext();
        getname=rootView.findViewById(R.id.FenterName);
        getphone=rootView.findViewById(R.id.FenterPhone);

        addperson=rootView.findViewById(R.id.FaddValues);

        abc= new DataBaseHelper(context);


        f_adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, f_relations);
        final ListView listView=rootView.findViewById(R.id.Frelationlist);
        listView.setAdapter(f_adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        viewData(f_adapter);


        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getname.getText().toString().length() <= 0) {
                    Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if( getphone.getText().toString().length() <= 0)
                {
                    Toast.makeText(context, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String a=getname.getText().toString(), b=getphone.getText().toString();
                    Toast.makeText(context, "NAME: "+a+"  PHONE: "+b, Toast.LENGTH_SHORT).show();
                    abc.insertData(a,b);
                    for(int j=0;j<f_ischecked.size();j++)
                    {
                        abc.insertRelation(a,f_ischecked.get(j));
                        abc.insertRelation(f_ischecked.get(j),a);
                    }
                        fragmentActionListener.refresh_main_list();
                        getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listView.isItemChecked(i)) {

                    f_hisname = getname.getText().toString();
                    f_relationname = f_relations.get(i);
                    /*abc.insertRelation(hisname, relationname);
                    abc.insertRelation(relationname, hisname);*/

                    String s=f_relations.remove(i);
                    f_relations.add(0,s);
                    listView.setItemChecked(0,true);
                    f_ischecked.add(s);

                    for(int i1=0;i1<f_relations.size();i1++)
                    {
                        if(f_ischecked.contains(f_relations.get(i1)))
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

                    String s=f_relations.remove(i);
                    f_relations.add(s);
                    listView.setItemChecked(f_relations.size()-1,false);
                    f_ischecked.remove(s);

                    for(int i1=0;i1<f_relations.size();i1++)
                    {
                        if(f_ischecked.contains(f_relations.get(i1)))
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



    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Contact");
    }


    private void viewData(ArrayAdapter c) {
        Cursor cursor= abc.viewData();
        f_relations.clear();
        if(cursor.getCount()==0)
        {
            Toast.makeText(context, "No data to show ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                f_relations.add(cursor.getString(0));
            }
            if(f_relations.size()==0)
            {
                Toast.makeText(context, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
            c.notifyDataSetChanged();
        }
    }

}