package com.platzerworld.biergartenfinder.rest.volley;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.animation.activitiesandslides.ItemListFragment;
import com.platzerworld.biergartenfinder.rest.retrofit.*;
import com.platzerworld.biergartenfinder.rest.volley.model.Flower;
import com.platzerworld.biergartenfinder.rest.volley.parsers.FlowerJSONParser;

public class VolleyActivity extends AppCompatActivity implements ItemListFragment.ListEventHandler{

    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";

    TextView output;
    ProgressBar pb;

    List<Flower> flowerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.progressBarVolley);
        pb.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                } else {
                    Toast.makeText(VolleyActivity.this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onListItemClick(int position) {

    }

    private void requestData(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        flowerList = FlowerJSONParser.parseFeed(response);
                        updateDisplay();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(VolleyActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    protected void updateDisplay() {
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        ListFragment fragment = new ItemListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container_volley, fragment).commit();

        fragment.setListAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
