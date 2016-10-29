package com.platzerworld.biergartenfinder;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.platzerworld.biergartenfinder.social.flickr.FlickrClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by platzerworld on 29.10.16.
 */

public class BiergartenFinderApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        BiergartenFinderApplication.context = this;

        FlowManager.init(new FlowConfig.Builder(this).build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


    }

    public static FlickrClient getRestClient() {
        return (FlickrClient) FlickrClient.getInstance(FlickrClient.class, BiergartenFinderApplication.context);
    }

}
