package com.example.android.fitnessapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;



public class EditPersonalDetails extends AppCompatActivity {


    private Button saveDetails;
    private EditText detailsName, detailsAge, detailsHeight, detailsWeight, detailsLocation;
    private String name = "";
    private String email = "";
    private String gender="";
    private String age = "";
    private String weight = "";
    private String height = "";
    private String location = "";
    RadioButton male,female;
    String whichChecked,email1;
    SQLiteHelper sqLiteHelper;
    private Session session;
   // private SharedPreferences mpreferences;
    //private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);

        male=(RadioButton)findViewById(R.id.radio_male);
        female=(RadioButton)findViewById(R.id.female);
        detailsName = (EditText)findViewById(R.id.edit_name);
        session= new Session(this);
        final String email= session.getEmail();
        email1=email.toString();
        sqLiteHelper = new SQLiteHelper(this);
        detailsAge = (EditText)findViewById(R.id.edit_age);
        detailsHeight = (EditText) findViewById(R.id.edit_height);
        detailsWeight = (EditText) findViewById(R.id.edit_weight);
        detailsLocation = (EditText) findViewById(R.id.edit_location);


        Cursor data = sqLiteHelper.getLastRow(email1);
        if (data.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();

        } else {

            while (data.moveToNext()) {
                // myList.add("Name: " + data.getString(1) + "\nEmail: " + data.getString(2) + "\nGender: " + data.getString(3) + "\nAge: " + data.getString(4) + "\nHeight: " + data.getString(5) + "\nWeight: " + data.getString(6) + "\nLocation: " + data.getString(7));
                //   myList.add(data.getString(2));


                //ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
                if (data.getString(1) != null)
                    detailsName.setText(data.getString(1));

                if(data.getString(3)!=null) {
                    if (data.getString(3).equals("Male")) {

                        male.setChecked(true);
                        whichChecked="Male";
                    }
                    if (data.getString(3).equals("Female")) {
                        female.setChecked(true);
                        whichChecked="Female";
                    }


                }

                if (data.getString(4) != null)
                    detailsAge.setText(data.getString(4));
                if (data.getString(5) != null)
                    detailsHeight.setText(data.getString(5));
                if (data.getString(6) != null)
                    detailsWeight.setText(data.getString(6));
                if (data.getString(7) != null)
                    detailsLocation.setText(data.getString(7));

            }
        }




      //  mpreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // editor = mpreferences.edit();


        saveDetails = (Button)findViewById(R.id.saveDetails);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = detailsName.getText().toString();

                gender = whichChecked;
                age = detailsAge.getText().toString();
                height = detailsHeight.getText().toString();
                weight = detailsWeight.getText().toString();
                location = detailsLocation.getText().toString();
                long id = sqLiteHelper.addPersonalDetails(name, email1, gender, age, height, weight, location);
                Toast.makeText(EditPersonalDetails.this, "Let's check values", Toast.LENGTH_SHORT).show();

                if (id < 0) {
                    Toast.makeText(EditPersonalDetails.this, "Transferring to database failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditPersonalDetails.this, "Transfer Successful", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(EditPersonalDetails.this, PersonalDetails.class);
                startActivity(i);
            }


                /*
                editor.putString("Name",name);
                editor.putString("detailsEmail",email1);
                editor.putString("detailsGender",gender);
                editor.putString("detailsAge",age);
                editor.putString("detailsHeight",height);
                editor.putString("detailsWeight",weight);
                editor.putString("detailsLocation",location);
                editor.commit();

                Intent intent = new Intent();
                intent.putExtra("detailsName", name);
                intent.putExtra("detailsEmail", email1);
                intent.putExtra("detailsGender",gender);
                intent.putExtra("detailsAge", age);
                intent.putExtra("detailsHeight", height);
                intent.putExtra("detailsWeight", weight);
                intent.putExtra("detailsLocation", location);
                setResult(RESULT_OK, intent);
                //Toast.makeText(EditPersonalDetails.this, "Intent Sent!" + name, Toast.LENGTH_SHORT).show();
                finish();*/
                //     }
                // });

            });

    }
    public void onRadioButtonClicked(View view) {

        if(male.isChecked())
        {
            whichChecked = "Male";

        }
        else
        {
            whichChecked="Female";
        }


    }
}


