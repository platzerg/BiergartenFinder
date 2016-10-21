package com.platzerworld.biergartenfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.rest.AuthenticatActivity;
import com.platzerworld.biergartenfinder.rest.ParseJSONActivity;
import com.platzerworld.biergartenfinder.rest.ParseXMLActivity;
import com.platzerworld.biergartenfinder.rest.URLConnectionActivity;
import com.platzerworld.biergartenfinder.rest.binary.BinaryActivity;
import com.platzerworld.biergartenfinder.rest.okhttp.OkHTTPActivity;
import com.platzerworld.biergartenfinder.rest.postparam.PostParamActivity;
import com.platzerworld.biergartenfinder.rest.retrofit.RetrofitActivity;

import okhttp3.OkHttpClient;

public class RestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
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

        Button btnURLConnection = (Button)findViewById(R.id.btnURLConnection);
        btnURLConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, URLConnectionActivity.class);
                startActivity(intent);
            }
        });

        Button btnParseXML = (Button)findViewById(R.id.btnParseXML);
        btnParseXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, ParseXMLActivity.class);
                startActivity(intent);
            }
        });

        Button btnParseJSON = (Button)findViewById(R.id.btnParseJSON);
        btnParseJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, ParseJSONActivity.class);
                startActivity(intent);
            }
        });

        Button btnAuthenticate = (Button)findViewById(R.id.btnAuthenticate);
        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, AuthenticatActivity.class);
                startActivity(intent);
            }
        });

        Button btnBinary = (Button)findViewById(R.id.btnBinary);
        btnBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, BinaryActivity.class);
                startActivity(intent);
            }
        });

        Button btnPostParams = (Button)findViewById(R.id.btnPostParams);
        btnPostParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, PostParamActivity.class);
                startActivity(intent);
            }
        });

        Button btnOkHttp = (Button)findViewById(R.id.btnOkHttp);
        btnOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, OkHTTPActivity.class);
                startActivity(intent);
            }
        });

        Button btnRetrofit = (Button)findViewById(R.id.btnRetrofit);
        btnRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestActivity.this, RetrofitActivity.class);
                startActivity(intent);
            }
        });
    }

}
