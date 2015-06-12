package com.starwanmeigo.xu.gps_wifi_combine;

import android.util.Log;

/**
 * Created by xu on 21.04.15.
 */
public class CalcLocation_2 {
    float X_1;
    float Y_1;
    float D_1;
    float X_2;
    float Y_2;
    float D_2;
    float X_3;
    float Y_3;
    float D_3;

    public CalcLocation_2(float X_1Float, float Y_1Float, float D_1Float, float X_2Float, float Y_2Float, float D_2Flloat,
                          float X_3Float, float Y_3Float, float D_3Float) {
        this.X_1 = X_1Float;
        this.X_2 = X_2Float;
        this.X_3 = X_3Float;
        this.Y_1 = Y_1Float;
        this.Y_2 = Y_2Float;
        this.Y_3 = Y_3Float;
        this.D_1 = D_1Float;
        this.D_2 = D_2Flloat;
        this.D_3 = D_3Float;
    }

    public float[] getPointsByCoordinate(/*float x1, float y1, float d1, float x2, float y2, float d2, float x3, float y3, float d3*/){
        float a = 2*X_2-2*X_1;
        float b = 2*Y_2-2*Y_1;
        float c = (D_1*D_1-D_2*D_2)-(X_1*X_1-X_2*X_2)-(Y_1*Y_1-Y_2*Y_2);
        float d = 2*X_3-2*X_2;
        float e = 2*Y_3-2*Y_2;
        float f = (D_2*D_2-D_3*D_3)-(X_2*X_2-X_3*X_3)-(Y_2*Y_2-Y_3*Y_3);
        float y = (f-d*c/a)/(e-b*d/a);
        float x = (c-b*y)/a;
        Log.d("a:",Float.toString(a));
        Log.d("b:",Float.toString(b));
        Log.d("c:",Float.toString(c));
        Log.d("d:",Float.toString(d));
        Log.d("e:",Float.toString(e));
        Log.d("f:",Float.toString(f));
        Log.d("x:",Float.toString(x));
        Log.d("y:",Float.toString(y));

        float points [] = new float[2];
        points [0] = x;
        points [1] = y;

        return points;

    }

}
