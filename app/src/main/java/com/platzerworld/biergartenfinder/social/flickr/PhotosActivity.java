package com.platzerworld.biergartenfinder.social.flickr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.platzerworld.biergartenfinder.BiergartenFinderApplication;
import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.social.flickr.models.FlickrPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends Activity {

    FlickrClient client;

    ArrayList<FlickrPhoto> photoItems;

    GridView gvPhotos;

    PhotoArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flickr_activity_photos);
        client = BiergartenFinderApplication.getRestClient();
        photoItems = new ArrayList<FlickrPhoto>();
        gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        adapter = new PhotoArrayAdapter(this, photoItems);
        gvPhotos.setAdapter(adapter);
        loadPhotos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flickr_photos, menu);
        return true;
    }

    public void loadPhotos() {
        client.getInterestingnessList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    JSONObject json) {
                Log.d("DEBUG", "result: " + json.toString());
                // Add new photos to SQLite
                try {
                    JSONArray photos = json.getJSONObject("photos").getJSONArray("photo");
                    for (int x = 0; x < photos.length(); x++) {
                        String uid = photos.getJSONObject(x).getString("id");
                        FlickrPhoto p = FlickrPhoto.byPhotoUid(uid);
                        if (p == null) {
                            p = new FlickrPhoto(photos.getJSONObject(x));
                        }

                        p.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", e.toString());
                }

                // Load into GridView from DB
                for (FlickrPhoto p : FlickrPhoto.recentItems()) {
                    adapter.add(p);
                }
                Log.d("DEBUG", "Total: " + photoItems.size());
            }
        });
    }

}