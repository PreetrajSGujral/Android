package com.example.android.fitnessapp2;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class SVM extends Service {

    //https://github.com/yctung/AndroidLibSVM
    //global for this service
    static final String LOG_TAG = "LibSVM";
    String systemPath,email1;
    private Session session;
    String appFolderPath;
    LibSVM svm;
    SQLiteHelper helper;
    Cursor cursor;
    int cv;
    Vector SensorReadingsByDate;
    Vector AllSensorReadings;
    String date, date1;

    public SVM() {
        /*systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        appFolderPath = systemPath + "libsvm_Example_Datasets/"; // your datasets folder
        svm = new LibSVM();*/
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
        Toast.makeText(this,"Pulling data from database", Toast.LENGTH_LONG).show();

        systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        appFolderPath = systemPath + "libsvm/"; // your datasets folder

        //Create necessary folders to save model files
        CreateAppFolderIfNeeded();
        copyAssetsDataIfNeed();

        session= new Session(this);
        String email= session.getEmail();
        email1=email.toString();

        helper = new SQLiteHelper(this);


        //Assign model/output paths
        /*String dataTrainPath = appFolderPath+"heart_scale ";
        String dataScaledPath = appFolderPath+"scaled";
        String dataPredictPath = appFolderPath+"heart_scale ";
        String modelPath = appFolderPath+"model ";
        String outputPath = appFolderPath+"predict ";*/

        svm = new LibSVM();
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //boolean TrainMode = intent.getBooleanExtra("Train",false);

        /*if(TrainMode)
        {
            TrainSVM();
        }
        else
        {
            PredictSVM();
        }*/
        TrainSVM();
        PredictSVM();

        //return mStartMode;
        stopSelf();
        return super.onStartCommand(intent,flags,startId);

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
        Toast.makeText(this,"Finished", Toast.LENGTH_LONG).show();
    }

    public void GetDataFromDatabase(String  date)
    { // Pull data for given night

        // Convert to weird sparse matrix format see files under napit/libsvm_Example_Datasets
        // https://www.csie.ntu.edu.tw/~cjlin/libsvm/faq.html#/Q3:_Data_preparation
        // Also // //https://github.com/yctung/AndroidLibSVM

        // write to file at under appFolderPath
        // since this SVM only seems to like files.

        SQLiteHelper helper = new SQLiteHelper(this);
        cursor = helper.getSensorReadingsByDate(date);

        SensorReadingsByDate = new Vector();
        //cv =  helper.getReadingsByDate(date);
        if(cursor.getCount()!= 0){
            Toast.makeText(this,"Data"+cursor.getCount(),Toast.LENGTH_SHORT).show();

        }
        else {
            int j = 0;
            while (cursor.moveToNext()) {
                j++;
            }
            Toast.makeText(this, "Data" + j, Toast.LENGTH_LONG).show();
        }

        if (cursor.moveToFirst()) {
            do {
                SensorReadingsByDate.addElement(Float.parseFloat(cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        //AllSensorReadings = helper.getAllSensorReadings();

        int len = SensorReadingsByDate.size();
        Toast.makeText(this, "Getting Data" + len, Toast.LENGTH_SHORT).show();

        //Feature 1: Average
        String avg_string = getAverage();
        //Feature 2: Peak
        String max_string = getPeak();
        //Feature 3: Minimum
        String min_string = getMin();
        //Feature 4: Standard Deviation
        String dev_string = getStdDev();

        //A row of file
        String str = "+1 1:" + avg_string + " 2:"+ max_string + " 3:" + min_string  + " 4:"+ dev_string+"\n";

        //Writing the data to a file
        File file = new File(appFolderPath, "userData");
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Feature 1: Average
    //Taking average of all z-values
    public String getAverage(){

        float sum = 0;
        float avg;
        String avg_string;
        for (int i = 0; i < SensorReadingsByDate.size(); i++){
            sum = sum + (float) SensorReadingsByDate.elementAt(i);
        }
        avg = sum/SensorReadingsByDate.size();
        Toast.makeText(this, "Getting Average! "+ avg, Toast.LENGTH_SHORT).show();
        avg_string = "" + avg;

        return avg_string;
    }

    public float getAverageForStdDev(){

        float sum = 0;
        float avg;
        for (int i = 0; i < SensorReadingsByDate.size(); i++){
            sum = sum + (float) SensorReadingsByDate.elementAt(i);
        }
        avg = sum/SensorReadingsByDate.size();
        return avg;
    }

    //Feature 2: Peak
    //Taking max z-value
    public String getPeak(){

        float max = 0;
        String max_string;
        Collections.sort(SensorReadingsByDate);
        max = (float) SensorReadingsByDate.lastElement();
        Toast.makeText(this, "Getting Maximum! "+ max, Toast.LENGTH_SHORT).show();
        max_string = "" + max;
        return max_string;
    }

    //Feature 3: Minimum
    //Taking minimum z-value
    public String getMin(){

        float min = 0;
        String min_string;
        Collections.sort(SensorReadingsByDate);
        min = (float) SensorReadingsByDate.firstElement();
        Toast.makeText(this, "Getting Minimum! "+ min, Toast.LENGTH_SHORT).show();
        min_string = "" + min;
        return min_string;
    }

    //Feature 4: Standard Deviation
    public String getStdDev() {
        float u = getAverageForStdDev();
        float sq_diff = 0;
        float mean_sq_diff=0;
        String dev_string;
        double std=0;
        for (int i=0; i<SensorReadingsByDate.size(); i++){
            sq_diff = (float) (sq_diff + Math.pow(((float)SensorReadingsByDate.elementAt(i) - u),2));
        }
        Toast.makeText(this, "Getting Square diff!" + sq_diff, Toast.LENGTH_SHORT).show();
        mean_sq_diff =  sq_diff/SensorReadingsByDate.size();
        Toast.makeText(this, "Getting meean!" + mean_sq_diff, Toast.LENGTH_SHORT).show();
        std =  Math.sqrt(mean_sq_diff);
        dev_string = "" +std;
        Toast.makeText(this, "Getting Standard Deviation!" + std, Toast.LENGTH_SHORT).show();
        return dev_string;

    }

    public void TrainSVM()
    { // train the SVM with some data
        Toast.makeText(this, "Training Data!", Toast.LENGTH_SHORT).show();
        //svm.scale(appFolderPath + "heart_scale ", appFolderPath + "heart_scale_scaled");
        //svm.train("-t 2 "/* svm kernel */ + appFolderPath + "heart_scale_scaled " + appFolderPath + "model");;

        svm.scale(appFolderPath + "TrainingData4 ", appFolderPath + "scaledData");
        svm.train("-t 2 "/*svm kernel*/ + appFolderPath + "TrainingData4 " + appFolderPath + "sleepmodel ");
    }

    public void PredictSVM()
    {
        // need to pass in a date to examine
        //getting the current date
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        date1=dfDate.format(Calendar.getInstance().getTime());
        date=date1.toString();

        //pass the date to examine the data of that particular date
        GetDataFromDatabase(date);

        // try to predict something
        Toast.makeText(this,"Analysing Data", Toast.LENGTH_LONG).show();
        //svm.predict(appFolderPath + "hear_scale_predict " + appFolderPath + "model " + appFolderPath + "predict ");
        svm.predict(appFolderPath + "userData "+ appFolderPath+"sleepmodel " + appFolderPath + "result ");

        //WriteAnalysisResultsToDB();
        //displayResult();
    }

    public void WriteAnalysisResultsToDB()
    { // whatever format this puts out
        // store into the database with username and date.

        //Creating a database object
        SQLiteHelper helper = new SQLiteHelper(this);
        File file = new File(appFolderPath, "result");

        //Add the file to the db
        helper.addLibSVM_Output(file);
    }

    //display the result in a text function
    /*void displayResult(){
        String sleepOutput;
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
        helper.addSVMOutput(sleepOutput,);
        Toast.makeText(this, "Output!" + sleepOutput, Toast.LENGTH_SHORT).show();

        Intent RTReturn = new Intent(SleepActivity.SleepResultFromSVM);
        RTReturn.putExtra("SleepResult",sleepOutput);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);

        //Intent in = new Intent();
        //in.putExtra("SleepResult", sleepOutput);
        //sendBroadcast(in);
    }
*/

    private void CreateAppFolderIfNeeded(){
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            folder.mkdir();
            Log.d(LOG_TAG,"Appfolder is not existed, create one");
        } else {
            Log.w(LOG_TAG,"WARN: Appfolder has not been deleted");
        }

    }

    private void copyAssetsDataIfNeed() {
        String assetsToCopy[] = {"heart_scale_predict","heart_scale_train","heart_scale", "TrainingData4"};
        //String targetPath[] = {C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigTrain+".wav", C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigPredict+".wav",C.systemPath+C.INPUT_FOLDER+"SomeoneLikeYouShort.mp3"};

        for(int i=0; i<assetsToCopy.length; i++){
            String from = assetsToCopy[i];
            String to = appFolderPath+from;

            // 1. check if file exist
            File file = new File(to);
            if(file.exists()){
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: file exist, no need to copy:"+from);
            } else {
                // do copy
                boolean copyResult = copyAsset(getAssets(), from, to);
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: copy result = "+copyResult+" of file = "+from);
            }
        }
    }

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "[ERROR]: copyAsset: unable to copy file = "+fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
