package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.platzerworld.biergartenfinder.R;

public class GCMEActivity extends AppCompatActivity {

    private final String TAG = "GCM_EXERCISE_MAIN";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean mRegistered;
    protected RegistrationReceiver mRegReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcme);

        // subclass of ResultReceiver that indicates when registration
        // has taken place
        mRegReceiver = new RegistrationReceiver(new Handler());

        if (checkPlayServices()) {
            Intent intent = new Intent(this, GcmRegisterService.class);
            intent.putExtra(Constants.KEY_REGISTRATION_RECEIVER, mRegReceiver);
            startService(intent);
        }


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
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    class RegistrationReceiver extends ResultReceiver {
        public RegistrationReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            // regardless of result, hide the progress bar
            pb.setVisibility(ProgressBar.GONE);

            mRegistered = resultData.getBoolean(Constants.KEY_REGISTRATION_COMPLETE);

            TextView status = (TextView)findViewById(R.id.tvGcmStatus);
            if (mRegistered) {
                status.setText(getString(R.string.reg_success));
            }
            else {
                status.setText(getString(R.string.reg_error));
            }
        }
    }
}
