package com.platzerworld.biergartenfinder.rest.retrofit;

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


import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.animation.activitiesandslides.ItemListFragment;
import com.platzerworld.biergartenfinder.rest.okhttp.*;
import com.platzerworld.biergartenfinder.rest.retrofit.model.Flower;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;

public class RetrofitActivity extends AppCompatActivity implements ItemListFragment.ListEventHandler, Callback<List<Flower>> {

    // https://inthecheesefactory.com/blog/retrofit-2.0/en
    // https://inthecheesefactory.com/blog/how-to-setup-private-maven-repository/en

    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";
    public static final String ENDPOINT =
            "http://services.hanselandpetal.com";
    TextView output;
    ProgressBar pb;

    List<Flower> flowerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.progressBarRetrofit);
        pb.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                } else {
                    Toast.makeText(RetrofitActivity.this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onListItemClick(int position) {

    }

    private void requestData(String uri) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://services.hanselandpetal.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FlowersAPI flowersAPI = retrofit.create(FlowersAPI.class);

        Call<List<Flower>> call = flowersAPI.getFeed();

        //asynchronous call
        call.enqueue(this);

        // synchronous call would be with execute, in this case you
        // would have to perform this outside the main thread
        // call.execute()

        // to cancel a running request
        // call.cancel();
        // calls can only be used once but you can easily clone them
        //Call<StackOverflowQuestions> c = call.clone();
        //c.enqueue(this);

    }

    protected void updateDisplay() {
        //Use FlowerAdapter to display data
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        ListFragment fragment = new ItemListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container_retrofit, fragment).commit();

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

    @Override
    public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
        flowerList = response.body();
        updateDisplay();
    }

    @Override
    public void onFailure(Call<List<Flower>> call, Throwable t) {
        Toast.makeText(RetrofitActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
