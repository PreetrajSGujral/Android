package com.example.android.mapsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    String address = "a";
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private TextView longitude, latitude, adres;
    private Button checkin, show;
    public boolean ismain=false;
    View mapView;
    Circle circle;

    String date, time;
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
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(100, 250, 80, 0);

            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);


            set_markers();

            red_line();

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        adres = (TextView) findViewById(R.id.address);
        checkin=(Button) findViewById(R.id.checkin);
        show=(Button) findViewById(R.id.show_checkin);
        db= new DataBaseHelper(this);
        MarkerPoints= new ArrayList<>();

        getLocationPermission();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10000, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    moveCamera(latLng, DEFAULT_ZOOM);
                    getLocalAddress(location, latitude, longitude);
                    ismain=Looper.getMainLooper().isCurrentThread();
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
        }
        //IF THE GPS PROVIDER IS WORKING =====> USE GPS PROVIDER
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10000, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    moveCamera(latLng, DEFAULT_ZOOM);
                    getLocalAddress(location, latitude, longitude);

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
        }


        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder= new AlertDialog.Builder(MapsActivity.this);
                final View mView= getLayoutInflater().inflate(R.layout.dialog_checkin, null);
                final EditText mCustomLocation=(EditText)mView.findViewById(R.id.customname);
                Button mSave=(Button)mView.findViewById(R.id.save);
                mBuilder.setView(mView);
                final  AlertDialog dialog =mBuilder.create();
                dialog.show();
                mSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insert_data(mCustomLocation.getText().toString());

                        dialog.dismiss();
                    }
                });
            }
        });



    }






    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyAWNScQMQ44iOD2hzRfKoHCYCXRW_nz9UU";


        return url;
    }
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            Log.d("Result size",""+result.size());
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }





















    public void insert_data(String location)
        {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            date=currentDateTimeString.substring(0,11);
            time=currentDateTimeString.substring(11);
            String x=latitude.getText().toString();
            String y=longitude.getText().toString();
            String address= adres.getText().toString();
            final boolean b = db.insertData(location, x, y, address, date, time);
            latitude.setText(b+"");
        }
        public void set_markers()
        {
            Cursor cursor= db.viewData();
            X=new ArrayList<>();
            Y=new ArrayList<>();
            place=new ArrayList<>();
            int i=0;
            if(cursor.getCount()!=0) {
                while (cursor.moveToNext()) {
                    X.add(cursor.getString(1));
                    Y.add(cursor.getString(2));
                    place.add(cursor.getString(0));
                    ++i;
                }

                latitude.setText(i + "");
                for (int i1 = 0; i1 < i; i1++) {
                    MarkerOptions options = new MarkerOptions().title(place.get(i1)).position(new LatLng(Double.valueOf(X.get(i1)), Double.valueOf(Y.get(i1))));
                    mMap.addMarker(options);
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.valueOf(X.get(i1)), Double.valueOf(Y.get(i1))))
                            .radius(300)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(5)
                            .fillColor(Color.TRANSPARENT));
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
                                Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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
            longitude.setText(("" + latLng.longitude).substring(0, 11));
            latitude.setText(("" + latLng.latitude).substring(0, 8));

        }

        private void initMap() {
            Log.d(TAG, "initMap: initializing map");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapView=mapFragment.getView();
            mapFragment.getMapAsync(MapsActivity.this);
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


        // GEOCODER AND ADDRESS
        public void getLocalAddress(Location currentLocation, double x, double y) {
            Geocoder geocoder;
            try {
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                adres.setText(address);
            } catch (IOException e) {
                Toast.makeText(MapsActivity.this, "address: " + address, Toast.LENGTH_LONG);
                e.printStackTrace();
            }
        }

        //called from xml.......to call activity to show list of checkins
        public void viewlist(View view)
        {
            Intent i= new Intent(this,Checkin.class );
            startActivity(i);
        }

        private void red_line()
        {
            for(int i=0;i<X.size()-1;i++) {
                LatLng origin = new LatLng(Double.valueOf(X.get(i)), Double.valueOf(Y.get(i)));
                LatLng dest = new LatLng(Double.valueOf(X.get(i+1)), Double.valueOf(Y.get(i+1)));
                String url = getUrl(origin, dest);
                FetchUrl fetchUrl = new FetchUrl();
                fetchUrl.execute(url);
            }

            LatLng origin = new LatLng(Double.valueOf(X.get(X.size()-1)), Double.valueOf(Y.get(X.size()-1)));
            LatLng dest = new LatLng(Double.valueOf(X.get(0)), Double.valueOf(Y.get(0)));
            String url = getUrl(origin, dest);
            FetchUrl fetchUrl = new FetchUrl();
            fetchUrl.execute(url);
        }

    }





    /*public void fake_locations()
        {
            db.insertData("Busch", "40.52332895", "-74.4588630","Busch Campus Center, 604 Bartholomew Rd, Piscataway Township, NJ 08854","Nov 4, 2018","01:33 AM");
            db.insertData("Livi", "40.5253586", "-74.4374398","Livingston Apartments BUilding B, Piscataway Township, NJ 08854","Nov 4, 2018","01:54 AM");
            db.insertData("Cook", "40.4791277", "-74.4314729796267","Cook Campus Center, New Brunswick, NJ 08901","Nov 4, 2018","02:16 AM");
            db.insertData("Douglass", "40.48469945", "-74.43666649072438","Douglass Campus Center, 100 George St, New Brunswick, NJ 08901","Nov 4, 2018","03:33 AM");

        }*/
