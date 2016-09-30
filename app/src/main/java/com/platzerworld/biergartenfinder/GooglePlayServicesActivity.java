package com.platzerworld.biergartenfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.platzerworld.biergartenfinder.googleplayservices.BasicLocationActivity;
import com.platzerworld.biergartenfinder.googleplayservices.CurrentPlaceActivity;
import com.platzerworld.biergartenfinder.googleplayservices.GCMEActivity;
import com.platzerworld.biergartenfinder.googleplayservices.GoogleDriveActivity;
import com.platzerworld.biergartenfinder.googleplayservices.GoogleSignInActivity;
import com.platzerworld.biergartenfinder.googleplayservices.PlaceCompleteActivity;
import com.platzerworld.biergartenfinder.googleplayservices.PlacePickerActivity;
import com.platzerworld.biergartenfinder.googleplayservices.RecurringLocationActivity;

public class GooglePlayServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_play_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnBasicLocation = (Button)findViewById(R.id.btnBasicLocation);
        btnBasicLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, BasicLocationActivity.class);
                startActivity(intent);
            }
        });

        Button btnRecurringLocation = (Button)findViewById(R.id.btnRecurringLocation);
        btnRecurringLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, RecurringLocationActivity.class);
                startActivity(intent);
            }
        });

        Button btnGoogleSignIn = (Button)findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        Button btnGoogleDrive = (Button)findViewById(R.id.btnGoogleDrive);
        btnGoogleDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, GoogleDriveActivity.class);
                startActivity(intent);
            }
        });

        Button btnCurrentPlace = (Button)findViewById(R.id.btnCurrentPlace);
        btnCurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, CurrentPlaceActivity.class);
                startActivity(intent);
            }
        });

        Button btnCompletedPlace = (Button)findViewById(R.id.btnCompletedPlace);
        btnCompletedPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, PlaceCompleteActivity.class);
                startActivity(intent);
            }
        });

        Button btnPlacePicker = (Button)findViewById(R.id.btnPlacePicker);
        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, PlacePickerActivity.class);
                startActivity(intent);
            }
        });

        Button btnGCME = (Button)findViewById(R.id.btnGCME);
        btnGCME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglePlayServicesActivity.this, GCMEActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
