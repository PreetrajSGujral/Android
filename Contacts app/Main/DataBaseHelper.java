package com.example.android.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="Table.db";
    private static final String RDB_TABLE="Relation";
    private static final String USER="USER";
    private static final String RELATION="RELATION";

    private static final String CREATE_RTABLE="CREATE TABLE "+RDB_TABLE+" ("+
            USER+" TEXT, "+
            RELATION+" TEXT"+") ";


    //----------------------------------------------------------------------------------------------\\
    private static final String DB_TABLE="Contacts";
    private static final String ID="ID";
    private static final String NAME="NAME";
    private static final String PHONE="PHONE";

    private static final String CREATE_TABLE="CREATE TABLE "+DB_TABLE+" ("+
            NAME+" TEXT, "+
            PHONE+" TEXT"+") ";

 //-------------------------------------------------------------------------------------------------\\
    public DataBaseHelper(Context context){
        super(context,DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_RTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DB_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ RDB_TABLE);
        onCreate(sqLiteDatabase);
    }
    //----------------------------------------------------------------------------------------------\\
    //                                  CONTACTS TABLE FUNCTIONS                                    \\
    //----------------------------------------------------------------------------------------------\\

    public boolean insertData(String name, String phone) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PHONE, phone);
        long result= db.insert(DB_TABLE,null, contentValues);

        return result!= -1;   //if result -1, data doesnt insert
    }


    public Cursor viewData() {
        SQLiteDatabase db= this.getReadableDatabase();
        String query= "Select * from "+DB_TABLE;
        Cursor cursor= db.rawQuery(query, null);
        return cursor;
    }
    public boolean delete(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+DB_TABLE+" WHERE "+NAME+" = '"+name+"'";
        Log.d("APPLE", "deleteName: query: "+ query);
        Log.d("ORANGE", "deleteName: Deleting: "+ name +" from Database.");
        db.execSQL(query);
        return false;
    }
    public Cursor getphone(String name)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+DB_TABLE+" WHERE "+NAME+" = '"+name+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }


    //----------------------------------------------------------------------------------------------\\
    //                                  RELATION TABLE FUNCTIONS                                    \\
    //----------------------------------------------------------------------------------------------\\





    public boolean insertRelation(String name, String relation){
        SQLiteDatabase abc= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(USER, name);
        cv.put(RELATION,relation);
        long result= abc.insert(RDB_TABLE,null, cv);

        return result!=1;
    }

    public Cursor viewRelation(String name)
    {
        SQLiteDatabase abc= this.getWritableDatabase();
        String query="Select * from "+RDB_TABLE+" WHERE "+USER+" = '"+name+"'";
        Cursor c=abc.rawQuery(query, null);
        return c;
    }

    public boolean delete_from_user(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+RDB_TABLE+" WHERE "+USER+" = '"+name+"'";
        Log.d("APPLE", "deleteName: query: "+ query);
        Log.d("ORANGE", "deleteName: Deleting: "+ name +" from Database.");
        db.execSQL(query);
        return false;
    }
    public boolean delete_from_relation(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+RDB_TABLE+" WHERE "+RELATION+" = '"+name+"'";
        Log.d("APPLE", "deleteName: query: "+ query);
        Log.d("ORANGE", "deleteName: Deleting: "+ name +" from Database.");
        db.execSQL(query);
        return false;
    }
}
