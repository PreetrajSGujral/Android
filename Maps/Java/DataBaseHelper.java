package com.example.android.mapsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Checkin.db";
    private static final String DB_TABLE="MapsApp";
    private static final String LOC = "LOC";
    private static final String TIMES = "TIMES";
    private static final String DATES = "DATES";
    private static final String LAT = "LAT";
    private static final String LON = "LON";
    private static final String ADDR = "ADDR";


    private static final String TABLE="CheckInTime";
    private static final String NAME = "LOC_NAME";
    private static final String LATI = "LATI";
    private static final String LONGI = "LONGI";
    private static final String ADDRESS = "ADDRESS";
    private static final String TIME = "TIME";



    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" +
            LOC + " TEXT, " +
            LAT + " TEXT, " +
            LON + " TEXT, " +
            ADDR + " TEXT, " +
            DATES + " TEXT, " +
            TIMES + " TEXT " +") ";


    private static final String CREATE_CHECKINTABLE = "CREATE TABLE " + TABLE + " (" +
            NAME + " TEXT, " +
            ADDRESS + " TEXT, " +
            LATI + " TEXT, " +
            LONGI + " TEXT, " +
            TIME + " TEXT " +") ";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHECKINTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    //====================================FUNCTIONS RELATED TO THE DATABASE================================================================

    public boolean insertData(String name, String lat, String lng, String address, String date, String time) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(LOC, name);
        contentValues.put(LAT, lat);
        contentValues.put(LON, lng);
        contentValues.put(ADDR, address);
        contentValues.put(DATES,date);
        contentValues.put(TIMES, time);
        long result= db.insert(DB_TABLE,null, contentValues);

        return result!= -1;   //if result -1, data doesnt insert
    }
    public Cursor viewData() {
        SQLiteDatabase db= this.getReadableDatabase();
        String query= "Select * from "+DB_TABLE;
        Cursor cursor= db.rawQuery(query, null);
        return cursor;
    }

    //     DELETING FROM THR DATABASE WHERE------ENTER CONDITION BASED ON WHAT NEEDED
    public boolean delete(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+DB_TABLE+" WHERE "+LOC+" = '"+name+"'";
        Log.d("APPLE", "deleteName: query: "+ query);
        Log.d("ORANGE", "deleteName: Deleting: "+ name +" from Database.");
        db.execSQL(query);
        return false;
    }
    public void delete_table()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+DB_TABLE); //delete all rows in a table
        db.close();
    }

    //     GETTING THE VALUE OF ONE ATTRIBUTE WHERE WE ARE GIVING THE VALUE OF THE CORRESPONING ATTRIBUTE FROM THE TABLE
   /* public Cursor getphone(String name)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+DB_TABLE+" WHERE "+NAME+" = '"+name+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }*/
}