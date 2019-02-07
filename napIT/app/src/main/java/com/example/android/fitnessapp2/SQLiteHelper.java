package com.example.android.fitnessapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/**
 * Created by Aishwarya Srikanth on 11/3/2017.
 * Modified by John Grun 11/23/2017
 * Modified by Madhura on 11/30/2017
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME="UserDataBase";

        public static final String TABLE_NAME="UserTable";

        public static final String Table_Column_ID="id";

        public static final String Table_Column_1_Name="name";

        public static final String Table_Column_2_Email="email";

        public static final String Table_Column_3_Password="password";

        ///Sensor table
        public static final String TABLE_NAME_Sensor="SensorTable";

        public static final String Table_Column_Sensor_ID="id";

        public static final String Table_Column_Sensor_Reading = "Sensor_Reading";

        public static final String Tables_Column_Sensor_Timestamp = "SensorTimestamp";

        public static final String Tables_Column_Sensor_Date = "SensorDate";

        public static final String Tables_Column_Sensor_Time = "SensorTime";

        //Sleep Anomaly Result table
        public static final String TABLE_NAME_AnomalyResult = "AnomalyResultTable";

        public static final String Table_Column_Anomaly_ID = "id";

        public static final String Table_Column_Email="email";

        public static final String Table_Column_User_Email = "UserEmail";

        public static final String Table_Column_Date = "date";

        public static final String Table_Column_Result = "results";

        //Normal sleep: +1, Abnormal Sleep: -1
        public static final String Table_Column_Decision = "decision";

        //Don't care about this right now
        //public static final String Table_Column_1_Username="name";

    //Personal Details Table
    public static final String TABLE_NAME_Details = "PersonalDetails";

    public static final String Table_Column_Details_ID="id";

    public static final String Table_Column_Gender = "gender";

    public static final String Table_Column_Age = "age";

    public static final String Table_Column_Weight = "weight";

    public static final String Table_Column_Height = "height";

    public static final String Table_Column_Location = "location";

    //ExerciseTable
    public static final String TABLE_NAME2 = "ExerciseTable";

    public static final String Ex_Col0= "Email";
    public static final String Ex_Col1 = "Steps";
    public static final String Ex_Col2 = "Miles";
    public static final String Ex_Col3 = "Calories";
    public static final String Ex_Col4 = "Duration";
    public static final String Ex_Col5 = "Date";
    public static final String Ex_ID = "Exercise_ID";



        public SQLiteHelper(Context context) {

            super(context, DATABASE_NAME, null, 28);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {

            String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR)";

            String CREATE_TABLE2="CREATE TABLE IF NOT EXISTS "+TABLE_NAME2+" ("+Ex_ID+" INTEGER PRIMARY KEY, "+Ex_Col0+" VARCHAR, "+Ex_Col1+" VARCHAR, "+Ex_Col2+" VARCHAR, "+Ex_Col3+" VARCHAR, "+Ex_Col4+" VARCHAR, "+Ex_Col5+" VARCHAR)";
            //sensor table
            String CREATE_TABLESensor="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_Sensor+" ("+Table_Column_Sensor_ID+" INTEGER PRIMARY KEY, "+Table_Column_Sensor_Reading+" REAL, " + Tables_Column_Sensor_Date+ " VARCHAR, "+Tables_Column_Sensor_Time + " VARCHAR);";
           //Personal table

            String CREATE_TABLEDetails = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Details + " ("+ Table_Column_Details_ID + " INTEGER PRIMARY KEY, " + Table_Column_1_Name+" VARCHAR, "+ Table_Column_2_Email +" VARCHAR, " +Table_Column_Gender+" VARCHAR, " + Table_Column_Age + " REAL, " + Table_Column_Height + " REAL, " + Table_Column_Weight + " REAL, " + Table_Column_Location + " VARCHAR " + ");";
            //sleep anomaly result table

            String CREATE_TABLEAnomaly="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_AnomalyResult+" ("+Table_Column_Anomaly_ID+" INTEGER PRIMARY KEY, "+Table_Column_User_Email+" VARCHAR, " + Table_Column_Date+ " VARCHAR, "+Table_Column_Result+ " REAL, " + Table_Column_Decision+" VARCHAR);";
            database.execSQL(CREATE_TABLE);
            database.execSQL(CREATE_TABLE2);
            database.execSQL(CREATE_TABLEDetails);
            database.execSQL(CREATE_TABLESensor);
            database.execSQL(CREATE_TABLEAnomaly);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_Sensor);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_AnomalyResult);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_Details);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
            onCreate(db);
        }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addSensorReading(float SensorReading, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Table_Column_Sensor_Reading, SensorReading); // Contact Name
        values.put(Tables_Column_Sensor_Date, date);
        values.put(Tables_Column_Sensor_Time, time);

        // Inserting Row
        db.insert(TABLE_NAME_Sensor, null, values);
        db.close(); // Closing database connection
    }


    // Getting single sensor reading
    float getSensorReading(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_Sensor, new String[] { Table_Column_Sensor_ID,
                        Table_Column_Sensor_Reading, Tables_Column_Sensor_Timestamp }, Table_Column_Sensor_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        float SensorReading = Float.parseFloat(cursor.getString(1));
        // return SensorReading
        return SensorReading;
    }
    public long insertExercise(String email,float steps,float miles, float calories, String duration,String date)
    {

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Ex_Col0,email);
        contentValues.put(Ex_Col1,steps);
        contentValues.put(Ex_Col2,miles);
        contentValues.put(Ex_Col3,calories);
        contentValues.put(Ex_Col4,duration);
        contentValues.put(Ex_Col5,date);
        long id = sqLiteDatabase.insert(TABLE_NAME2,null,contentValues);
        return id;

    }

    public boolean getUser(String email, String pass)
    {
        String selectQuery= "SELECT * FROM " + TABLE_NAME + " where " + Table_Column_2_Email + " = " + "'"+email+"'" + " and " + Table_Column_3_Password + " = " + "'"+pass+"'";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    public long addPersonalDetails(String name,String email,String gender,String age, String height, String weight,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table_Column_1_Name,name);
        values.put(Table_Column_2_Email,email);
        values.put(Table_Column_Gender,gender);
        values.put(Table_Column_Age,age);
        values.put(Table_Column_Height,height);
        values.put(Table_Column_Weight,weight);
        values.put(Table_Column_Location,location);
        long id = db.insert(TABLE_NAME_Details,null,values);
        return id;
    }

    public Cursor getProfileData(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("Select * FROM "+ TABLE_NAME_Details,null);
        return data;
    }


    public Cursor getListData(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("Select * FROM "+ TABLE_NAME2,null);
        return data;
    }
    public Cursor getRecentEx(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("Select * FROM "+ TABLE_NAME2,null);
        data.moveToLast();
        return data;

    }

    public Cursor getLastRow(String email){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selectQuery = "Select * FROM "+TABLE_NAME_Details+" WHERE "+ Table_Column_Details_ID+"=(Select MAX("+Table_Column_Details_ID+") FROM "+TABLE_NAME_Details+" WHERE "+Table_Column_2_Email+"='"+email+"')";
        Cursor data = sqLiteDatabase.rawQuery(selectQuery,null);
        return data;
    }

    public Cursor getLastRowStep(String email){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selectQuery = "Select * FROM "+TABLE_NAME2+" WHERE "+ Ex_ID+"=(Select MAX("+Ex_ID+") FROM "+TABLE_NAME2+" WHERE "+Ex_Col0+"='"+email+"')";
        Cursor data = sqLiteDatabase.rawQuery(selectQuery,null);
        return data;
    }

    public Cursor getLastRowResult(String email){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selectQuery = "Select * FROM "+TABLE_NAME_AnomalyResult+" WHERE "+ Table_Column_Anomaly_ID+"=(Select MAX("+Table_Column_Anomaly_ID+") FROM "+TABLE_NAME_AnomalyResult+" WHERE "+Table_Column_User_Email+"='"+email+"')";
        Cursor data = sqLiteDatabase.rawQuery(selectQuery,null);
        return data;
    }


    //get sensor reading for a particular date
    public int getReadingsByDate(String date) {

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_Sensor + " where " + Tables_Column_Sensor_Date + " = " + "'" + date + "'";
        // String selectQuery = "SELECT * FROM " + TABLE_NAME_Sensor;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount();
    }

    public Cursor getSensorReadingsByDate(String date){

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_Sensor + " where " + Tables_Column_Sensor_Date+ " = "+"'"+date+"'";
       // String selectQuery = "SELECT * FROM " + TABLE_NAME_Sensor;

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
        /*Vector SensorReadingsByDate = new Vector();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SensorReadingsByDate.addElement(Float.parseFloat(cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        // return contact list
        return SensorReadingsByDate;*/
    }

    // Getting All sensor readings
    public Vector getAllSensorReadings() {
        //List<float> contactList = new ArrayList<float>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME_Sensor;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Vector SensorReadings = new Vector();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SensorReadings.addElement(Float.parseFloat(cursor.getString(1)));
            } while (cursor.moveToNext());

        }

        // return contact list
        return SensorReadings;
    }
/*
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }*/

/*    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }*/

    // Getting contacts Count
    public int getSensorReadingCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_Sensor;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    //Adding the output of LibSVM to the database
    void addLibSVM_Output(File output) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dataToSave = null;
        //byte[] dataToSave = new byte[0];

        try {
            InputStream is = new FileInputStream(output);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead = is.read(b);
            while (bytesRead != -1){
                bos.write(b, 0, bytesRead);
                bytesRead = is.read(b);
                Log.v(TAG, "Bytes" + b);
            }
            byte[] bytes = bos.toByteArray();
            dataToSave = Base64.encodeToString(bytes, Base64.DEFAULT);
            //dataToSave = Base64.encode(bytes, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(Table_Column_Result, dataToSave); // Contact Name

        // Inserting Row
        db.insert(TABLE_NAME_AnomalyResult, null, values);
        db.close(); // Closing database connection
    }

    void addSVMOutput(String sleepOutput, String decision, String date, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        String dataToSave = sleepOutput;
        ContentValues values = new ContentValues();
        values.put(Table_Column_User_Email, email);
        values.put(Table_Column_Date, date);
        values.put(Table_Column_Result, dataToSave);
        values.put(Table_Column_Decision, decision);
        // Inserting Row
        db.insert(TABLE_NAME_AnomalyResult, null, values);
        db.close(); // Closing database connection
    }

    public Cursor getRecentSleep(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("Select * FROM "+ TABLE_NAME_AnomalyResult,null);
        data.moveToLast();
        return data;
    }

    public Cursor getSleepResultData(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("Select * FROM "+ TABLE_NAME_AnomalyResult,null);
        return data;
    }

}