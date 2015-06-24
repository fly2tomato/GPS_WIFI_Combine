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
    double rssi1;
    double rssi2;
    double rssi3;
    //the big differece between CalcDistance.java and CalcDistance_outside.java is here: the parameters A and n are different.
    double A = 34.04;
    double n = 2.606;
    double distance [] = new double[3];

    public CalcDistance(double strongestLevel1, double strongestLevel2, double strongestLevel3){
        this.rssi1 = strongestLevel1;
        this.rssi2 = strongestLevel2;
        this.rssi3 = strongestLevel3;
    }

    public double [] getDistance (){
        if (rssi1!=0||rssi2!=0||rssi3!=0){
            double power1 = (abs(rssi1) - A) / (10 * n);
            d1 = pow(10,power1);
            double power2 = (abs(rssi2) - A) / (10 * n);
            d2 = pow(10,power2);
            double power3 = (abs(rssi3) - A) / (10 * n);
            d3 = pow(10,power3);
        }
        else{
            d1 = 0;
            d2 = 0;
            d3 = 0;
        }


        distance [0] = d1;
        distance [1] = d2;
        distance [2] = d3;

        return distance;
    }
}