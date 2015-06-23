package com.starwanmeigo.xu.gps_wifi_combine;



import Jama.Matrix;

import static java.lang.Math.pow;

/**
 * Created by xu on 23.06.15.
 */
public class calcLocation_leastSquares {
    double x1;
    double y1;
    double d1;
    double x2;
    double y2;
    double d2;
    double x3;
    double y3;
    double d3;
    double x;
    double y;
    double rnorm;

    public calcLocation_leastSquares(double X_1Float, double Y_1Float, double D_1Float, double X_2Float, double Y_2Float, double D_2Flloat,
                                     double X_3Float, double Y_3Float, double D_3Float){
        this.x1 = X_1Float;
        this.x2 = X_2Float;
        this.x3 = X_3Float;
        this.y1 = Y_1Float;
        this.y2 = Y_2Float;
        this.y3 = Y_3Float;
        this.d1 = D_1Float;
        this.d2 = D_2Flloat;
        this.d3 = D_3Float;
    }

    public Matrix getSolve(){
        double [][] array1 = {{2*(x1-x3),2*(y1-y3)},{2*(x2-x3),2*(y2-y3)}};
        double [][] array2 = {{pow(x1, 2)- pow(x2, 2)+ pow(y1,2)-pow(y3,2)+pow(d3,2)-pow(d1,2)},{pow(x2,2)-pow(x3,2)+pow(y2,2)-pow(y3,2)+pow(d3,2)-pow(d2,2)}};
        Matrix A = new Matrix(array1);
        Matrix b = new Matrix(array2);
        Matrix X = A.solve(b);
        Matrix Residual = A.times(x).minus(b);
        rnorm = Residual.normInf();

        return X;

    }

    public double getLocationX(){
        x = getSolve().get(0,0);
        return x;
    }

    public double getLocationY(){
        y = getSolve().get(1,0);
        return y;
    }


}
