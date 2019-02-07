package com.example.android.fitnessapp2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    Button subbutton;
    EditText subfb;

    Intent emailIntent=null, chooser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        subbutton=(Button)findViewById(R.id.submitbutton);
        subfb = (EditText)findViewById(R.id.submitfb);
        subbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                //emailIntent.setType("text/plain");
                String[] TO = {"infonapit@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feed Back");
                emailIntent.putExtra(Intent.EXTRA_TEXT, subfb.getText().toString());
                emailIntent.setType("message/rfc822");
                chooser=Intent.createChooser(emailIntent,"Send Email");
                startActivity(chooser);

                //Toast.makeText(ContactUs.this,"Your Feedback has been submitted. Thank You!",Toast.LENGTH_LONG).show();


            }
        });
    }
}
