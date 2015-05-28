package com.starwanmeigo.xu.gps_wifi_combine;


import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by xu on 17.04.15.
 */
public class CalcLocation {

    double x1;
    double y1;
    double d1;
    double x2;
    double y2;
    double d2;
    double x3;
    double y3;
    double d3;
    /*float X_Y []= new float[2];*/

    //get the date from mainActivity.java
    public CalcLocation(double X_1Float, double Y_1Float, double D_1Float, double X_2Float, double Y_2Float, double D_2Flloat,
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

    //get crosspoints of the two circle funktions
     private double[] getPointsByCoordinate(double x_a, double y_a, double d1, double x_b, double y_b, double d2, double x_c, double y_c){

         double a = x_a*x_a-x_b*x_b;
         double b = x_a-x_b;
         double c = y_a*y_a-y_b*y_b;
         double d = y_a-y_b;
         double e = d1*d1-d2*d2;
         double f = (a+c-e)/(2*b);
         double g = d/b;
         double h = g*g+1;
         double i = -2*f*g+2*g*x_a-2*y_a;
         double j = f*f+x_a*x_a-2*f*x_a+y_a*y_a-d1*d1;
         double det;
         double y_1_cross;
         double y_2_cross;
         double x_1_cross;
         double x_2_cross;
         det =  sqrt(i*i-4*h*j);
         y_1_cross = (-i+det)/(2*h);
         y_2_cross = (-i-det)/(2*h);
         x_1_cross = f-g*y_1_cross;
         x_2_cross = f-g*y_2_cross;
         double points[] = new double[4];
         points[0] = x_1_cross;
         points[1] = y_1_cross;
         points[2] = x_2_cross;
         points[3] = y_2_cross;
         return points;
     }




    //decide which point is more near to located spot
    private double[] getNearPoint(double x_1, double y_1, double x_2, double y_2, double x3, double y3){
        double distanceToc_1 = sqrt(abs((x_1 - x3) * (x_1 - x3) - (y_1 - y3) * (y_1 - y3)));
        double distanceToc_2 = sqrt(abs((x_2-x3)*(x_2-x3)-(y_2-y3)*(y_2-y3)));
        double nearPoint[] = new double[2];

        if (distanceToc_1<distanceToc_2){
            nearPoint[0] = x_1;
            nearPoint[1] = y_1;
        }
        else {
            nearPoint[0] = x_2;
            nearPoint[1] = y_2;
        }
        return nearPoint;
    }



    public double[] calcABPoint(){

        //get 4 crossed points from getPointsByCoordinate() and put them into array points[]
        double points[] = getPointsByCoordinate(x1, y1, d1, x2, y2, d2, x3, y3);
        //choose 2 of the 4 crossed points, which is more near to the center of the 3, circle.
        // points[i] is the crossed points of two circle
        return getNearPoint(points[0],points[1],points[2],points[3],x3, y3);

    }

    public double[] calcBCPoint(){

        double points[] = getPointsByCoordinate(x2, y2, d2, x3, y3, d3, x1, y1);
        return getNearPoint(points[0],points[1],points[2],points[3],x1, y1);
    }

    public double[] calcCAPoint(){

        double points[] = getPointsByCoordinate(x3, y3, d3, x1, y1, d1, x2, y2);
        return getNearPoint(points[0],points[1],points[2],points[3],x2, y2);
    }

    public double getLocationX(){
        double a = x1*x1-x2*x2;
        double b = x1-x2;
        double c = y1*y1-y2*y2;
        double d = y1-y2;
        double e = d1*d1-d2*d2;
        double f = (a+c-e)/(2*b);
        double g = d/b;
        double h = g*g+1;
        double i = -2*f*g+2*g*x1-2*y1;
        double j = f*f+x1*x1-2*f*x1+y1*y1-d1*d1;
        double x_c;
        double x_a;
        double x_b;
        if (i*i-4*h*j>=0){
            x_c = calcABPoint() [0];
            x_a = calcBCPoint() [0];
            x_b = calcCAPoint() [0];
        }
        else {
            x_a = x1;
            x_b = x2;
            x_c = x3;
        }
        double x_average = (x_a+x_b+x_c)/3;
        return x_average;
    }

    public double getLocationY(){
        double a = x1*x1-x2*x2;
        double b = x1-x2;
        double c = y1*y1-y2*y2;
        double d = y1-y2;
        double e = d1*d1-d2*d2;
        double f = (a+c-e)/(2*b);
        double g = d/b;
        double h = g*g+1;
        double i = -2*f*g+2*g*x1-2*y1;
        double j = f*f+x1*x1-2*f*x1+y1*y1-d1*d1;
        double y_c;
        double y_a;
        double y_b;
        if (i*i-4*h*j>=0){
            y_c = calcABPoint() [1];///????????
            y_a = calcBCPoint() [1];
            y_b = calcCAPoint() [1];
        }
        else {
            y_a = y1;
            y_b = y2;
            y_c = y3;

        }
        double y_average = (y_a+y_b+y_c)/3;
        return y_average;
    }
}
