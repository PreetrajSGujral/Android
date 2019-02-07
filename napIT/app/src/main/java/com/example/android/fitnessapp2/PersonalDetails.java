package com.example.android.fitnessapp2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.fitnessapp2.R.id.female;


public class PersonalDetails extends AppCompatActivity {

    static final int REQ_CODE = 123;

    Button btnDetails;
    TextView textName, textEmail, textAge, textHeight, textWeight, textLocation,txtGender;
    SQLiteHelper Detailsdb;
    private Session session;
    String email1;


   // private SharedPreferences mpreferences;
   // private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        session = new Session(this);
        String email = session.getEmail();
        email1 = email.toString();


        textName = (TextView) findViewById(R.id.name_holder);
        textEmail = (TextView) findViewById(R.id.email_holder);
        textAge = (TextView) findViewById(R.id.age_holder);
        textHeight = (TextView) findViewById(R.id.height_holder);
        textWeight = (TextView) findViewById(R.id.weight_holder);
        textLocation = (TextView) findViewById(R.id.location_holder);

        txtGender = (TextView) findViewById(R.id.gender_holder);


        Detailsdb = new SQLiteHelper(this);
        Cursor data = Detailsdb.getLastRow(email1);
        if (data.getCount() == 0) {


        } else {

            while (data.moveToNext()) {
                // myList.add("Name: " + data.getString(1) + "\nEmail: " + data.getString(2) + "\nGender: " + data.getString(3) + "\nAge: " + data.getString(4) + "\nHeight: " + data.getString(5) + "\nWeight: " + data.getString(6) + "\nLocation: " + data.getString(7));
                //   myList.add(data.getString(2));


                //ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
                if (data.getString(1) != null)
                    textName.setText(data.getString(1));
                textEmail.setText(email1);
                if (data.getString(3) != null)
                   txtGender.setText(data.getString(3));

                if (data.getString(4) != null)
                    textAge.setText(data.getString(4));
                if (data.getString(5) != null)
                    textHeight.setText(data.getString(5));
                if (data.getString(6) != null)
                    textWeight.setText(data.getString(6));
                if (data.getString(7) != null)
                    textLocation.setText(data.getString(7));

            }


        }
        btnDetails = (Button) findViewById(R.id.btnDetails);
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonalDetails.this, EditPersonalDetails.class);
                startActivity(i);
            }
        });
    }
        /*
        mpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mpreferences.edit();

        String details_name = mpreferences.getString("Name", "");
        String details_email = mpreferences.getString("detailsEmail","");
        String details_gender = mpreferences.getString("detailsGender","");
        String details_age = mpreferences.getString("detailsAge","");
        String details_height = mpreferences.getString("detailsHeight","");
        String details_weight = mpreferences.getString("detailsWeight","");
        String details_location = mpreferences.getString("detailsLocation","");

        textName.setText(details_name);
        textEmail.setText(details_email);
        txtGender.setText(details_gender);
        textAge.setText(details_age);
        textHeight.setText(details_height);
        textWeight.setText(details_weight);
        textLocation.setText(details_location);


        btnDetails = (Button) findViewById(R.id.btnDetails);
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonalDetails.this, EditPersonalDetails.class);
                startActivityForResult(i, REQ_CODE);
            }
        });



    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

       /* if (requestCode == REQ_CODE_TAKE_PICTURE
                && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) intent.getExtras().get("data");
            ImageView img = (ImageView) findViewById(R.id.camera_image);
            img.setImageBitmap(bmp);
        }

        if(requestCode == REQ_CODE){

            String details_name = intent.getStringExtra("detailsName");
            String details_email = intent.getStringExtra("detailsEmail");
            String details_gender = intent.getStringExtra("detailsGender");
            String details_age = intent.getStringExtra("detailsAge");
            String details_height = intent.getStringExtra("detailsHeight");
            String details_weight = intent.getStringExtra("detailsWeight");
            String details_location = intent.getStringExtra("detailsLocation");

            //Toast.makeText(this, "Intent Received " + details_name, Toast.LENGTH_SHORT).show();

            textName.setText(details_name);
            textEmail.setText(details_email);
            txtGender.setText(details_gender);
            textAge.setText(details_age);
            textHeight.setText(details_height);
            textWeight.setText(details_weight);
            textLocation.setText(details_location);
            /*
            long id = Detailsdb.addPersonalDetails(textName.getText().toString(),textEmail.getText().toString(),details_gender,textAge.getText().toString(),textHeight.getText().toString(),textWeight.getText().toString(),textLocation.getText().toString());
            //Detailsdb.getRows();
          //  long id = Exercisedb.insertExercise(email1,stepsSinceReset,miles,calories,duration,date1);
            Toast.makeText(this,"Let's check values",Toast.LENGTH_SHORT).show();

            if(id<0){
                Toast.makeText(this,"Transferring to database failed",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Transfer Successful",Toast.LENGTH_SHORT).show();
            }



        }
        */
    }



