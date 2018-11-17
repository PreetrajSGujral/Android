package com.example.android.mapsapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsSecond extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapSecond";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    View mapView;





    Button save;
    EditText enterplace;

    String date, time, entered_place="";
    ArrayList<String> X;
    ArrayList<String> Y;
    ArrayList<String> place;
    ArrayList<LatLng> MarkerPoints;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private LocationManager locationManager;

    MarkerOptions options;
    DataBaseHelper db;
    Button markedlist;
    LatLng a;


    ArrayList<String> Lat;
    ArrayList<String> Lng;
    ArrayList<String> Place;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setTiltGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

           /* View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(100, 250, 80, 0);
*/

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            // fake_locations();

            set_markers();
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    a= latLng;
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsSecond.this);
                    final View mView = getLayoutInflater().inflate(R.layout.dialog_checkin, null);
                    enterplace = (EditText) mView.findViewById(R.id.customname);
                    save = (Button) mView.findViewById(R.id.save);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            entered_place=enterplace.getText().toString();
                            MarkerOptions options = new MarkerOptions().title(entered_place).position(a).draggable(true);
                            mMap.addMarker(options);
                            dialog.dismiss();
                            Log.i("Marker added","at "+a.latitude+", "+a.longitude);
                            db.insertMarkerData(entered_place,a.latitude+"",a.longitude+"");
                        }
                    });
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng latLng=marker.getPosition();
                    String s=marker.getTitle();
                    db.updateMarkerTable(s,latLng.latitude+"",latLng.longitude+"");
                }
            });


        }
    }





/*



    private void addLocationAlert()
    {
        addGeoLocation(40.49752,-74.4484875);
        addGeoLocation(40.52332895,-74.4588630);
        addGeoLocation(40.5253586, -74.4374398);
        addGeoLocation(40.4791277, -74.4314729796267);
        addGeoLocation(40.48469945, -74.43666649072438);
    }
*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_map);
        db = new DataBaseHelper(this);
        MarkerPoints = new ArrayList<>();
        markedlist = (Button) findViewById(R.id.markedlist);


        Lat= new ArrayList<>();
        Lng= new ArrayList<>();
        Place= new ArrayList<>();
        getLocationPermission();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsSecond.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_inrange, null);
        final TextView mCustomLocation = (TextView) mView.findViewById(R.id.foundinrange);
        mBuilder.setView(mView);
        final AlertDialog dialog1 = mBuilder.create();

       /* if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 2, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Log.i("Location change",latitude+","+longitude);
                    LatLng latLng = new LatLng(latitude, longitude);
                    moveCamera(latLng, DEFAULT_ZOOM);
                    getLocalAddress(location, latitude, longitude);

                    Location current= new Location("curent");
                    current.setLongitude(longitude);
                    current.setLatitude(latitude);


                    int found=0;
                    for (int i = 0; i < Lat.size(); i++) {
                        Location markers = new Location(Place.get(i));
                        markers.setLatitude((Double.valueOf(Lat.get(i))));
                        markers.setLongitude((Double.valueOf(Lng.get(i))));
                        if (current.distanceTo(markers) < 400) {
                            Log.i("Found in range ", "marker number i");
                            mCustomLocation.setText("Found! within marker "+i);
                            dialog1.show();
                            break;
                        }
                        else
                        {
                            Log.i("Not found in any range"," dubara check kar");
                            dialog1.dismiss();
                        }
                    }

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
            });
        }*/
        //IF THE GPS PROVIDER IS WORKING =====> USE GPS PROVIDER
       // else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.i("Location change",latitude+","+longitude);


                    LatLng latLng = new LatLng(latitude, longitude);
                    moveCamera(latLng, DEFAULT_ZOOM);
                    getLocalAddress(location, latitude, longitude);

                    Location current= new Location("curent");
                    current.setLongitude(longitude);
                    current.setLatitude(latitude);


                    int found=0;
                    for (int i = 0; i < Lat.size(); i++) {
                        Location markers = new Location(Place.get(i));
                        markers.setLatitude((Double.valueOf(Lat.get(i))));
                        markers.setLongitude((Double.valueOf(Lng.get(i))));
                        if (current.distanceTo(markers) < 400) {
                            Log.i("Found in range ", "marker number i");
                            mCustomLocation.setText("Found! within marker "+i);
                            dialog1.show();
                            break;
                        }
                        else
                        {
                            Log.i(Place.get(i)+": Not found in any range"," dubara check kar");
                            dialog1.dismiss();
                        }
                    }
                    for (int i = 0; i < X.size(); i++) {
                        Location markers = new Location(place.get(i));
                        markers.setLatitude((Double.valueOf(X.get(i))));
                        markers.setLongitude((Double.valueOf(Y.get(i))));
                        if (current.distanceTo(markers) < 600) {
                            Log.i("Found in range ", "of marker "+place.get(i));
                            mCustomLocation.setText("Found! within marker "+i);
                            dialog1.show();
                            break;
                        }
                        else
                        {
                            Log.i(place.get(i)+": Not found in any range",": distance is: "+current.distanceTo(markers));
                            dialog1.dismiss();
                        }
                    }

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
            });
//        }

        markedlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsSecond.this, MarkerList.class);
                startActivity(i);
            }
        });

    }



    public void set_markers() {
        Cursor cursor = db.viewData();
        Cursor mcursor= db.viewMarkerData();
        X = new ArrayList<>();
        Y = new ArrayList<>();
        place = new ArrayList<>();
        int i = 0;
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                X.add(cursor.getString(1));
                Y.add(cursor.getString(2));
                place.add(cursor.getString(0));
                ++i;
            }
        int j=0;
            if (mcursor.getCount() != 0) {
                while (mcursor.moveToNext()) {
                    Lat.add(mcursor.getString(1));
                    Lng.add(mcursor.getString(2));
                    Place.add(mcursor.getString(0));
                    ++j;
                }
            }

           // latitude.setText(i + "");
            for (int i1 = 0; i1 < i; i1++) {
                MarkerOptions options = new MarkerOptions().title(place.get(i1)).position(new LatLng(Double.valueOf(X.get(i1)), Double.valueOf(Y.get(i1))));
                mMap.addMarker(options);
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(Double.valueOf(X.get(i1)), Double.valueOf(Y.get(i1))))
                        .radius(400)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(5)
                        .fillColor(Color.TRANSPARENT));
            }
            for(int i1=0;i1<j;i1++)
            {
                MarkerOptions options = new MarkerOptions().title(Place.get(i1)).position(new LatLng(Double.valueOf(Lat.get(i1)), Double.valueOf(Lng.get(i1))));
                mMap.addMarker(options);
            }

        }
    }
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            mCurrentLocation = currentLocation;
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            getLocalAddress(currentLocation, currentLocation.getLatitude(), currentLocation.getLongitude());

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsSecond.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }




    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(10)
                .bearing(70)
                .tilt(25)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
       // longitude.setText(("" + latLng.longitude).substring(0, 11));
       // latitude.setText(("" + latLng.latitude).substring(0, 8));

    }
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(MapsSecond.this);
    }
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    public void getLocalAddress(Location currentLocation, double x, double y) {
        Geocoder geocoder;
        String address="";
        try {
            List<Address> addresses;
            geocoder = new Geocoder(MapsSecond.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
           // adres.setText(address);
        } catch (IOException e) {
            Toast.makeText(MapsSecond.this, "address: " + address, Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }


    private void insertcheckin(String place, double latitude, double longitude) {
        Cursor cursor = db.get_address(place);
        String add = "";
        while (cursor.moveToNext()) {
            add = cursor.getString(0);
        }
        Log.i("Got place:","adding "+add);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String date1 = currentDateTimeString.substring(0, 11);
        String time1 = currentDateTimeString.substring(11);
        boolean ad = db.insertCheckinData(place, latitude+"", longitude+"", add, date1, time1);
        Log.i("ADD in Checkin", ad + "");
    }
}
