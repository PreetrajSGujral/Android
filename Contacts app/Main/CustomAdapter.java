package com.example.android.contacts;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    public static final String s = "MyActivity";
    ArrayList<String> list;
    String name;
    public CustomAdapter(Context context, ArrayList<String> abc) {
        super(context, R.layout.contact, abc);
        this.list = abc;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        // default -  return super.getView(position, convertView, parent);
        // add the layout
        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        final View customView = myCustomInflater.inflate(R.layout.contact, parent, false);

        name= getItem(position);
        if(name!=null)
        {
            TextView txt1 = (TextView) customView.findViewById(R.id.name);
            txt1.setText(name);
            //   txt2.setText(x);
        }


        final CheckBox box=(CheckBox) customView.findViewById(R.id.box);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // Log.i(s,"onCheckedChanged: hello");
                //   list.remove(position);
                //   notifyDataSetChanged();
                if(box.isChecked()) {
                    checkedItems.res.add(getItem(position));
                Toast.makeText(getContext(), getItem(position)+" added to res", Toast.LENGTH_LONG)
                        .show();
                }
            }
        });
        return customView;
    }
}
