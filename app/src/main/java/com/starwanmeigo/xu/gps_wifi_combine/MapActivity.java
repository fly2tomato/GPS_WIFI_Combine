package com.starwanmeigo.xu.gps_wifi_combine;

/**
 * Created by xu on 10.06.15.
 */
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//1111
public class MapActivity extends ActionBarActivity {

    private final String LOCATION_VALUE = "locationValue";
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_map);
        //取出Intent中附加的数据
        mCurrentLocation = getIntent().getExtras().getParcelable(LOCATION_VALUE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        GoogleMap googleMap = mapFragment.getMap();
        //googleMap.setMyLocationEnabled(true);

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));

        // add marker
        googleMap.addMarker(new MarkerOptions().position(currentLatLng));
    }
}
