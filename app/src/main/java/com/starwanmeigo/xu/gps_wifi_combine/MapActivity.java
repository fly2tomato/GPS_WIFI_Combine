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
    private double[] gooLatLng = new double[2];
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_map);
        //取出Intent中附加的数据

        latLng =  getIntent().getExtras().getParcelable(LOCATION_VALUE);
        gooLatLng[0] = latLng.latitude;
        gooLatLng[1] = latLng.longitude;
        LatLng currentLatLng = new LatLng(gooLatLng[1],gooLatLng[0]);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        GoogleMap googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));

        // add marker
        googleMap.addMarker(new MarkerOptions().position(currentLatLng));
    }
}
