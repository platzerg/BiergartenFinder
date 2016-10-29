package com.platzerworld.biergartenfinder.social.flickr;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

public class FlickrClient extends OAuthBaseClient {

    // https://www.flickr.com/services/api/
    // https://www.flickr.com/services/apps/72157675808307785/
    // https://www.flickr.com/services/apps/72157675808307785/key/

    public static final Class<? extends Api> REST_API_CLASS = FlickrApi.class;

    public static final String REST_URL = "https://www.flickr.com/services";

    public static final String REST_CONSUMER_KEY = "550d0b6b5100f3500be922f46952abe6";

    public static final String REST_CONSUMER_SECRET = "7bf26d1376f58a11";

    public static final String REST_CALLBACK_URL = "oauth://cprest";

    public FlickrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
        setBaseUrl("https://api.flickr.com/services/rest");
    }

    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        // https://www.flickr.com/services/api/
        //String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.interestingness.getList");
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&user_id=61271948@N02&method=flickr.people.getPhotos");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
        // https://api.flickr.com/services/rest/?&format=json&nojsoncallback=1&method=flickr.interestingness.getList

        // flickr.people.getPhotos
    }
}
