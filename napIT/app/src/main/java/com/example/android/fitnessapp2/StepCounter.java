package com.example.android.fitnessapp2;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import android.os.IBinder;

import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class StepCounter extends Service implements SensorEventListener  {

    //global for this service
    public float stepsInSensor = 0;
    public float stepsAtReset;
    public float stepsSinceReset,miles,calories;
    boolean walk = false;
    SharedPreferences prefs;
    //Chronometer simpleChronometer;
    private Session session;
    String email;
    long startTime = 0;

    SensorManager sm; // sensor object
    public static final String Step_Counter_Reset_EOD = "Step_Counter_Reset_EOD";


    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Step_Counter_Reset_EOD))
            {

                // store to database
                insertdetails();
                // reset count
                // stop service
                // start service
            }
        }

    };

    LocalBroadcastManager bManager;

    public StepCounter() {

    }

    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        super.onCreate();
        prefs  = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        stepsAtReset = prefs.getFloat("stepsAtReset", 0);
        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Toast.makeText(this,"Starting step counter", Toast.LENGTH_LONG).show();

        // Register sensor Listener
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Step_Counter_Reset_EOD);
        bManager.registerReceiver(bReceiver, intentFilter);


        session= new Session(this);
        email = session.getEmail();
        //simpleChronometer.start();
        //simpleChronometer.setBase(SystemClock.elapsedRealtime());


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("STEPCOUNTER").setContentTitle("STEPCOUNTER").setContentText("STEPCOUNTER")
                //.setWhen(System.currentTimeMillis()).setAutoCancel(false)
                .setOngoing(true).setPriority(Notification.PRIORITY_HIGH);
                //.setContentIntent(pendIntent);
        Notification notification = builder.build();

        int id = (int)System.currentTimeMillis()%10000;
        startForeground(id, notification);
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stepsAtReset = stepsInSensor;
        //stopSelf();
        //super.onResume();
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putFloat("stepsAtReset", stepsAtReset);
        editor.commit();

        startTime = System.currentTimeMillis();

        walk=true;
        Sensor countSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null)
        {
            sm.registerListener(this,countSensor,sm.SENSOR_DELAY_UI);

        }
        else
        {
            Toast.makeText(this,"Sensor Not Found!",Toast.LENGTH_SHORT).show();

        }

        return super.onStartCommand(intent,flags,startId);

    }


    float getDistance()
    {
        return (stepsSinceReset*78)/(float)100000;
    }

    float getMiles()
    {
        return (float)(stepsSinceReset*0.0005);
    }

    float getCalories()
    {
        return (float)((stepsSinceReset*0.0005) * 0.0128);
    }

    String getDuration()
    {
        //simpleChronometer.stop();

        long millis = System.currentTimeMillis() - startTime;

        return String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        //simpleChronometer.getText().toString();
    }



    public void onSensorChanged(SensorEvent sensorEvent) {
       //if(walk)
       // {
            stepsInSensor = sensorEvent.values[0];
            stepsSinceReset = stepsInSensor - stepsAtReset;

            Intent RTReturn = new Intent(Exercise.RECEIVE_Count);
            RTReturn.putExtra("stepcount",stepsSinceReset);
            RTReturn.putExtra("Distance",getDistance());
            RTReturn.putExtra("Miles",getMiles());
            RTReturn.putExtra("Calories",getCalories());
            LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);

        //}
        //else
        //{
        //    sensorEvent.values[0]=0;
        //}

    }

    public void insertdetails()
    {

        SQLiteHelper Exercisedb = new SQLiteHelper(this);
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        String date1=date.toString();
        DateFormat dfTime = new SimpleDateFormat("HH:mm");

        long id = Exercisedb.insertExercise(email.toString(),stepsSinceReset,getMiles(),getCalories(),getDuration(),date1);
        Toast.makeText(this,"Let's check values",Toast.LENGTH_SHORT).show();

        if(id<0){
            Toast.makeText(this,"Transferring to database failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Transfer Successful",Toast.LENGTH_SHORT).show();
        }

    }

    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }


    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Stopping step counter", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putFloat("stepsAtReset", stepsAtReset);
        editor.commit();

        bManager.unregisterReceiver(bReceiver);
    }


}




