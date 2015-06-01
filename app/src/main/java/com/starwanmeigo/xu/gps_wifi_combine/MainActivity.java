package com.starwanmeigo.xu.gps_wifi_combine;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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

import java.util.List;


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
    private double [] longitude_gps_array;
    private double [] latitude_gps_array;

    //set coordinates of routers
    double ap1_x = 0;
    double ap1_y = 0.60;
    double ap2_x = 3.15;
    double ap2_y = 2.50;
    double ap3_x = 5.65;
    double ap3_y = 1.30;
    //collect 20 data
    int arraySize = 50;
    private double x_wifi = 0;
    private double y_wifi = 0;
    private double [] x_wifi_array;
    private double [] y_wifi_array;

    private double accuracy_wifi = 4;

    private double longitude_kf = 0;
    private double latitude_kf = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                        for(int count=0;count<5;count++){
                            double rssiLevel_2_sum = 0;
                            double[] routerInfoArray1 = new double[arraySize];
                            double[] routerInfoArray2 = new double[arraySize];
                            double[] routerInfoArray3 = new double[arraySize];

                            // get Router Info for 3 ssids, <arraySize> times
                            for(int i =0;i<arraySize;i++){
                                double [] rssiLevel_  = getRouterInfo();
                                routerInfoArray1[i] = rssiLevel_[0];
                                routerInfoArray2[i] = rssiLevel_[1];
                                routerInfoArray3[i] = rssiLevel_[2];
                                try{
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //deal with the array routerInfoArray[]

                            // deal with the raw data
                            double sum1 = 0;
                            double sum2 = 0;
                            double sum3 = 0;
                            //log info
                            String ssid1Log = new String("SSID1: ");
                            String ssid2Log = new String("SSID2: ");
                            String ssid3Log = new String("SSID3: ");
                            for(int i =0;i<arraySize;i++){
                                sum1 += routerInfoArray1[i];
                                sum2 += routerInfoArray2[i];
                                sum3 += routerInfoArray3[i];
                                ssid1Log = ssid1Log.concat(Double.toString(routerInfoArray1[i])).concat(" ");
                                ssid2Log = ssid2Log.concat(Double.toString(routerInfoArray2[i])).concat(" ");
                                ssid3Log = ssid3Log.concat(Double.toString(routerInfoArray3[i])).concat(" ");
                            }

                            selectBetterRssi sBR = new selectBetterRssi(routerInfoArray1,routerInfoArray2,routerInfoArray3);

                            final double [] bestRssi = sBR.funktionOfX();
                            final double bestRssi1 = bestRssi [0];
                            final double bestRssi2 = bestRssi [1];
                            final double bestRssi3 = bestRssi [2];

                            //calc distance with rssi
                            CalcDistance calcDistance = new CalcDistance(bestRssi1, bestRssi2, bestRssi3);
                            double [] D_Float_ = calcDistance.getDistance();
                            double D_1Float =  D_Float_ [0];
                            double D_2Float =  D_Float_ [1];
                            double D_3Float =  D_Float_ [2];
                            //calc the Location
                            CalcLocation calclocation = new CalcLocation(ap1_x, ap1_y, D_1Float, ap2_x, ap2_y, D_2Float, ap3_x, ap3_y, D_3Float);
                            final double XFloat = calclocation.getLocationX();
                            final double YFloat = calclocation.getLocationY();
                            x_wifi_array [count] = XFloat;
                            y_wifi_array [count] = YFloat;
                            allAP.post(new Runnable() {
                                @Override
                                public void run() {
                                    X_WIFI.setText(Double.toString(XFloat));
                                    Y_WIFI.setText(Double.toString(YFloat));
                                }
                            });
                        }
                    }
                }).start();
                
                //make up a new thread to calculate the Longitude and Latitude with Kalman Filter
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //we should convert Cartesian To Lon and Lat here!

                        //
                        kalmanFilter KF = new kalmanFilter(longitude_gps,latitude_gps, x_wifi_array,y_wifi_array ,accuracy_gps,accuracy_wifi);
                        double X = KF.kalman_Filter_Process_X();
                        double Y = KF.kalman_Filter_Process_Y();
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

    protected double [] getGpsInfo(){
        double longitude_gps_sum = 0;
        double latitude_gps_sum = 0;
        for(int i =0;i<arraySize;i++){
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
                longitude_gps = Double.parseDouble(null);
                latitude_gps = Double.parseDouble(null);
            }

            longitude_gps_array [i] = longitude_gps;
            latitude_gps_array [i] = latitude_gps;

            try{
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<arraySize;i++){
            longitude_gps_sum = longitude_gps_sum + longitude_gps_array [i];
            latitude_gps_sum = latitude_gps_sum + latitude_gps_array [i];
        }

        longitude_gps = longitude_gps_sum/arraySize;
        latitude_gps = latitude_gps_sum/arraySize;

        double [] lon_lat = new double[2];
        lon_lat [0] = longitude_gps;
        lon_lat [1] = latitude_gps;

        return lon_lat;
    }

    protected double [] getRouterInfo(){
        List<ScanResult> wifiList;
        WifiManager wifiManager;
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiList = (List<android.net.wifi.ScanResult>) wifiManager.getScanResults();

        double level [] = new double[wifiList.size()];
        String ssid [] = new String[wifiList.size()];
        double level_1 = 0;
        String ssidName_1 ="ap1";
        double level_2 = 0;
        String ssidName_2 ="ap2";
        double level_3 = 0;
        String ssidName_3 ="ap3";

        for (int i=0;i<wifiList.size();i++) {
            level [i] = wifiList.get(i).level;
            ssid [i] = wifiList.get(i).SSID;
            if (ssid[i].equals(ssidName_1)){
                level_1 = level[i];
            }
            else if (ssid[i].equals(ssidName_2)){
                level_2 = level[i];
            }
            else if (ssid[i].equals(ssidName_3)){
                level_3 = level[i];
            }
            else {
                //Log.d("MainActivity", "finding the APs!" );
                //Toast.makeText(this, "finding the APs!", Toast.LENGTH_SHORT).show();
            }
        }

        double level_ [] = new double[3];
        level_ [0] = level_1;
        level_ [1] = level_2;
        level_ [2] = level_3;

        return level_;
    }



}
