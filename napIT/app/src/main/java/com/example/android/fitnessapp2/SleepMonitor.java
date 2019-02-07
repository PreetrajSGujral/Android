package com.example.android.fitnessapp2;
// Created by John
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SleepMonitor extends Service implements SensorEventListener  {

    private Sensor mySensor;
    private SensorManager SM;
    SQLiteHelper SensorReadingdb;
    String date, date1, time, time1;

    public SleepMonitor() {

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


        // keep the service running!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("SleepMonitor").setContentTitle("SleepMonitor").setContentText("SleepMonitor")
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

        Toast.makeText(this,"Starting Sleep Monitor", Toast.LENGTH_LONG).show();
        SensorReadingdb = new SQLiteHelper(this);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        //xText.setText("X: " + event.values[0]);
        //yText.setText("Y: " + event.values[1]);
        //zText.setText("Z: " + event.values[2]);

        //DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        //Getting date and time

        // send the data to the SleepActivity
        Intent RTReturn = new Intent(SleepActivity.Sleep_Monitor_Reading);
        RTReturn.putExtra("X",event.values[0]);
        RTReturn.putExtra("Y",event.values[1]);
        RTReturn.putExtra("Z",event.values[2]);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);

        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        date=dfDate.format(Calendar.getInstance().getTime());
        date1=date.toString();
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        time=dfTime.format(Calendar.getInstance().getTime());
        time1=time.toString();


        SensorReadingdb.addSensorReading(event.values[2], date1, time1);

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
        SM.unregisterListener(this, mySensor);
        Toast.makeText(this,"Stopping Sleep Monitor", Toast.LENGTH_LONG).show();
    }

}