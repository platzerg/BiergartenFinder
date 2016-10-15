package com.platzerworld.biergartenfinder.animation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.platzerworld.biergartenfinder.R;

public class FramesAndImagesActivity extends AppCompatActivity {

    private ImageView imageView;
    private AnimationDrawable monkeyAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frames_and_images);

        imageView = (ImageView) findViewById(R.id.animation);
        if (imageView == null) throw new AssertionError();

        imageView.setVisibility(View.INVISIBLE);
        imageView.setBackgroundResource(R.drawable.monkey_animation);

        monkeyAnimation = (AnimationDrawable) imageView.getBackground();
        monkeyAnimation.setOneShot(true);


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

    public void onStartBtnClick(View v) {
        imageView.setVisibility(View.VISIBLE);
        if (monkeyAnimation.isRunning()) {
            monkeyAnimation.stop();
        }
        monkeyAnimation.start();
    }

    public void onStopBtnClick(View v) {
        monkeyAnimation.stop();
    }

}
