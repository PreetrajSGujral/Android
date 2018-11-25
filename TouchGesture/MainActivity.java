package com.example.android.assignment4touch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button touch;
    Button draw;
    Button multitouch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touch=(Button)findViewById(R.id.touch1);
        draw=(Button)findViewById(R.id.draw1);
        multitouch=(Button) findViewById(R.id.multi);
        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TouchActivity.class);
                startActivity(intent);
            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, WriteOnScreenActivity.class);
                startActivity(intent1);
            }
        });
        multitouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, MultiTouchActivity.class);
                startActivity(intent2);
            }
        });
    }
}
