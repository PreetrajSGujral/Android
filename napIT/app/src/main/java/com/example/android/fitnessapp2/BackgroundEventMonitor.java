package com.example.android.fitnessapp2;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;
public class BackgroundEventMonitor extends Service {

    public static final String Sleep_Monitor_Trigger = "Sleep_Monitor_Trigger";
    public static final String Sleep_Monitor_Stop = "Sleep_Monitor_Stop";
    public static final String Step_Counter_Reset = "Step_Counter_Reset";

    public BackgroundEventMonitor() {
    }


    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Sleep_Monitor_Trigger))
            {
                //start the sleep monitor
                Intent i = new Intent(BackgroundEventMonitor.this,SleepMonitor.class);
                startService(i);
            }
            if(intent.getAction().equals(Sleep_Monitor_Stop))
            {
                //start the sleep monitor
                Intent i = new Intent(BackgroundEventMonitor.this,SleepMonitor.class);
                stopService(i);
            }

            if(intent.getAction().equals(Step_Counter_Reset))
            {

                // send the data to the SleepActivity
                Intent RTReturn = new Intent(StepCounter.Step_Counter_Reset_EOD);
                LocalBroadcastManager.getInstance(BackgroundEventMonitor.this).sendBroadcast(RTReturn);

                //start the stepcounter reset
                //Intent intent = new Intent(StepCounter.Step_Counter_ResetCount);
                // store to database
                // reset count
                // stop service
                // start service
            }
        }
        // Are we charging / charged?
//        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
//
//        // How are we charging?
////        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
////        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
////        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//
//            //compare times
//        if(isCharging )
//        {
//
////          //launch service
//
//        }

    };

    LocalBroadcastManager bManager;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
       // super.onCreate();

        Toast.makeText(this, "Starting Background Event Monitoring", Toast.LENGTH_LONG).show();

        // get the battery charging status
        // people normally charge their phones before they go to sleep
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        IntentFilter ifilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Sleep_Monitor_Trigger);
        intentFilter.addAction(Step_Counter_Reset);
        bManager.registerReceiver(bReceiver, ifilter);


        // keep the service running!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("BackgroundEventMonitor").setContentTitle("BackgroundEventMonitor").setContentText("BackgroundEventMonitor")
                //.setWhen(System.currentTimeMillis()).setAutoCancel(false)
                .setOngoing(true).setPriority(Notification.PRIORITY_HIGH);
        //.setContentIntent(pendIntent);
        Notification notification = builder.build();
        int id = (int)System.currentTimeMillis()%10000;
        startForeground(id, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bManager.unregisterReceiver(bReceiver);
    }
}
