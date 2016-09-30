package com.platzerworld.biergartenfinder.googleplayservices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;



import com.platzerworld.biergartenfinder.R;

public class GoogleDriveActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    private final String TAG = "DRIVE_EXERCISE";
    private final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;
    private final int OPEN_FILE_REQUEST_CODE = 1001;
    private final int CREATE_FILE_REQUEST_CODE = 1002;
    private final int DELETE_FILE_REQUEST_CODE = 1003;

    protected GoogleApiClient mGoogleApiClient;
    TextView tvData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);


        // get the reference to the text view that will be used
        // to display the results of various operations
        tvData = (TextView)findViewById(R.id.tvMsg);

        findViewById(R.id.btnChooseFile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFile();
                    }
                }
        );
        findViewById(R.id.btnCreateFileWithCFAB).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createFileUsingActivity();
                    }
                }
        );
        findViewById(R.id.btnDeleteFile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFileToDelete();
                    }
                }
        );
        findViewById(R.id.btnQueryFile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryFiles();
                    }
                }
        );

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();




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

    // Simple getting-started function that shows how to get some basic
    // information from the Drive API, in this case the root folder ID
    protected void getRootFolderInfo() {
        // TODO: get the root folder info and display its ID
        DriveFolder rootFolder = Drive.DriveApi.getRootFolder(mGoogleApiClient);
        DriveId rootId = rootFolder.getDriveId();

        tvData.setText("Root Folder: " + rootId.toString());
    }

    // Give the user a standard file-picker UI that lets them select a file
    // for your app to operate on. Note that the OpenFileActivityBuilder
    // shows all files, not just the ones that your app has created.
    protected void chooseFile() {
        IntentSender openFileIS;

        // TODO: invoke the file chooser and display the file info
        openFileIS = new OpenFileActivityBuilder()
                .setActivityTitle("Select a File")
                .setMimeType(new String[] {"application/pdf"})
                .build(mGoogleApiClient);

        // This code will open the file picker Activity, and the result will
        // be passed to the onActivityResult function.
        try {
            startIntentSenderForResult(openFileIS, OPEN_FILE_REQUEST_CODE, null, 0,0,0);
        }
        catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Problem starting the OpenFileActivityBuilder");
        }
    }

    // Once the user has chosen a file, this function will display information
    // about the file in the TextView in the Activity
    protected void displayChosenFileData(Intent fileData) {
        // the chosen file is stored as an Extra piece of data in the Intent
        DriveId chosenFileID = fileData
                .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
        // Given a Drive ID, we can convert it to a file reference
        DriveFile theFile = chosenFileID.asDriveFile();

        // Once we have the file reference, we can get the file's metadata
        // TODO: display the file metadata
        theFile.getMetadata(mGoogleApiClient).setResultCallback(
                new ResultCallback<DriveResource.MetadataResult>() {
                    @Override
                    public void onResult(@NonNull DriveResource.MetadataResult metadataResult) {
                        Metadata theData = metadataResult.getMetadata();
                        String str = theData.getTitle() + "\n"
                                + theData.getMimeType() + "\n"
                                + theData.getFileSize() + " bytes\n";
                        tvData.setText(str);
                    }
                }
        );
    }

    // Create a file using the CreateFileActivityBuilder - instantiate
    // new DriveContents and then invoke the callback to set the
    // initial file content and use the Activity to choose a location
    protected void createFileUsingActivity() {
        // Step 1: Create a new DriveContents object and set its callback
        // TODO: Create the new content
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(mNewContentCallback);
    }

    private final ResultCallback<DriveContentsResult> mNewContentCallback =
            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(@NonNull final DriveContentsResult result) {
                    // Step 2: Using the newly created content, set the initial
                    // file metadata and properties and then invoke the
                    // CreateFileWithActivityBuilder

                    // Performing intensive work, such as writing data to a file, should
                    // always happen off of the main UI thread so that your app stays responsive.
                    // This is a very simple example so it just creates a new Thread,
                    // but you can use other methods like an AsyncTask or IntentService for this.
                    new Thread() {
                        @Override
                        public void run() {
                            // Retrieve the output stream from the DriveContents
                            OutputStream outStrm = result.getDriveContents().getOutputStream();
                            OutputStreamWriter outStrmWrt = new OutputStreamWriter(outStrm);

                            try {
                                outStrmWrt.write("This is some file content!");
                                outStrmWrt.close();
                            }
                            catch (IOException e) {
                                Log.e(TAG, "Error writing to file " + e);
                            }

                            // TODO: Create a MetadataChangesetBuilder to change the MIME type
                            MetadataChangeSet newMetaData = new MetadataChangeSet.Builder()
                                    .setMimeType("text/plain")
                                    .build();

                            // TODO: Create the IntentSender to fire off the CreateFileActivity
                            IntentSender intentSender;

                            intentSender = Drive.DriveApi
                                    .newCreateFileActivityBuilder()
                                    .setActivityTitle("Create a new file")
                                    .setInitialMetadata(newMetaData)
                                    .setInitialDriveContents(result.getDriveContents())
                                    .build(mGoogleApiClient);

                            try {
                                startIntentSenderForResult(
                                        intentSender, CREATE_FILE_REQUEST_CODE, null, 0, 0, 0);
                            }
                            catch (IntentSender.SendIntentException e) {
                                Log.w(TAG, "Unable to send intent", e);
                            }
                        }
                    }.start();
                }
            };


    protected void chooseFileToDelete() {
        IntentSender openFileIS = new OpenFileActivityBuilder()
                .setActivityTitle("Select File to Delete")
                .build(mGoogleApiClient);

        try {
            startIntentSenderForResult(openFileIS, DELETE_FILE_REQUEST_CODE, null, 0,0,0);
        }
        catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Problem starting the OpenFileActivityBuilder");
        }
    }

    protected void deleteSelectedFile(DriveId theFileId) {
        // TODO: delete the given file
        DriveResource theResource = theFileId.asDriveResource();

        theResource.trash(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            tvData.setText("File Deleted");
                        }
                    }
                }
        );
    }

    protected void queryFiles() {
        // TODO: Build a sort order
        SortOrder sortOrder = new SortOrder.Builder()
                .addSortDescending(SortableField.TITLE)
                .build();

        // TODO: Create a search query
        Query searchQ = new Query.Builder()
                .addFilter(Filters.contains(SearchableField.TITLE,"file"))
                .setSortOrder(sortOrder)
                .build();

        Drive.DriveApi.query(mGoogleApiClient, searchQ)
                .setResultCallback(
                        new ResultCallback<DriveApi.MetadataBufferResult>() {
                            @Override
                            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                String str = new String();
                                MetadataBuffer results = metadataBufferResult.getMetadataBuffer();
                                for (Metadata md : results) {
                                    str += md.getTitle() + "\n";
                                }
                                tvData.setText(str);

                                results.release();
                            }
                        }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Standard Google Play Services lifecycle callback methods
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Start off by performing a simple operation on the root folder
        getRootFolderInfo();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            }
            catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        }
        else {
            GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
            gAPI.getErrorDialog(this, result.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection was suspended for some reason");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            // This code is passed when the user has resolved whatever connection
            // problem there was with the Google Play Services library
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;

            // This code is passed when the user has selected a file from the
            // OpenFileActivityBuilder
            case OPEN_FILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    displayChosenFileData(data);
                }
                break;

            // This code is passed when the user has selected a filename and
            // folder to create new content in.
            case CREATE_FILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    Log.d(TAG, "File created with ID: " + driveId);
                }
                break;

            case DELETE_FILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    deleteSelectedFile(driveId);
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
