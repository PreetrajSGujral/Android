package com.example.android.fitnessapp2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SleepActivity extends Activity {
    private TextView xText, yText, zText;
    //private Sensor mySensor;
    Button startSleep, stopSleep, train_svm;
    TextView startSleepTime, stopSleepTime, sleepResult;
    String date, date1, time, time1;
    //private BroadcastReceiver statusReceiver;
    private Session session;
    String email1;

    //private SensorManager SM;
    //SQLiteHelper SensorReadingdb;
    IntentFilter filter;
    String systemPath, appFolderPath;
    public static final String Sleep_Monitor_Reading = "Sleep_Monitor_Reading";
    public static final String SleepResultFromSVM = "SleepResultFromSVM";

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Sleep_Monitor_Reading)) {
                //get step count from service
                float Xvalue = intent.getFloatExtra("X", 0);
                float Yvalue = intent.getFloatExtra("Y", 0);
                float Zvalue = intent.getFloatExtra("Z", 0);

                xText.setText("X: " + Xvalue);
                yText.setText("Y: " + Yvalue);
                zText.setText("Z: " + Zvalue);
            }
            if (intent.getAction().equals(SleepResultFromSVM)) {
                String sleepOutput = intent.getStringExtra("SleepResult");
                sleepResult.setText(sleepOutput);
            }

        }
    };
    LocalBroadcastManager bManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        //SensorReadingdb = new SQLiteHelper(this);
        systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        appFolderPath = systemPath + "libsvm/"; // your datasets folder

        session = new Session(this);
        String email = session.getEmail();
        email1 = email.toString();
        if (!session.loggedin()) {
            logout();
        }

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Sleep_Monitor_Reading);
        intentFilter.addAction(SleepResultFromSVM);
        bManager.registerReceiver(bReceiver, intentFilter);

        // comms to background service
        // trigger automatic intent at time of day
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(System.currentTimeMillis());
        calendarStart.set(Calendar.HOUR_OF_DAY, 22); //10pm
        calendarStart.set(Calendar.MINUTE, 30); // set minutes

        AlarmManager pm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //creating a new intent specifying the broadcast receiver
        Intent StartSleepMonitor = new Intent(BackgroundEventMonitor.Sleep_Monitor_Trigger);
        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, StartSleepMonitor, 0);
        pm.cancel(pi);
        //setting the repeating alarm that will be fired every day
        pm.setRepeating(AlarmManager.RTC, calendarStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Start sleep monitor alarm is set", Toast.LENGTH_SHORT).show();

        //// Sleep stop
        // comms to background service
        // trigger automatic intent at time of day
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTimeInMillis(System.currentTimeMillis());
        calendarStop.set(Calendar.HOUR_OF_DAY, 06); //6 am
        calendarStop.set(Calendar.MINUTE, 00); // set minutes

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //creating a new intent specifying the broadcast receiver
        Intent StopSleepMonitor = new Intent(BackgroundEventMonitor.Sleep_Monitor_Stop);
        //creating a pending intent using the intent
        PendingIntent PendingStopSleep = PendingIntent.getBroadcast(this, 0, StopSleepMonitor, 0);
        am.cancel(PendingStopSleep);
        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, calendarStop.getTimeInMillis(), AlarmManager.INTERVAL_DAY, PendingStopSleep);
        Toast.makeText(this, "Stop sleep monitor alarm is set", Toast.LENGTH_SHORT).show();

        // Assign TextView
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        startSleep = (Button) findViewById(R.id.startsleep);
        stopSleep = (Button) findViewById(R.id.stopbutton);
        startSleepTime = (TextView) findViewById(R.id.sleeptime1);
        stopSleepTime = (TextView) findViewById(R.id.sleeptime2);
        //train_svm = (Button) findViewById(R.id.Train_SVM);
        sleepResult = (TextView) findViewById(R.id.sleepresult);
        startSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                String date = dfDate.format(Calendar.getInstance().getTime());
                DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
                String time = dfTime.format(Calendar.getInstance().getTime());
                startSleepTime.setText(date + " " + time);
                Intent i = new Intent(SleepActivity.this, SleepMonitor.class);
                Intent svm = new Intent(SleepActivity.this, SVM.class);
                switch (view.getId()) {
                    case R.id.startsleep:
                        //SM.registerListener(SleepActivity.this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
                        // Start service
                        startService(i);
                        break;
                    case R.id.stopbutton:
                        // SM.unregisterListener(SleepActivity.this);
                        //stop service
                        stopService(i);
                        startService(svm);
                        break;
                }
            }


        });

        stopSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                String date = dfDate.format(Calendar.getInstance().getTime());
                DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
                String time = dfTime.format(Calendar.getInstance().getTime());
                stopSleepTime.setText(date + " " + time);
                Intent i = new Intent(SleepActivity.this, SleepMonitor.class);
                Intent svm = new Intent(SleepActivity.this, SVM.class);
                switch (view.getId()) {
                    case R.id.startsleep:
                        startService(i);
                        break;
                    case R.id.stopbutton:
                        //SM.unregisterListener(SleepActivity.this);
                        //Stop service
                        stopService(i);
                        startService(svm);
                        break;

                }

            }
        });


        /*train_svm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Train the SVM
                Toast.makeText(getApplicationContext(), "Train_SVM called!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SleepActivity.this,SVM.class);
                i.putExtra("Train",true);
                Toast.makeText(getApplicationContext(), "Service called!", Toast.LENGTH_SHORT).show();
                startService(i);
            }
        });*/

        /*train_svm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Train the SVM
                Toast.makeText(getApplicationContext(), "Train_SVM called!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SleepActivity.this,SVM.class);
                i.putExtra("Train",true);
                Toast.makeText(getApplicationContext(), "Service called!", Toast.LENGTH_SHORT).show();
                startService(i);
            }
        });*/

        //Receive the output from the service
//        statusReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String sleepOutput = intent.getStringExtra("SleepResult");
//                sleepResult.setText(sleepOutput);
//            }
//        };

        // series = new LineGraphSeries<DataPoint>();
    }


    protected void onResume() {
        super.onResume();
        //SM.unregisterListener(this);
        //SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        //SM.unregisterListener(this);
    }
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Not in use
//    }

    /// move to broadcast reciever
    // xText.setText("X: " + event.values[0]);
    // yText.setText("Y: " + event.values[1]);
    //zText.setText("Z: " + event.values[2]);


//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        //DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
//        //Getting date and time
//
//        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
//        date=dfDate.format(Calendar.getInstance().getTime());
//        date1=date.toString();
//        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
//        time=dfTime.format(Calendar.getInstance().getTime());
//        time1=time.toString();
//
//        SensorReadingdb.addSensorReading(event.values[2], date1, time1);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    public boolean WriteSensorReadingsToDatabase(SensorEvent event)
//    {
//
//        return true;
//    }

    /*public void Run_Analysis(View v)
    { // Run the analysis on the data
        Intent i = new Intent(this,SVM.class);
        startService(i);

    }

    public void Train_SVM(View v)
    { // Train the SVM

        Toast.makeText(this, "Train_SVM called!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,SVM.class);
        i.putExtra("Train",true);
        Toast.makeText(this, "Service called!", Toast.LENGTH_SHORT).show();
        startService(i);
    }*/

    //display the result in a text function
    public void displayResult(View v){
        String sleepOutput, res;

        SQLiteHelper helper = new SQLiteHelper(this);
        File file = new File(appFolderPath, "result");
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null){
                text.append(line);
                //text.append('\n');
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sleepOutput = text.toString();
        //Display whether sleep is proper or improper
        if (sleepOutput.equals("1"))
            res = "Proper Sleep";
        else if (sleepOutput.equals("-1"))
            res = "Improper Sleep";
        else res = "Error: Wrong output";

        sleepResult.setText(res);

        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        date=dfDate.format(Calendar.getInstance().getTime());
        date1=date.toString();
        DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        time=dfTime.format(Calendar.getInstance().getTime());

        helper.addSVMOutput(sleepOutput, res, date, email1);
        Toast.makeText(this, "Output!" + sleepOutput, Toast.LENGTH_SHORT).show();



        /*
        Intent in = new Intent();
        in.putExtra("SleepResult", sleepOutput);
        sendBroadcast(in);
        */
    }

    private void logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(SleepActivity.this, MainActivity.class));
    }

}



