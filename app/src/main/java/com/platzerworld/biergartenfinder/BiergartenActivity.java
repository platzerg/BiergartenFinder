package com.platzerworld.biergartenfinder;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.platzerworld.biergartenfinder.basic.BasicFragmentActivity;
import com.platzerworld.biergartenfinder.googleplayservices.BasicLocationActivity;
import com.platzerworld.biergartenfinder.maps.MapsParentActivity;
import com.platzerworld.biergartenfinder.services.Constants;
import com.platzerworld.biergartenfinder.services.GPSTracker;
import com.platzerworld.biergartenfinder.services.GeocodeService;

public class BiergartenActivity extends AppCompatActivity  {

    private final String TAG = "PlayServicesDemo";

    private TextView txtoutput;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biergarten);


        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            gps.getLatitude(); // returns latitude
            gps.getLongitude(); // returns longitude
            //gps.showSettingsAlert();
            gps.stopUsingGPS();
        }

        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;




         locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        final LocationListener myListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("BiergartenActivity","onLocationChanged");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("BiergartenActivity","onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("BiergartenActivity","onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("BiergartenActivity","onProviderDisabled");
            }
        };

        try {
            Looper myLooper = Looper.myLooper();
            locationManager.requestSingleUpdate(criteria, myListener, null);
            final Handler myHandler = new Handler(myLooper);
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        BiergartenActivity.this.locationManager.removeUpdates(myListener);
                        // myHandler.removeCallbacksAndMessages(null);
                    }catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);


        }catch (SecurityException e) {
            e.printStackTrace();
        }



        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        txtoutput = (TextView) findViewById(R.id.txtoutput);

        Button btnGooglePlayServices = (Button)findViewById(R.id.btnGooglePlayServices);
        btnGooglePlayServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BiergartenActivity.this, GooglePlayServicesActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_biergarten, menu);
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

}
