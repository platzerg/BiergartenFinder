package com.platzerworld.biergartenfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.platzerworld.biergartenfinder.animation.FramesAndImagesActivity;
import com.platzerworld.biergartenfinder.animation.OjbectPropertiesActivity;
import com.platzerworld.biergartenfinder.animation.ViewAnimationActivity;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Button btnViewAnimation = (Button)findViewById(R.id.btnViewAnimation);
        btnViewAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationActivity.this, ViewAnimationActivity.class);
                startActivity(intent);
            }
        });

        Button btnFramesAndImages = (Button)findViewById(R.id.btnFramesAndImages);
        btnFramesAndImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationActivity.this, FramesAndImagesActivity.class);
                startActivity(intent);
            }
        });

        Button btnObjectProperties = (Button)findViewById(R.id.btnObjectProperties);
        btnObjectProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationActivity.this, OjbectPropertiesActivity.class);
                startActivity(intent);
            }
        });

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

}
