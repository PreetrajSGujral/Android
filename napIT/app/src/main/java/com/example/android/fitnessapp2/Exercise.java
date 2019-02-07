package com.example.android.fitnessapp2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Exercise extends Activity {
    Chronometer simpleChronometer;
    TextView tv,displaydist,displayMiles,displayCalories;
    Button getDist,getMiles,getCalories,startButton,stopButton,restartButton,btnLogout;
    //public float stepsInSensor = 0;
    //public float stepsAtReset;
    public float miles,calories; //stepsSinceReset,
    float stepsSinceReset = 0;
    //String duration,email1;
    private Session session;


    public static final String RECEIVE_Count = "RECEIVE_Count";

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_Count)) {
                //get step count from service

                tv.setText(String.valueOf(intent.getFloatExtra("stepcount",0)));
                displayMiles.setText(String.valueOf(intent.getFloatExtra("Miles",0)));
                displaydist.setText(String.valueOf(intent.getFloatExtra("Distance",0)));
                displayCalories.setText(String.valueOf( intent.getFloatExtra("Calories",0)));


            }
        }
    };
    LocalBroadcastManager bManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        tv=(TextView)findViewById(R.id.value);
        restartButton=(Button)findViewById(R.id.restartex);
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        displayMiles=(TextView)findViewById(R.id.displaymiles);
        displayCalories=(TextView)findViewById(R.id.displaycals);
        getMiles=(Button)findViewById(R.id.getmiles);
        //viewList=(Button)findViewById(R.id.viewlist);
        getCalories=(Button)findViewById(R.id.getcals);
        startButton=(Button)findViewById(R.id.startex);
        stopButton=(Button)findViewById(R.id.stopex);

        displaydist=(TextView)findViewById(R.id.displaydistance);
        // getDist=(Button)findViewById(R.id.viewlist);

        //sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        session= new Session(this);
        if(!session.loggedin())
        {
            logout();
        }

        btnLogout=(Button)findViewById(R.id.idLogOut);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }

        });

        // register to receive broadcast from stepcounter
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_Count);
        bManager.registerReceiver(bReceiver, intentFilter);


        //// Sleep stop
        // comms to background service
        // trigger automatic intent at time of day
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTimeInMillis(System.currentTimeMillis());
        calendarStop.set(Calendar.HOUR_OF_DAY, 24); //12 am
        calendarStop.set(Calendar.MINUTE, 00); // set minutes

        AlarmManager StepResetAndStore = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //creating a new intent specifying the broadcast receiver
        Intent RESETSTEPCOUNTER = new Intent(BackgroundEventMonitor.Step_Counter_Reset);
        //creating a pending intent using the intent
        PendingIntent PendingResetCounter = PendingIntent.getBroadcast(this, 0, RESETSTEPCOUNTER, 0);
        StepResetAndStore.cancel(PendingResetCounter);
        //setting the repeating alarm that will be fired every day
        StepResetAndStore.setRepeating(AlarmManager.RTC, calendarStop.getTimeInMillis(), AlarmManager.INTERVAL_DAY, PendingResetCounter);
        Toast.makeText(this, "Step counter reset and store alarm is set", Toast.LENGTH_SHORT).show();


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stepsAtReset = stepsInSensor;

//                SharedPreferences.Editor editor =
//                        getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
//                editor.putFloat("stepsAtReset", stepsAtReset);
//                editor.commit();
                Intent i = new Intent(Exercise.this,StepCounter.class);
                startService(i);
                // you can now display 0:
                tv.setText(String.valueOf(0));
                simpleChronometer.start();

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Start = new Intent(StepCounter.Step_Counter_Reset_EOD);
                LocalBroadcastManager.getInstance(Exercise.this).sendBroadcast(Start);

                Intent i = new Intent(Exercise.this,StepCounter.class);
                stopService(i);
                simpleChronometer.stop();


            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                // stepsAtReset = stepsInSensor;

//                SharedPreferences.Editor editor =
//                        getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
//                editor.putFloat("stepsAtReset", stepsAtReset);
//                editor.commit();

                // you can now display 0:
                tv.setText(String.valueOf(0));

            }
        });




    }


    @Override
    protected void onResume() {
        super.onResume();
//        walk=true;
//        Sensor countSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if(countSensor != null){
//            sm.registerListener(this,countSensor,sm.SENSOR_DELAY_UI);
//
//        }
//        else
//        {
//            Toast.makeText(this,"Sensor Not Found!",Toast.LENGTH_SHORT).show();
//
//        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //walk=false;
        // send intent to service
    }


/*    public void onSensorChanged(SensorEvent sensorEvent) {
        if(walk)
        {
            stepsInSensor = sensorEvent.values[0];
            stepsSinceReset = stepsInSensor - stepsAtReset;
            tv.setText(String.valueOf(stepsSinceReset));
        }
        else
        {
            sensorEvent.values[0]=0;
       ]
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }*/



    private void logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(Exercise.this, MainActivity.class));
    }

    public void onDestroy() {
        super.onDestroy();
        bManager.unregisterReceiver(bReceiver);

    }


}
