package com.platzerworld.biergartenfinder.social.flickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.platzerworld.biergartenfinder.BiergartenActivity;
import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.RestActivity;

public class FlickrActivity extends AppCompatActivity {
    private static final int ACTION_FLICKR_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
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

        Button btnLoginToFlickr = (Button)findViewById(R.id.btnLoginToFlickr);
        btnLoginToFlickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlickrActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_FLICKR_LOGIN: {
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("login")) {
                        Toast.makeText(this, data.getExtras().getString("login"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            } // ACTION_TAKE_PHOTO_B
        } // switch
    }
}
