package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.platzerworld.biergartenfinder.R;

public class PlacePickerActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private final String TAG = "PLACEPICKER_EXERCISE";

    private final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;
    private final int PLACE_PICKER_REQUEST = 1001;

    protected GoogleApiClient mGoogleApiClient;
    private boolean mHaveLocPerm = false;


    // Invoked when the user clicks on the "Trigger the PlacePicker"
    // button in the main activity
    private void pickAPlace() {
        if (mHaveLocPerm) {
            try {
                // TODO: set a default viewport
                // get the bounding area for the Pike Place market
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(47.608670, -122.340033))
                        .include(new LatLng(47.610005, -122.342865))
                        .build();

                // Trigger the PlacePicker, which will then pass the result
                // to the onActivityResult function below
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = builder
                        .setLatLngBounds(bounds)
                        .build(this);

                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }


    // Called from onActivityResult to update the UI with the data
    // for the Place that was selected by the user
    private void updateUI(Place chosenPlace) {
        TextView placeData = (TextView)findViewById(R.id.tvPlaceData);

        String str = chosenPlace.getName() + "\n"
                + chosenPlace.getAddress() + "\n"
                + chosenPlace.getPhoneNumber() + "\n"
                + chosenPlace.getWebsiteUri();

        placeData.setText(str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        // Build the GoogleApiClient and connect to the Places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        findViewById(R.id.btnTriggerPlacePick).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickAPlace();
                    }
                }
        );


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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            // This code is passed when the user has resolved whatever connection
            // problem there was with the Google Play Services library
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;

            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place chosenPlace = PlacePicker.getPlace(this, data);
                    updateUI(chosenPlace);
                }
                break;
        }
    }

    /**
     * Called when the user has been prompted at runtime to grant permissions
     */
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results){
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                mHaveLocPerm = true;
            }
        }
    }

    /**
     * Standard Google Play Services lifecycle callbacks
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Places API");

        // If we're running on API 23 or above, we need to ask permissions at runtime
        // In this case, we need Fine Location access to use Place Detection
        int permCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            mHaveLocPerm = true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            }
            catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        }
        else {
            GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
            gAPI.getErrorDialog(this, result.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection was suspended for some reason");
        mGoogleApiClient.connect();
    }

}
