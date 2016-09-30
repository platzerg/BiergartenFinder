package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.platzerworld.biergartenfinder.R;

public class PlaceCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_complete);

        findViewById(R.id.btnTriggerAutoComp).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectAPlace();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private final String TAG = "PLACECOMPLETE_EXERCISE";
    private final int PLACE_AUTOCOMPLETE_REQUEST = 1001;


    // Called when the user clicks the Open Place Autocomplete button
    // in the main activity. Invokes the Autocomplete UI
    protected void selectAPlace() {
        try {
            Intent intent;

            // A filter can be used to restrict the kinds of predictions that
            // the autocomplete widget provides. In this case, the code
            // only displays places that are business establishments
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            // Mode can either be MODE_FULLSCREEN or MODE_OVERLAY
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                    .setBoundsBias(new LatLngBounds.Builder()
//                            .include(new LatLng(47.608670, -122.340033))
//                            .include(new LatLng(47.610005, -122.342865))
//                            .build())
                    .setFilter(filter)
                    .build(this);

            // Start the Autocomplete Activity. the result will be returned
            // in the onActivityResult function
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST);
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    // Updates the TextView in the main Activity with the selected Place details
    private void updateUI(Place chosenPlace) {
        TextView placeData = (TextView)findViewById(R.id.tvPlaceData);

        if (chosenPlace == null) {
            placeData.setText("No place selected");
        }
        else {
            String str = chosenPlace.getName() + "\n"
                    + chosenPlace.getAddress() + "\n"
                    + chosenPlace.getPhoneNumber() + "\n"
                    + chosenPlace.getWebsiteUri();

            placeData.setText(str);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                updateUI(place);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            }
            else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
                updateUI(null);
            }
        }
    }
}

