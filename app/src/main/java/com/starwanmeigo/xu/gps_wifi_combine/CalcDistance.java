package com.starwanmeigo.xu.gps_wifi_combine;

/**
 * Created by xu on 21.04.15.
 */

import static java.lang.Math.pow;
import static java.lang.StrictMath.abs;

public class CalcDistance {
    double d1;
    double d2;
    double d3;
    double d4;
    double d5;
    double rssi1;
    double rssi2;
    double rssi3;
    double rssi4;
    double rssi5;
    //the big differece between CalcDistance.java and CalcDistance_outside.java is here: the parameters A and n are differen t.
    double A = 34.04;
    double n = 2.606;
    double distance [] = new double[5];

    public CalcDistance(double strongestLevel1, double strongestLevel2, double strongestLevel3, double strongestLevel4, double strongestLevel5){
        this.rssi1 = strongestLevel1;
        this.rssi2 = strongestLevel2;
        this.rssi3 = strongestLevel3;
        this.rssi4 = strongestLevel4;
        this.rssi5 = strongestLevel5;
    }

    public double [] getDistance (){
            if (rssi1!=0||rssi2!=0||rssi3!=0||rssi4!=0||rssi5!=0){
                double power1 = (abs(rssi1) - A) / (10 * n);
                d1 = pow(10,power1);
                double power2 = (abs(rssi2) - A) / (10 * n);
                d2 = pow(10,power2);
                double power3 = (abs(rssi3) - A) / (10 * n);
                d3 = pow(10,power3);
                double power4 = (abs(rssi4) - A) / (10 * n);
                d4 = pow(10,power4);
                double power5 = (abs(rssi5) - A) / (10 * n);
                d5 = pow(10,power5);
            }
            else{
                d1 = 0;
                d2 = 0;
                d3 = 0;
                d4 = 0;
                d5 = 0;
            }


        distance [0] = d1;
        distance [1] = d2;
        distance [2] = d3;
        distance [3] = d4;
        distance [4] = d5;

        return distance;
    }
}