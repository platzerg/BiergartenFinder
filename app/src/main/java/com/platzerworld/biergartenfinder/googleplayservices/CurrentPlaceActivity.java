package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.platzerworld.biergartenfinder.R;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.List;

public class CurrentPlaceActivity extends AppCompatActivity  implements OnConnectionFailedListener, ConnectionCallbacks{
        private final String TAG = "PlacesExercise";
        private final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;

        private GoogleApiClient mGoogleApiClient;
        private boolean mHaveLocPerm = false;

        // Use this variable to determine the most likely place where the
        // user's device is currently located
        private PlaceLikelihood mostLikelyPlace = null;

        // Called when the user clicks on the Get Current Place Information
        // button in the main Activity UI. This function calls the
        // getCurrentPlace API and loops through the list of results to
        // determine which place the user is most likely located in
    protected void getCurrentPlaceData() {
        mostLikelyPlace = null;

        if (mHaveLocPerm) {
            try {
                // getCurrentPlace, like many other Play Services APIs, returns
                // a PendingResult, which is then handled by setting a callback
                PendingResult<PlaceLikelihoodBuffer> result;
                // TODO: Call getCurrentPlace
                result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient,null);

                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {

                    // the onResult function of the callback is given a list of
                    // PlaceLikelihood objects, from which we can determine what places
                    // the user is near and mostly likely located
                    @Override
                    public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                        for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
                            // Log out the place name and likelihood
                            Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));

                            // TODO: Determine the most likely place where the device is
                            if (mostLikelyPlace == null) {
                                mostLikelyPlace = placeLikelihood;
                            }
                            else {
                                // if this place is more likely than the current one, store
                                // it in our holding variable.
                                if (placeLikelihood.getLikelihood() > mostLikelyPlace.getLikelihood()) {
                                    mostLikelyPlace = placeLikelihood;
                                }
                            }
                        }

                        // if we've found a most likely place, get some information about it and
                        // set the content of the TextView in the Activity
                        if (mostLikelyPlace != null) {
                            Place tempPlace = mostLikelyPlace.getPlace();
                            String str = tempPlace.getName() + "\n"
                                    + tempPlace.getAddress() + "\n"
                                    + tempPlace.getPhoneNumber() + "\n";

                            if (tempPlace.getWebsiteUri() != null)
                                str += tempPlace.getWebsiteUri() + "\n";

                            List<Integer> typeList = tempPlace.getPlaceTypes();
                            str += "Types: " + typeList.toString();

                            TextView tvPlace = (TextView)findViewById(R.id.tvPlaceInfo);
                            tvPlace.setText(str);
                        }

                        // According to the Places API docs, we must display attributions
                        // to third parties if their content is used. This method retrieves
                        // any attributions, which are in HTML format. If there are any,
                        // they are displayed in a small TextView.
                        final CharSequence attribs = placeLikelihoods.getAttributions();
                        if (attribs != null && attribs.length() > 0) {
                            String attrStr = attribs.toString();
                            TextView tvAttribs = (TextView)findViewById(R.id.tvAttributions);
                            tvAttribs.setText(Html.fromHtml(attrStr));
                            // make the link clickable
                            tvAttribs.setMovementMethod(LinkMovementMethod.getInstance());
                        }

                        // IMPORTANT: always release the result buffer to avoid memory leaks
                        placeLikelihoods.release();
                    }
                });
            }
            catch (SecurityException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_place);

        findViewById(R.id.btnGetCurPlace).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        getCurrentPlaceData();
                    }
                });

        // Build the GoogleApiClient and connect to the Places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


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
            // If there was a problem connecting to the Play Services library and
            // we were able to resolve it, this code will passed in and
            // we can then connect to the library
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
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
     * Standard Google Play Services lifecycle callback methods
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

        // Check to see if there's a way to resolve this problem. Usually the user can just
        // update Play Services and be on their way.
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            }
            catch (IntentSender.SendIntentException e) {
                // Unable to resolve - use this opportunity to message user appropriately
                Log.e(TAG, "Could not connect to Play Services");
            }
        }
        else {
            // If there's no resolution available to the problem, give the user an error dialog
            // (which will typically tell them how to fix what's wrong)
            GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
            gAPI.getErrorDialog(this, result.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection was suspended for some reason: " + cause);
        mGoogleApiClient.connect();
    }
}
