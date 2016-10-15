package com.platzerworld.biergartenfinder.animation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.platzerworld.biergartenfinder.R;

public class TransitionActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    int mCurrentScene = 1;
    private Scene mScene1, mScene2;
    private ViewGroup mSceneRoot;
    private TransitionManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        mScene1 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene1, this);
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.scene2, this);
        mManager = TransitionInflater.from(this)
                .inflateTransitionManager(R.transition.manager, mSceneRoot);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transition_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_transition) {
            runTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runTransition() {
        if (mCurrentScene == 1) {
            TransitionManager.go(mScene2);
            mCurrentScene = 2;
        } else {
            mManager.transitionTo(mScene1);
            mCurrentScene = 1;
        }
    }

}
