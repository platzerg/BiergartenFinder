package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// Import the Play Services namespaces we will need use the Location API
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.platzerworld.biergartenfinder.R;

public class BasicLocationActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_location);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // get references to the user interface fields
        mLatVal = (TextView)findViewById(R.id.latValue);
        mLongVal = (TextView)findViewById(R.id.longValue);
        mAlt = (TextView)findViewById(R.id.altValue);
        mAccuracy = (TextView)findViewById(R.id.accValue);




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

    private final String TAG = "LOC_SAMPLE";

    // member to hold reference to the Play Services client
    protected GoogleApiClient mGoogleApiClient;

    private TextView mLatVal;
    private TextView mLongVal;
    private TextView mAlt;
    private TextView mAccuracy;

    /**
     * Update the location field values in the Activity with the given
     * values in the supplied Location object
     */
    public void setLocationFields(Location loc) {
        Log.d(TAG, "Updating location fields");
        if (loc != null) {
            mLatVal.setText(String.format("%f", loc.getLatitude()));
            mLongVal.setText(String.format("%f", loc.getLongitude()));

            if (loc.hasAltitude()) {
                mAlt.setText(String.format("%f", loc.getAltitude()));
            }
            if (loc.hasAccuracy()) {
                mAccuracy.setText(String.format("%f", loc.getAccuracy()));
            }
        }
    }

    /**
     * Retrieves the last known location. Assumes that permissions are granted.
     */
    private Location getLocation() {
        try {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return loc;
        }
        catch (SecurityException e) {
            return null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Called when the user has been prompted at runtime to grant permissions
     */
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results){
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                Location locData = getLocation();
                setLocationFields(locData);
            }
        }
    }

    /**
     * Google Play Services Lifecycle methods
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // If we're running on API 23 or above, we need to ask permission at runtime
        int permCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            Location locData = getLocation();
            setLocationFields(locData);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }
}
