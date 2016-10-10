package com.platzerworld.biergartenfinder.googlemaps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.platzerworld.biergartenfinder.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera 48.185960, 11.619992
        LatLng home = new LatLng(48.180086, 11.592196);
        LatLng aumeister = new LatLng(48.185960, 11.619992);
        mMap.addMarker(new MarkerOptions().position(home).title("Marker Home"));
        mMap.addMarker(new MarkerOptions().position(aumeister).title("Marker Aumeister"));


        //LatLngBounds.Builder bc = new LatLngBounds.Builder();
        //bc.include(aumeister);
        //bc.include(home);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 200));

        // Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(aumeister)      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
}
