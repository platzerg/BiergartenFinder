package com.platzerworld.biergartenfinder.googleplayservices;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.platzerworld.biergartenfinder.R;

public class RecurringLocationActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
        View.OnClickListener {

    private final String TAG = "LOC_RECURRING_SAMPLE";

    // Constants that define how often location updates will be delivered
    private final long LOC_UPDATE_INTERVAL = 10000; // in milliseconds
    private final long LOC_FASTEST_UPDATE = 5000; // in milliseconds

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocRequest;
    protected Location mCurLocation;
    private EditText mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_location);


        mLocationList = (EditText)findViewById(R.id.edtLocations);

        // Listen for the Button clicks
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);

        mCurLocation = null;

        // build the Play Services client object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // create the LocationRequest we'll use for location updates
        mLocRequest = new LocationRequest();
        mLocRequest.setInterval(LOC_UPDATE_INTERVAL);
        mLocRequest.setFastestInterval(LOC_FASTEST_UPDATE);
        mLocRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocRequest, this);
        } catch (SecurityException e) {
            Log.i(TAG, "SecurityException");
        }


        // give the location list a slight green tint so we know we're listening
        mLocationList.setBackgroundColor(Color.rgb(204,242,204));
        Log.i(TAG, "Location updates started");
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

        mLocationList.setBackgroundColor(Color.TRANSPARENT);
        Log.i(TAG, "Location updates stopped");
    }

    protected void updateUI() {
        // take the lat and long of the current location object and add it to the list
        if (mCurLocation != null) {
            String lat = String.format("Lat: %f\n", mCurLocation.getLatitude());
            String lon = String.format("Lon: %f\n", mCurLocation.getLongitude());

            // add the new location and autoscroll if needed
            String newText = mLocationList.getText().append(lat).append(lon).toString();
            mLocationList.setText(newText);
            mLocationList.setSelection(newText.length());
        }
    }

    protected void initializeUI() {
        // start by getting the last known location as a starting point
        if (mCurLocation == null) {

            try {
                mCurLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } catch (SecurityException e) {
                Log.i(TAG, "SecurityException");
            }

            // clear the locations list
            mLocationList.setText("");
            updateUI();
        }
    }

    /**
     * Called to handle the button clicks in the view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                startLocationUpdates();
                break;
            case R.id.btnStop:
                stopLocationUpdates();
                break;
        }
    }

    /**
     * Called by Play Services when the user's location changes
     */
    @Override
    public void onLocationChanged(Location loc) {
        mCurLocation = loc;
        updateUI();
    }

    /**
     * On API Level 23 and above, we ask for permissions at runtime. This method is called
     * when the user has approved or denied the permission.
     */
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results){
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                initializeUI();
            }
        }
    }

    /**
     * Google Play Services Lifecycle methods
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Play Services connected");
        int permCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initializeUI();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection was suspended for some reason");
        mGoogleApiClient.connect();
    }

    /**
     * Activity lifecycle events
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}

