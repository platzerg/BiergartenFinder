package com.platzerworld.biergartenfinder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobile = false, isWifi = false;

        if (cm.getActiveNetworkInfo()!=null){
            Toast.makeText(context, "Connected to Internet", Toast.LENGTH_LONG).show();

            NetworkInfo[] infoAvailableNetworks = cm.getAllNetworkInfo();

            if (infoAvailableNetworks != null) {
                for (NetworkInfo network : infoAvailableNetworks) {

                    if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (network.isConnected() && network.isAvailable())
                            isWifi = true;
                    }
                    if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                        if (network.isConnected() && network.isAvailable())
                            isMobile = true;
                    }
                }
            }
        }
        else Log.i("INTERNET","---------------------> Internet Disconnected. ");
    }
}
