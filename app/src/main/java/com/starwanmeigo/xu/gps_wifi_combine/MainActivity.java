package com.starwanmeigo.xu.gps_wifi_combine;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    // 定义LocationManager对象
    /*private final String LOCATION_VALUE = "locationValue";*/
    private int gps_size =25;
    private LocationManager locationManager;
    //private MainActivity mActivity;
    private Location mCurrentLocation;
    private double longitude_gps = 0;
    private double latitude_gps = 0;
    private double longitude_gps_sum = 0;
    private double latitude_gps_sum = 0;
    private double accuracy_gps_sum = 0;
    private double longitude_gps_average = 0;
    private double latitude_gps_average = 0;
    private double accuracy_gps_average = 0;
    private double longitude_gps_average_8 = 0;
    private double latitude_gps_average_8 = 0;
    private double accuracy_gps_average_8 = 0;
    private double speed_gps = 0;
    private double bearing_gps = 0;
    private double accuracy_gps = 0;
    private double [] longitude_gps_array = new double[gps_size];
    private double [] latitude_gps_array = new double[gps_size];
    private double [] accuracy_gps_array = new double[gps_size];
    private double [] gpsSignals = new double[gps_size];

    //set coordinates of routers
    double ap1_x = 0;
    double ap1_y = 4.1868;
    double ap2_x = 2.5916;
    double ap2_y = 6.8637;
    double ap3_x = 5.0574;
    double ap3_y = 4.1868;
    //collect 50 data
    private int arraySize = 20;
    private int countNumber = 5;
    private double x_wifi = 0;
    private double y_wifi = 0;
    private double [] x_wifi_array = new double[arraySize];
    private double [] y_wifi_array = new double[arraySize];
    private double [] cartesian_to_latitude = new double[arraySize];
    private double [] cartesian_to_longitude = new double[arraySize];
    private double x_wifi_array_sum = 0;
    private double y_wifi_array_sum = 0;
    private double x_wifi_array_average = 0;
    private double y_wifi_array_average = 0;


    private double accuracy_wifi = 4;

    private double longitude_kf = 0;
    private double latitude_kf = 0;
    private double [] bearing = new double[arraySize];
    private double [] distance = new double[arraySize];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout gpsAP = (LinearLayout) findViewById(R.id.gpsAP);
        final LinearLayout wifiAP = (LinearLayout) findViewById(R.id.wifiAP);
        final LinearLayout kfAP = (LinearLayout) findViewById(R.id.kfAP);
        final ProgressBar loadingSpinnerForGPS = (ProgressBar) findViewById(R.id.loadingSpinnerForGPS);
        final ProgressBar loadingSpinnerForWifi = (ProgressBar) findViewById(R.id.loadingSpinnerForWifi);
        final TextView Longitude = (TextView) findViewById(R.id.gps_longitude);
        final TextView Latitude = (TextView) findViewById(R.id.gps_latitude);
        final TextView Accuracy = (TextView) findViewById(R.id.gps_accuracy);
        final TextView X1_WIFI = (TextView) findViewById(R.id.wifi_x1);
        final TextView Y1_WIFI = (TextView) findViewById(R.id.wifi_y1);
        final TextView X2_WIFI = (TextView) findViewById(R.id.wifi_x2);
        final TextView Y2_WIFI = (TextView) findViewById(R.id.wifi_y2);
        final TextView X3_WIFI = (TextView) findViewById(R.id.wifi_x3);
        final TextView Y3_WIFI = (TextView) findViewById(R.id.wifi_y3);
        final TextView X4_WIFI = (TextView) findViewById(R.id.wifi_x4);
        final TextView Y4_WIFI = (TextView) findViewById(R.id.wifi_y4);
        final TextView X5_WIFI = (TextView) findViewById(R.id.wifi_x5);
        final TextView Y5_WIFI = (TextView) findViewById(R.id.wifi_y5);
        final TextView Longitude_KF = (TextView) findViewById(R.id.kf_longitude);
        final TextView Latitude_KF = (TextView) findViewById(R.id.kf_latitude);
        Button startBtn = (Button) findViewById(R.id.startbtn);
        Button calcBtn = (Button) findViewById(R.id.calcbtn);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gpsAP.post(new Runnable() {
                            @Override
                            public void run() {
                                gpsAP.setVisibility(View.GONE);
                                loadingSpinnerForGPS.setVisibility(View.VISIBLE);
                            }
                        });
                        for(int i =0;i< gps_size;i++){
                            gpsSignals  = getGpsInfo();
                            longitude_gps_array [i] = gpsSignals[0];
                            latitude_gps_array [i] = gpsSignals[1];
                            accuracy_gps_array [i] = gpsSignals [2];
                            longitude_gps_sum += longitude_gps_array[i];
                            latitude_gps_sum += latitude_gps_array[i];
                            accuracy_gps_sum += accuracy_gps_array[i];

                            try{
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        longitude_gps_average = longitude_gps_sum/gps_size;
                        latitude_gps_average = latitude_gps_sum/gps_size;
                        accuracy_gps_average = accuracy_gps_sum/gps_size;
                        longitude_gps_average_8 = round(longitude_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);
                        latitude_gps_average_8 = round(latitude_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);
                        accuracy_gps_average_8 = round(accuracy_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);

                        gpsAP.post(new Runnable() {
                            @Override
                            public void run() {
                                gpsAP.setVisibility(View.VISIBLE);
                                loadingSpinnerForGPS.setVisibility(View.GONE);
                                Longitude.setText(Double.toString(longitude_gps_average_8));
                                Latitude.setText(Double.toString(latitude_gps_average_8));
                                Accuracy.setText(Double.toString(accuracy_gps_average_8));
                            }
                        });
                    }
                }).start();

                //make up a new thread to calculate the wifiLocation
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wifiAP.post(new Runnable() {
                            @Override
                            public void run() {
                                wifiAP.setVisibility(View.GONE);
                                loadingSpinnerForWifi.setVisibility(View.VISIBLE);
                            }
                        });
                        for(int count=0;count<countNumber;count++){
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

                            //we should convert Cartesian To Lon and Lat here!
                            distance [count] = Math.sqrt(XFloat*XFloat-YFloat*YFloat);
                            bearing [count] = Math.atan2(YFloat,XFloat);
                            LatLng initLocation = new LatLng(51.0270068, 13.7243968);//this is the original point and we get the data by checking google map
                            convertCartesianToLonLat newLonLat = new convertCartesianToLonLat(initLocation, bearing[count], distance[count]);
                            cartesian_to_latitude [count] = newLonLat.getDestinationPoint(initLocation, bearing[count], distance[count]).latitude;
                            cartesian_to_longitude [count] = newLonLat.getDestinationPoint(initLocation,bearing[count], distance[count]).longitude;
                        }

                        wifiAP.post(new Runnable() {
                            @Override
                            public void run() {
                                wifiAP.setVisibility(View.VISIBLE);
                                loadingSpinnerForWifi.setVisibility(View.GONE);
                                X1_WIFI.setText(Double.toString(round(x_wifi_array[0],8,BigDecimal.ROUND_HALF_DOWN)));
                                X2_WIFI.setText(Double.toString(round(x_wifi_array[1],8,BigDecimal.ROUND_HALF_DOWN)));
                                X3_WIFI.setText(Double.toString(round(x_wifi_array[2],8,BigDecimal.ROUND_HALF_DOWN)));
                                X4_WIFI.setText(Double.toString(round(x_wifi_array[3],8,BigDecimal.ROUND_HALF_DOWN)));
                                X5_WIFI.setText(Double.toString(round(x_wifi_array[4],8,BigDecimal.ROUND_HALF_DOWN)));
                                Y1_WIFI.setText(Double.toString(round(y_wifi_array[0],8,BigDecimal.ROUND_HALF_DOWN)));
                                Y2_WIFI.setText(Double.toString(round(y_wifi_array[1],8,BigDecimal.ROUND_HALF_DOWN)));
                                Y3_WIFI.setText(Double.toString(round(y_wifi_array[2],8,BigDecimal.ROUND_HALF_DOWN)));
                                Y4_WIFI.setText(Double.toString(round(y_wifi_array[3],8,BigDecimal.ROUND_HALF_DOWN)));
                                Y5_WIFI.setText(Double.toString(round(y_wifi_array[4],8,BigDecimal.ROUND_HALF_DOWN)));
                            }
                        });
                    }
                }).start();
            }
        });
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calculate the Longitude and Latitude with Kalman Filter
                //we should convert Cartesian To Lon and Lat here!



                //
                longitude_gps = longitude_gps_average;
                latitude_gps = latitude_gps_average;
                accuracy_gps = accuracy_gps_average;
/*
                y_wifi_array = new double[]{51, 53, 54, 50, 52};
                x_wifi_array = new double[]{11, 14 ,15 ,13, 16};
*/
                kalmanFilter KF = new kalmanFilter(longitude_gps,latitude_gps,cartesian_to_longitude,cartesian_to_latitude,accuracy_gps_average,accuracy_wifi);
                final double X = KF.kalman_Filter_Process_X();
                final double Y = KF.kalman_Filter_Process_Y();
                Longitude_KF.setText(Double.toString(round(X, 8, BigDecimal.ROUND_HALF_DOWN)));
                Latitude_KF.setText(Double.toString(round(Y, 8, BigDecimal.ROUND_HALF_DOWN)));


            }
        });

    }

    private double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale,roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    private double [] getGpsInfo() {
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
            longitude_gps = 0.0;
            latitude_gps = 0.0;
            accuracy_gps = 0.0;
        }
        return new double[]{longitude_gps, latitude_gps, accuracy_gps};
    }


    private double [] getRouterInfo(){
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
                Log.d("MainActivity", "finding the APs!" );
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


