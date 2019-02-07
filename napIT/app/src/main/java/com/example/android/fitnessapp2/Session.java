package com.example.android.fitnessapp2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aishwarya Srikanth on 12/4/2017.
 */

public class Session {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx) {
        this.ctx= ctx;
        prefs= ctx.getSharedPreferences("napit", Context.MODE_PRIVATE);
        editor= prefs.edit();
    }

    public void setLoggedin(boolean loggedin)
    {
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
    }

    public boolean loggedin()
    {
        return prefs.getBoolean("loggedInmode", false);
    }

    public  void setEmail(String email)
    {
        prefs.edit().putString("email", email).commit();
    }

    public String getEmail()
    {
        String email= prefs.getString("email","");
        return email;
    }

}
