package com.starwanmeigo.xu.gps_wifi_combine;

/**
 * Created by xu on 10.06.15.
 */

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
    private final String LOCATION_VALUE_2 = "locationValue_2";
    private final String LOCATION_VALUE_3 = "locationValue_3";
    private double[] gooLatLng = new double[6];
    private LatLng latLng;
    private LatLng latLng_2;
    private LatLng latLng_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_map);
        //取出Intent中附加的数据

        latLng = getIntent().getExtras().getParcelable(LOCATION_VALUE);
        gooLatLng[0] = latLng.latitude;
        gooLatLng[1] = latLng.longitude;
        latLng_2 = getIntent().getExtras().getParcelable(LOCATION_VALUE_2);
        gooLatLng[2] = latLng_2.latitude;
        gooLatLng[3] = latLng_2.longitude;
        latLng_3 = getIntent().getExtras().getParcelable(LOCATION_VALUE_3);
        gooLatLng[4] = latLng_3.latitude;
        gooLatLng[5] = latLng_3.longitude;
        LatLng currentLatLng_kf = new LatLng(gooLatLng[0],gooLatLng[1]);
        LatLng currentLatLng_wifi = new LatLng(gooLatLng[2],gooLatLng[3]);
        LatLng currentLatLng_gps = new LatLng(gooLatLng[4],gooLatLng[5]);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        GoogleMap googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng_gps, 18));

        // add marker
        googleMap.addMarker(new MarkerOptions().position(currentLatLng_kf).title("kalman fiter"));
        googleMap.addMarker(new MarkerOptions().position(currentLatLng_wifi).title("wifi"));
    }
}
