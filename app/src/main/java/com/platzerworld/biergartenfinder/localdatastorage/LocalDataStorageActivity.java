package com.platzerworld.biergartenfinder.localdatastorage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.platzerworld.biergartenfinder.BiergartenActivity;
import com.platzerworld.biergartenfinder.GooglePlayServicesActivity;
import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.localdatastorage.db.ToursDataSource;
import com.platzerworld.biergartenfinder.localdatastorage.model.Tour;
import com.platzerworld.biergartenfinder.localdatastorage.xml.ToursPullParser;

import java.util.List;

public class LocalDataStorageActivity extends ListActivity {

    public static final String LOGTAG="EXPLORECA";
    public static final String USERNAME="pref_username";
    public static final String VIEWIMAGE="pref_viewimages";

    private SharedPreferences settings;
    private OnSharedPreferenceChangeListener listener;
    private List<Tour> tours;

    ToursDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data_storage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerForContextMenu(getListView());

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                LocalDataStorageActivity.this.refreshDisplay();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        datasource = new ToursDataSource(this);
        datasource.open();

        tours = datasource.findAll();
        if (tours.size() == 0) {
            createData();
            tours = datasource.findAll();
        }

        refreshDisplay();

        Button btnAll = (Button)findViewById(R.id.btnAll);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tours = datasource.findAll();
                refreshDisplay();
            }
        });

        Button btnCheap = (Button)findViewById(R.id.btnCheap);
        btnCheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tours = datasource.findFiltered("price <= 300", "price ASC");
                refreshDisplay();
            }
        });

        Button btnFancy = (Button)findViewById(R.id.btnFancy);
        btnFancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tours = datasource.findFiltered("price >= 1000", "price DESC");
                refreshDisplay();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_localdatastorage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.menu_all:
                tours = datasource.findAll();
                refreshDisplay();
                break;

            case R.id.menu_cheap:
                tours = datasource.findFiltered("price <= 300", "price ASC");
                refreshDisplay();
                break;

            case R.id.menu_fancy:
                tours = datasource.findFiltered("price >= 1000", "price DESC");
                refreshDisplay();
                break;

            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setPreference(View v) {
        Log.i(LOGTAG, "Clicked set");
        //Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent);
    }

    public void refreshDisplay() {
        ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,
                android.R.layout.simple_list_item_1, tours);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }

    private void createData() {
        ToursPullParser parser = new ToursPullParser();
        List<Tour> tours = parser.parseXML(this);

        for (Tour tour : tours) {
            datasource.create(tour);
        }

    }

}

