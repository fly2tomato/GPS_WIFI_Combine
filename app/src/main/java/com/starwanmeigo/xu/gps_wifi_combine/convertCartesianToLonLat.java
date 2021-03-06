package com.starwanmeigo.xu.gps_wifi_combine;


import com.google.android.gms.maps.model.LatLng;

public class convertCartesianToLonLat {

    public convertCartesianToLonLat(LatLng initLocation, double bearing, double distance){

    }


    public LatLng getDestinationPoint(LatLng initLocation, double bearing, double distance)
    {
        LatLng newLocation;

        double radius = 6371.0; // earth's mean radius in km
        double lat1 = Math.toRadians(initLocation.latitude);//initial point by Latitude
        double lng1 = Math.toRadians(initLocation.longitude);//initial point by Longitude
        double brng = Math.toRadians(bearing);//angle to the unknown target point??
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(0.001*distance/radius) + Math.cos(lat1)*Math.sin(0.001*distance/radius)*Math.cos(brng) );
        double lng2 = lng1 + Math.atan2(Math.sin(brng)*Math.sin(0.001*distance/radius)*Math.cos(lat1), Math.cos(0.001*distance/radius)-Math.sin(lat1)*Math.sin(lat2));
        lng2 = (lng2+Math.PI)%(2*Math.PI) - Math.PI;

        // normalize to -180...+180
        if (lat2 == 0 || lng2 == 0)
        {
            newLocation = new LatLng(0.0,0.0);
        }
        else
        {
            newLocation = new LatLng(Math.toDegrees(lat2), Math.toDegrees(lng2));
        }

        return newLocation;
    };
}