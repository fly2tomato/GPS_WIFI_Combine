package com.starwanmeigo.xu.gps_wifi_combine;

import android.util.Log;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by xu on 01.06.15.
 */
public class kalmanFilter {
    double x_gps_initial;
    double y_gps_initial;
    double [] x_wifi_array;
    double [] y_wifi_array;
    double uncertain_gps_initial;
    double uncertain_wifi_initial;
    int arraySize = 5;
    double [] x_gps = new double[arraySize];
    double [] y_gps = new double[arraySize];
    double [] x_wifi = new double[arraySize];
    double [] y_wifi = new double[arraySize];
    /*double [] uncertain_gps = new double[arraySize];
    double [] uncertain_wifi = new double[arraySize];*/
    double [] bestResultbyKF = new double[arraySize];
    double [][] X = new double[arraySize][arraySize];
    double [][] Y = new double[arraySize][arraySize];
    double [][] P = new double[arraySize][arraySize];
    double [] KG = new double[arraySize];
    double [] Z = new double[arraySize+1];





    protected kalmanFilter(double gps_x,double gps_y,double [] wifi_x,double [] wifi_y,double gps_uncertain,double wifi_uncertain){
        this.x_gps_initial = gps_x;
        this.y_gps_initial = gps_y;
        this.x_wifi_array = wifi_x;
        this.y_wifi_array = wifi_y;
        this.uncertain_gps_initial = gps_uncertain;
        this.uncertain_wifi_initial = wifi_uncertain;
    }

    protected double kalman_Filter_Process_X(){
        x_wifi[0] = x_wifi_array [0];
        x_wifi[1] = x_wifi_array [1];
        x_wifi[2] = x_wifi_array [2];
        x_wifi[3] = x_wifi_array [3];
        x_wifi[4] = x_wifi_array [4];
        X[0][0] =x_gps_initial;
        P[0][0] =uncertain_gps_initial;
        int i;
        for(i=0;i<arraySize-1;i++){
            X[i+1][i] = X[i][i];
            P[i+1][i] = sqrt(pow(P[i][i],2)+pow(uncertain_wifi_initial,2));
            Z [i+1] = x_wifi [i];
            KG[i+1] = P[i+1][i]/(sqrt(pow(P[i+1][i],2)+pow(uncertain_wifi_initial,2)));
            X[i+1][i+1] = X[i+1][i]+KG[i+1]*(Z[i+1]-X[i+1][i]);
            P[i+1][i+1] =P[i+1][i]*sqrt(1-KG[i+1]);
            Log.i("check the answer:", Double.toString(X[i][i]));
        }

        return X[i][i];
    }

    protected double kalman_Filter_Process_Y() {
        y_wifi[0] = y_wifi_array [0];
        y_wifi[1] = y_wifi_array [1];
        y_wifi[2] = y_wifi_array [2];
        y_wifi[3] = y_wifi_array [3];
        y_wifi[4] = y_wifi_array [4];
        Y[0][0] =y_gps_initial;
        P[0][0] =uncertain_gps_initial;
        int i;
        for(i=0;i<arraySize-1;i++){
            Y[i+1][i] = Y[i][i];
            P[i+1][i] = sqrt(pow(P[i][i],2)+pow(uncertain_wifi_initial,2));
            Z [i+1] = y_wifi [i];
            KG[i+1] = P[i+1][i]/(sqrt(pow(P[i+1][i],2)+pow(uncertain_wifi_initial,2)));
            Y[i+1][i+1] = Y[i+1][i]+KG[i+1]*(Z[i+1]-Y[i+1][i]);
            P[i+1][i+1] =P[i+1][i]*sqrt(1-KG[i+1]);
            Log.i("check the answer:",Double.toString(Y[i][i]));
        }

        return Y[i][i];
    }
}
