package com.platzerworld.biergartenfinder.rest.postparam;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.platzerworld.biergartenfinder.R;

public class PostParamActivity extends AppCompatActivity {

    TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_param);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //		Initialize the TextView for vertical scrolling
        output = (TextView) findViewById(R.id.textViewPostParam);
        output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBarPostParam);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                } else {
                    Toast.makeText(PostParamActivity.this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void requestData(String uri) {

        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String result) {
        output.append(result + "\n");
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

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            updateDisplay(result);

        }

    }

}
