package com.platzerworld.biergartenfinder.animation.activitiesandslides;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import com.platzerworld.biergartenfinder.R;

public class ActivityAndSlidesActivity extends AppCompatActivity implements ItemListFragment.ListEventHandler {

    public static final String PRODUCT_ID = "PRODUCT_ID";
    private List<Product> products = DataProvider.productList;
    private AboutFragment aboutFragment;
    private boolean mShowingAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_slides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListFragment fragment = new ItemListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();

//        Display data
        ProductListAdapter adapter = new ProductListAdapter(
                this, R.layout.list_item, products);
        fragment.setListAdapter(adapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_and_slides_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                viewAbout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(ActivityAndSlidesActivity.this, ActivityAndSlidesActivityDetailActivity.class);

        Product product = products.get(position);
        intent.putExtra(PRODUCT_ID, product.getProductId());

        startActivity(intent);
    }

    private void viewAbout() {

        if (mShowingAbout) {
            getFragmentManager().popBackStack();
            mShowingAbout = false;
            return;
        }

        aboutFragment = new AboutFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, aboutFragment)
                .addToBackStack(null)
                .commit();
        mShowingAbout = true;
    }

    @Override
    public void onBackPressed() {
        if (mShowingAbout) {
            getFragmentManager().popBackStack();
            mShowingAbout = false;
        } else {
            super.onBackPressed();
        }
    }
}
