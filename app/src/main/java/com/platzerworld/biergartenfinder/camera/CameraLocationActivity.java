package com.platzerworld.biergartenfinder.camera;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.platzerworld.biergartenfinder.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraLocationActivity extends AppCompatActivity {
    TextView editTextDisplay;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextDisplay = (TextView) findViewById(R.id.editTextDisplay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }


        Button btnCameraLocation1 = (Button)findViewById(R.id.btnCameraLocation1);
        btnCameraLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createImageFile1();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnCameraLocation2 = (Button)findViewById(R.id.btnCameraLocation2);
        btnCameraLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbumDir();
            }
        });

        Button btnCameraLocation3 = (Button)findViewById(R.id.btnCameraLocation3);
        btnCameraLocation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                editTextDisplay.append("\nfile1" + file1.getAbsolutePath());
                File file2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                editTextDisplay.append("\nfile2" + file2.getAbsolutePath());
                File file3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                editTextDisplay.append("\nfile3" + file3.getAbsolutePath());
                File file4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                editTextDisplay.append("\nfile4" + file4.getAbsolutePath());
                File file5 = Environment.getDataDirectory();
                editTextDisplay.append("\nfile5" + file5.getAbsolutePath());
                File file6 = Environment.getDownloadCacheDirectory();
                editTextDisplay.append("\nfile6" + file6.getAbsolutePath());
                File file7 = Environment.getExternalStorageDirectory();
                editTextDisplay.append("\nfile7" + file7.getAbsolutePath());
                File file8 = Environment.getRootDirectory();
                editTextDisplay.append("\nfile8" + file8.getAbsolutePath());

                File file9 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                editTextDisplay.append("\nfile9" + file9.getAbsolutePath());

                File albumDir = getAlbumDir();
                editTextDisplay.append("\nalbumDir" + albumDir.getAbsolutePath());
                File cacheDir = getCacheDir();
                editTextDisplay.append("\ncacheDir" + cacheDir.getAbsolutePath());
                File filesDir = getFilesDir();
                editTextDisplay.append("\nfilesDir" + filesDir.getAbsolutePath());

            }
        });

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

    private File createImageFile1() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        editTextDisplay.append("\n" + image.getAbsolutePath());
        return image;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("Urlaub 2016");
            editTextDisplay.append("\n" +storageDir.getAbsolutePath());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

}
