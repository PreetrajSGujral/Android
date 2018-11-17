package com.example.android.mapsapp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    DataBaseHelper db;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate()
    {
        db= new DataBaseHelper(this);
        listener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.i("Location change", latitude + "," + longitude);
                db.insertMarkerData("SERVICE CHECK IN",latitude+"", longitude+"");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager=(LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*5*60, 100,listener);

    }

}
