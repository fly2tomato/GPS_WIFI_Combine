package com.starwanmeigo.xu.gps_wifi_combine;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    // 定义LocationManager对象
    private final String LOCATION_VALUE = "locationValue";
    private LocationManager locationManager;
    //private MainActivity mActivity;
    private Location mCurrentLocation;
    private double longitude_gps = 0;
    private double latitude_gps = 0;
    private double speed_gps = 0;
    private double bearing_gps = 0;
    private double accuracy_gps = 0;
    private double x_wifi = 0;
    private double y_wifi = 0;
    private double longitude_kf = 0;
    private double latitude_kf = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout allAP = (LinearLayout) findViewById(R.id.allAP);
        final ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
        final TextView Longitude = (TextView) findViewById(R.id.gps_longitude);
        final TextView Latitude = (TextView) findViewById(R.id.gps_latitude);
        final TextView Accuracy = (TextView) findViewById(R.id.gps_accuracy);
        final TextView X_WIFI = (TextView) findViewById(R.id.wifi_x);
        final TextView Y_WIFI = (TextView) findViewById(R.id.wifi_y);
        final TextView Longitude_KF = (TextView) findViewById(R.id.kf_longitude);
        final TextView Latitude_KF = (TextView) findViewById(R.id.kf_latitude);
        Button startBtn = (Button) findViewById(R.id.startbtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make up a new thread to get gpsInformation
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //update UI
                        allAP.post(new Runnable() {
                            @Override
                            public void run() {
                                allAP.setVisibility(View.GONE);
                                loadingSpinner.setVisibility(View.VISIBLE);
                            }
                        });

                        // 获取系统LocationManager服务
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // 从GPS获取最近的定位信息
                        mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                        if(mCurrentLocation != null){
                            longitude_gps = mCurrentLocation.getLongitude();
                            latitude_gps = mCurrentLocation.getLatitude();
                            speed_gps = mCurrentLocation.getSpeed();
                            bearing_gps = mCurrentLocation.getBearing();
                            accuracy_gps = mCurrentLocation.getAccuracy();
                            Log.d("Longitude","Longitude:"+longitude_gps);
                            Log.d("Latitude","Latitude:"+latitude_gps);
                            Log.d("speed","speed:"+speed_gps);
                            Log.d("bearing","bearing:"+bearing_gps);
                            Log.d("accuracy","accuracy:"+accuracy_gps);
                        }else {
                            Longitude.setText(null);
                            Latitude.setText(null);
                        }


                        //update UI
                        allAP.post(new Runnable() {
                            @Override
                            public void run() {
                                allAP.setVisibility(View.VISIBLE);
                                loadingSpinner.setVisibility(View.GONE
                                );
                                Longitude.setText(Double.toString(longitude_gps));
                                Latitude.setText(Double.toString(latitude_gps));
                                Accuracy.setText(Double.toString(accuracy_gps));

                            }
                        });
                    }
                }).start();

                //make up a new thread to calculate the wifiLocation
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        allAP.post(new Runnable() {
                            @Override
                            public void run() {
                                X_WIFI.setText(Double.toString(x_wifi));
                                Y_WIFI.setText(Double.toString(y_wifi));
                            }
                        });
                    }
                }).start();
                
                //make up a new thread to calculate the Longitude and Latitude with Kalman Filter
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        allAP.post(new Runnable() {
                            @Override
                            public void run() {
                                Longitude_KF.setText(Double.toString(longitude_kf));
                                Latitude_KF.setText(Double.toString(latitude_kf));
                            }
                        });
                    }
                }).start();
            }
        });
    }




}
