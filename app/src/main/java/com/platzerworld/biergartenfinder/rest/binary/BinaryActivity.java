package com.platzerworld.biergartenfinder.rest.binary;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.animation.activitiesandslides.ItemListFragment;
import com.platzerworld.biergartenfinder.animation.activitiesandslides.ProductListAdapter;
import com.platzerworld.biergartenfinder.rest.binary.model.Flower;
import com.platzerworld.biergartenfinder.rest.binary.parsers.FlowerJSONParser;

public class BinaryActivity extends AppCompatActivity implements ItemListFragment.ListEventHandler{

    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";

    TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Flower> flowerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.binary_progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    requestData("http://services.hanselandpetal.com/secure/flowers.json");
                } else {
                    Toast.makeText(BinaryActivity.this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(BinaryActivity.this, "Postition: " + position, Toast.LENGTH_LONG).show();

    }
    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {
        //Use FlowerAdapter to display data
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        ListFragment fragment = new ItemListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.binary_fragment_container, fragment).commit();

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

    private class MyTask extends AsyncTask<String, String, List<Flower>> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... params) {

            String content = HttpManager.getData(params[0], "feeduser", "feedpassword");
            flowerList = FlowerJSONParser.parseFeed(content);

            return flowerList;
        }

        @Override
        protected void onPostExecute(List<Flower> result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(BinaryActivity.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

            flowerList = result;
            updateDisplay();

        }

    }

}
