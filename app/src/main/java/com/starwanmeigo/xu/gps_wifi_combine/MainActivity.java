package com.starwanmeigo.xu.gps_wifi_combine;

import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    // 定义LocationManager对象
    final String LOCATION_VALUE = "locationValue";
    final String LOCATION_VALUE_2 = "locationValue_2";
    final String LOCATION_VALUE_3 = "locationValue_3";
    private int gps_size =25;
    private LocationManager locationManager;
    //private MainActivity mActivity;
    private Location mCurrentLocation;
    private LatLng newCurrentLocation;
    private int countSateNum=0;
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
    private double latitude_intime = 0;
    private double longitude_intime = 0;


    //set coordinates of routers
    /*double ap1_x = 0;
    double ap1_y = 4.1868;
    double ap2_x = 2.5916;
    double ap2_y = 6.8637;
    double ap3_x = 5.0574;
    double ap3_y = 4.1868;*/
    double ap1_x = 1.4809;
    double ap1_y = 8.0134;
    double ap2_x = 4.6376;
    double ap2_y = 6.0159;
    double ap3_x = 1.1097;
    double ap3_y = 1.8736;
    //collect 50 data
    private int arraySize = 20;
    private int countNumber = 5;
    double wifiSignalQuality = 0;
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
    double D_1Float = 0;
    double D_2Float = 0;
    double D_3Float = 0;
    double XFloat = 0;
    double YFloat = 0;
    double bestRssi1 = 0;
    double bestRssi2 = 0;
    double bestRssi3 = 0;



    private double accuracy_wifi = 4;

    double[] weightWifi = new double[5];
    double[] weightGps = new  double[5];
    private double []longitude_kf = new double[5];
    private double [] latitude_kf = new double[5];
    private double [] bearing = new double[arraySize];
    private double [] distance = new double[arraySize];


    private String nmeaString = null;
    private String gpggaString = null;
    private double amountOfSatInUsed = 0.0;
    private double hdop = 0.0;
    private double gpsSignalQuality = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout gpsAP = (LinearLayout) findViewById(R.id.gpsAP);
        final LinearLayout wifiAP = (LinearLayout) findViewById(R.id.wifiAP);
        final LinearLayout kfAP = (LinearLayout) findViewById(R.id.kfAP);
        final ProgressBar loadingSpinnerForGPS = (ProgressBar) findViewById(R.id.loadingSpinnerForGPS);
        final ProgressBar loadingSpinnerForWifi = (ProgressBar) findViewById(R.id.loadingSpinnerForWifi);
        /*final TextView sateNum = (TextView) findViewById(R.id.satellitesnumber);*/
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
        Button saveBtn = (Button) findViewById(R.id.savebtn);
        Button gooMap = (Button) findViewById(R.id.googleMap);

        openGPSSettings();
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

                        for (int i = 0; i < gps_size; i++) {
                            gpsSignals = getGpsInfo();
                            longitude_gps_array[i] = gpsSignals[0];
                            latitude_gps_array[i] = gpsSignals[1];
                            accuracy_gps_array[i] = gpsSignals[2];
                            countSateNum = (int) gpsSignals[3];
                            longitude_gps_sum += longitude_gps_array[i];
                            latitude_gps_sum += latitude_gps_array[i];
                            accuracy_gps_sum += accuracy_gps_array[i];

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        longitude_gps_average = longitude_gps_sum / gps_size;
                        latitude_gps_average = latitude_gps_sum / gps_size;
                        accuracy_gps_average = accuracy_gps_sum / gps_size;
                        longitude_gps_average_8 = round(longitude_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);
                        latitude_gps_average_8 = round(latitude_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);
                        accuracy_gps_average_8 = round(accuracy_gps_average, 8, BigDecimal.ROUND_HALF_DOWN);

                        gpsAP.post(new Runnable() {
                            @Override
                            public void run() {

                                gpsAP.setVisibility(View.VISIBLE);
                                loadingSpinnerForGPS.setVisibility(View.GONE);
                                /*sateNum.setText(Integer.toString(countSateNum));*/
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
                        for (int count = 0; count < countNumber; count++) {
                            double rssiLevel_2_sum = 0;
                            double[] routerInfoArray1 = new double[arraySize];
                            double[] routerInfoArray2 = new double[arraySize];
                            double[] routerInfoArray3 = new double[arraySize];

                            // get Router Info for 3 ssids, <arraySize> times
                            for (int i = 0; i < arraySize; i++) {
                                double[] rssiLevel_ = getRouterInfo();
                                routerInfoArray1[i] = rssiLevel_[0];
                                routerInfoArray2[i] = rssiLevel_[1];
                                routerInfoArray3[i] = rssiLevel_[2];
                                try {
                                    Thread.sleep(300);
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
                            for (int i = 0; i < arraySize; i++) {
                                sum1 += routerInfoArray1[i];
                                sum2 += routerInfoArray2[i];
                                sum3 += routerInfoArray3[i];
                                ssid1Log = ssid1Log.concat(Double.toString(routerInfoArray1[i])).concat(" ");
                                ssid2Log = ssid2Log.concat(Double.toString(routerInfoArray2[i])).concat(" ");
                                ssid3Log = ssid3Log.concat(Double.toString(routerInfoArray3[i])).concat(" ");
                            }

                            selectBetterRssi sBR = new selectBetterRssi(routerInfoArray1, routerInfoArray2, routerInfoArray3);

                            final double[] bestRssi = sBR.funktionOfX();
                            bestRssi1 = bestRssi[0];
                            bestRssi2 = bestRssi[1];
                            bestRssi3 = bestRssi[2];

                            //calc distance with rssi


                            CalcSignalQuality calcSignalQuality = new CalcSignalQuality(bestRssi1, bestRssi2, bestRssi3);
                            wifiSignalQuality = calcSignalQuality.getSignalQuality();

                            CalcDistance calcDistance = new CalcDistance(bestRssi1, bestRssi2, bestRssi3);
                            double[] D_Float_ = calcDistance.getDistance();
                            D_1Float = D_Float_[0];
                            D_2Float = D_Float_[1];
                            D_3Float = D_Float_[2];


                            //calc the Location

                            if (D_1Float != 0 || D_2Float != 0 || D_3Float != 0) {
                                calcLocation_leastSquares calclocation = new calcLocation_leastSquares(ap1_x, ap1_y, D_1Float, ap2_x, ap2_y, D_2Float, ap3_x, ap3_y, D_3Float);
                                XFloat = calclocation.getLocationX();
                                YFloat = calclocation.getLocationY();
                            }
                            x_wifi_array[count] = XFloat;
                            y_wifi_array[count] = YFloat;

                            //we should convert Cartesian To Lon and Lat here!
                            distance[count] = Math.sqrt(Math.abs(XFloat * XFloat + YFloat * YFloat));
                            bearing[count] = Math.atan2(YFloat, XFloat);
                            LatLng initLocation = new LatLng(51.0270068, 13.7243968);//this is the original point and we get this data by checking google map
                            convertCartesianToLonLat newLonLat = new convertCartesianToLonLat(initLocation, bearing[count], distance[count]);
                            cartesian_to_latitude[count] = newLonLat.getDestinationPoint(initLocation, bearing[count], distance[count]).latitude;
                            cartesian_to_longitude[count] = newLonLat.getDestinationPoint(initLocation, bearing[count], distance[count]).longitude;

                            x_wifi_array_sum = x_wifi_array[0] + x_wifi_array[1] + x_wifi_array[2] + x_wifi_array[3] + x_wifi_array[4];
                            x_wifi_array_average = x_wifi_array_sum / x_wifi_array.length;
                            y_wifi_array_sum = y_wifi_array[0] + y_wifi_array[1] + y_wifi_array[2] + y_wifi_array[3] + y_wifi_array[4];
                            y_wifi_array_average = y_wifi_array_sum / y_wifi_array.length;
                        }

                        wifiAP.post(new Runnable() {
                            @Override
                            public void run() {
                                wifiAP.setVisibility(View.VISIBLE);
                                loadingSpinnerForWifi.setVisibility(View.GONE);
                                X1_WIFI.setText(Double.toString(round(x_wifi_array[0], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_latitude[0]);
                                X2_WIFI.setText(Double.toString(round(x_wifi_array[1], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_latitude[1]);
                                X3_WIFI.setText(Double.toString(round(x_wifi_array[2], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_latitude[2]);
                                X4_WIFI.setText(Double.toString(round(x_wifi_array[3], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_latitude[3]);
                                X5_WIFI.setText(Double.toString(round(x_wifi_array[4], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_latitude[4]);
                                Y1_WIFI.setText(Double.toString(round(y_wifi_array[0], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_longitude[0]);
                                Y2_WIFI.setText(Double.toString(round(y_wifi_array[1], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_longitude[1]);
                                Y3_WIFI.setText(Double.toString(round(y_wifi_array[2], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_longitude[2]);
                                Y4_WIFI.setText(Double.toString(round(y_wifi_array[3], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_longitude[3]);
                                Y5_WIFI.setText(Double.toString(round(y_wifi_array[4], 8, BigDecimal.ROUND_HALF_DOWN)) + ' ' + cartesian_to_longitude[4]);
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

                //to evaluate the signal quality of GPS and WIFI, during some algorithms the
                // wifiSignalQuality and gpsSignalQuality will be calculated to accomplish that,
                //because of the different intervals of each other, we need to let these two SQ in a
                //same intervall,e.g. [0,1], to do that, we use linear normalise.
                double wifiSignalQualityNorm = 0.0;
                double wifiSignalQualityMin = 0.1;
                double wifiSignalQualityMax = 1.0;
                double gpsSignalQualityNorm = 0.0;
                double gpsSignalQualityMin = -0.5;
                double gpsSignalQualityMax = 0.5;
                double accuracy_wifi_metric = 0.0;
                double accuracy_gps_metric = 0.0;
                double [] factor = {0.9,0.95,1,1.05,1.1};
                longitude_gps = longitude_gps_average;
                latitude_gps = latitude_gps_average;
                accuracy_gps = accuracy_gps_average;

                for(int m=0;m<5;m++){
                    if(wifiSignalQuality != 0 && gpsSignalQuality != 0){
                        wifiSignalQualityNorm = (wifiSignalQuality-wifiSignalQualityMin)/(wifiSignalQualityMax-wifiSignalQualityMin);
                        gpsSignalQualityNorm = (gpsSignalQuality-gpsSignalQualityMin)/(gpsSignalQualityMax-gpsSignalQualityMin);
                        weightWifi[m] = factor[m]*wifiSignalQualityNorm/(wifiSignalQualityNorm+gpsSignalQualityNorm);
                        weightGps[m] = 1-weightWifi[m];
                        accuracy_wifi_metric = accuracy_wifi*(1+(0.5-weightWifi[m]));//if weightWifi is bigger than 0.5, then accuracy_wifi_metric will smaller than original value.
                        accuracy_gps_metric = accuracy_gps*(1+(0.5-weightGps[m]));
                        kalmanFilter KF = new kalmanFilter(longitude_gps,latitude_gps,cartesian_to_longitude,cartesian_to_latitude,accuracy_gps_metric,accuracy_wifi_metric);
                        longitude_kf[m] = KF.kalman_Filter_Process_X();
                        latitude_kf[m] = KF.kalman_Filter_Process_Y();
                    }
                    else if(wifiSignalQuality == 0 && gpsSignalQuality != 0){
                        latitude_kf[m] = latitude_gps;
                        longitude_kf[m] = longitude_gps;
                    }
                    else if(wifiSignalQuality != 0 && gpsSignalQuality == 0){
                        latitude_kf[m] = cartesian_to_latitude[0];
                        longitude_kf[m] = cartesian_to_longitude[0];
                    }
                    else if(wifiSignalQuality == 0 && gpsSignalQuality == 0){
                        Toast.makeText(MainActivity.this,"Not enough Data to locate! ",Toast.LENGTH_SHORT).show();
                    }

                }



                Longitude_KF.setText(Double.toString(round(longitude_kf[2], 8, BigDecimal.ROUND_HALF_DOWN)));
                Latitude_KF.setText(Double.toString(round(latitude_kf[2], 8, BigDecimal.ROUND_HALF_DOWN)));


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText filenumber = (EditText) findViewById(R.id.editfilenumber);
                double longitude_wifi = (cartesian_to_longitude[0]+cartesian_to_longitude[1]+cartesian_to_longitude[2]+cartesian_to_longitude[3]+cartesian_to_longitude[4])/5;
                double latitude_wifi = (cartesian_to_latitude[0]+cartesian_to_latitude[1]+cartesian_to_latitude[2]+cartesian_to_latitude[3]+cartesian_to_latitude[4])/5;
                String fileNum = filenumber.getText().toString();
                String filename ="data"+fileNum+".txt";
                String lon_gps = Double.toString(longitude_gps)+" ";
                String lat_gps = Double.toString(latitude_gps)+" ";
                String lon_wifi = Double.toString(longitude_wifi)+" ";
                String lat_wifi = Double.toString(latitude_wifi)+" ";
                String lon_kf1 = Double.toString(longitude_kf[0])+" ";
                String lon_kf2 = Double.toString(longitude_kf[1])+" ";
                String lon_kf3 = Double.toString(longitude_kf[2])+" ";
                String lon_kf4 = Double.toString(longitude_kf[3])+" ";
                String lon_kf5 = Double.toString(longitude_kf[4])+" ";
                String lat_kf1 = Double.toString(latitude_kf[0])+" ";
                String lat_kf2 = Double.toString(latitude_kf[1])+" ";
                String lat_kf3 = Double.toString(latitude_kf[2])+" ";
                String lat_kf4 = Double.toString(latitude_kf[3])+" ";
                String lat_kf5 = Double.toString(latitude_kf[4])+" ";
                String rssi1 = Double.toString(bestRssi1)+" ";
                String rssi2 = Double.toString(bestRssi2)+" ";
                String rssi3 = Double.toString(bestRssi3)+" ";
                String x_wifi = Double.toString(x_wifi_array_average)+" ";
                String y_wifi = Double.toString(y_wifi_array_average)+" ";
                String dataStream1 = "RSSIs(AP1,AP2,AP3):\n"+rssi1+rssi2+rssi3+"\nLon&Lat of GPS:\n"+lon_gps+lat_gps+"\nLon&Lat of WIFI:\n"+lon_wifi+lat_wifi+"\nX&Y of WIFI:\n"+x_wifi+y_wifi;
                String dataStream2 = "Kalman Filter_1\n"+lon_kf1+lat_kf1;
                String dataStream3 = "\nKalman Filter_2\n"+lon_kf2+lat_kf2;
                String dataStream4 = "\nKalman Filter_3\n"+lon_kf3+lat_kf3;
                String dataStream5 = "\nKalman Filter_4\n"+lon_kf4+lat_kf4;
                String dataStream6 = "\nKalman Filter_5\n"+lon_kf5+lat_kf5;
                String dataStream = dataStream1+dataStream2+dataStream3+dataStream4+dataStream5;

                FileService service = new FileService(getApplicationContext());
                try {
                    service.save(filename,dataStream);
                    Toast.makeText(getApplication(),"data saved",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(),"data fail saved",Toast.LENGTH_LONG).show();
                }
            }
        });

        gooMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向下一个Activity传递数据(使用Intent.putExtras)
                //putExtra("A",B)中，AB为键值对，第一个参数为键名，第二个参数为键对应的值。
                // 顺便提一下，如果想取出Intent对象中的这些值，需要在你的另一个Activity中用getXXXXXExtra方法，
                // 注意需要使用对应类型的方法，参数为键名
                double gooLatLng [] = new double[4];
                gooLatLng [0] = latitude_kf[2];
                gooLatLng [1] = longitude_kf[2];
                gooLatLng [2] = cartesian_to_latitude[0];
                gooLatLng [3] = cartesian_to_longitude[0];
                LatLng latLng = new LatLng(gooLatLng[0],gooLatLng[1]);
                LatLng latLng_2 = new LatLng(gooLatLng[2],gooLatLng[3]);
                LatLng latLng_3 = new LatLng(latitude_intime,longitude_intime);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra(LOCATION_VALUE, latLng);
                intent.putExtra(LOCATION_VALUE_2, latLng_2);
                intent.putExtra(LOCATION_VALUE_3,latLng_3);

                startActivity(intent);
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
        //
        /*GpsStatus gpsStatus = locationManager.getGpsStatus(null);
        //获取默认最大卫星数
        int maxSatellites = gpsStatus.getMaxSatellites();
        //获取卫星
        Iterable<GpsSatellite> iterable=gpsStatus.getSatellites();
        //再次转换成Iterator
        Iterator<GpsSatellite> itrator=iterable.iterator();
        //通过遍历重新整理为ArrayList
        ArrayList<GpsSatellite> satelliteList=new ArrayList<GpsSatellite>();
        //count the number of available satellites as countSateNum

        maxSatellites = gpsStatus.getMaxSatellites();
        satelliteList.clear();
        while (itrator.hasNext() && countSateNum <= maxSatellites) {
            GpsSatellite satellite = itrator.next();
            satelliteList.add(satellite);
            countSateNum++;
        }*/

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
        return new double[]{longitude_gps, latitude_gps, accuracy_gps, countSateNum};
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
                /*Toast.makeText(this, "finding the APs!", Toast.LENGTH_SHORT).show();*/
            }
        }

        double level_ [] = new double[3];
        level_ [0] = level_1;
        level_ [1] = level_2;
        level_ [2] = level_3;

        return level_;
    }
        private void openGPSSettings() {
            LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "GPS is working!", Toast.LENGTH_SHORT).show();
                getLocation();
                return;
        }
        else{
            Toast.makeText(this, "please open GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, R.layout.activity_main); // 此为设置完成后返回到获取界面
            getLocation();
            return;
        }
    }

    private int sateNumbers(){
        int sateNumbers = numSatelliteList.size();
        return sateNumbers;
    }

    //LocationManager locationManager;

    private void getLocation() {
        // 获取位置管理服务

        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        // Criteria criteria = new Criteria();
        // criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // // 高精度
        // criteria.setAltitudeRequired(false);
        // criteria.setBearingRequired(false);
        // criteria.setCostAllowed(true);
        // criteria.setPowerRequirement(Criteria.POWER_LOW);
        // // 低功耗
        // String provider = locationManager.getBestProvider(criteria, true);
        // 获取GPS信息
        String provider = LocationManager.GPS_PROVIDER;

        Location location = locationManager.getLastKnownLocation(provider);// 通过GPS获取位置

        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        updateToNewLocation(location);
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000)或最小位移变化超过N米
        locationManager.requestLocationUpdates(provider, 500, 0,locationListener);
        // 注册状态信息回调
        locationManager.addGpsStatusListener(statusListener);
        //
        locationManager.addNmeaListener(nmealistener);
    }

    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号
    /**
     * 卫星状态监听器
     */
    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            GpsStatus status = locationManager.getGpsStatus(null); // 取当前状态
            updateGpsStatus(event, status);
        }
    };


    //get GPS signal-quality!
    private final GpsStatus.NmeaListener nmealistener = new GpsStatus.NmeaListener() {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            nmeaString = nmea;
                /*Log.d("Tag", "Nmea Received :");*/
            Log.d("TAG", "Timestamp is :" + timestamp + "   nmea is :" + nmea);
            String determinString = nmeaString.substring(0,6);
            String choosedString = "$GPGGA";
            boolean isEqual = determinString.equals(choosedString);
            Log.d("TAG","is equal? "+isEqual);

            if(gpggaString == null){
                if(determinString.equals(choosedString)){
                    if(nmeaString.length()>30){
                        gpggaString = nmeaString;
                        amountOfSatInUsed = Double.valueOf(nmeaString.substring(46,47));
                        hdop = Double.valueOf(nmeaString.substring(48,51));
                    }
                }
            }

            double amountOfSatInView = 6.0;
            double hdop_intial = 1.5;
            gpsSignalQuality = ((amountOfSatInUsed-amountOfSatInView)/amountOfSatInView+(hdop_intial-hdop)/hdop_intial)/2;
        }
    };

    private void updateGpsStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                numSatelliteList.add(s);
                count++;
            }

        }
    }

    private void updateToNewLocation(Location location) {
        // 获取系统时间
        Time t = new Time();
        t.setToNow(); // 取得系统时间
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 24小时制
        int minute = t.minute;
        int second = t.second;
        TextView tv1;
        tv1 = (TextView) this.findViewById(R.id.tv1);
        if (location != null) {
            latitude_intime = location.getLatitude();// 经度
            longitude_intime = location.getLongitude();// 纬度
            double altitude = location.getAltitude(); // 海拔
            tv1.setText("find " + numSatelliteList.size() + " satellites!" + "\nlatitude："
                    + latitude_intime + "\nlongitude：" + longitude_intime + "\naltitude：" + altitude
                    + "\ntime：" + year + "." + month + "." + date + "." + hour
                    + ":" + minute + ":" + second+"\nGPS SignalQuality: "+gpsSignalQuality+"\nWIFI SignalQuality:"+wifiSignalQuality);
        } else {

            tv1.setText("can't get location information!");
        }
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            if (location != null) {
                updateToNewLocation(location);
                /*Toast.makeText(MainActivity.this, "Location is being changed！",
                        Toast.LENGTH_SHORT).show();*/
            }
        }

        public void onProviderDisabled(String provider) {
            // Provider被disable时触发此函数，比如GPS被关闭
            updateToNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
            // Provider被enable时触发此函数，比如GPS被打开
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {// 拦截menu键事件
            // do something...
            System.exit(0);

        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {// 拦截返回按钮事件
            // do something...
            System.exit(0);
        }
        return true;
    }
}


