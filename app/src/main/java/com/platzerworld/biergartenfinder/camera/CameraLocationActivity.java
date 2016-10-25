package com.platzerworld.biergartenfinder.camera;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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


                String stringUrl = "http://darthtransformers.de:8080/iWorld/api/biergarten/all";
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    editTextDisplay.append("\n No network connection available.");
                }

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

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void traceNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
    }



    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("GPL", "Result: " + result);
        }
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("GPL", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
