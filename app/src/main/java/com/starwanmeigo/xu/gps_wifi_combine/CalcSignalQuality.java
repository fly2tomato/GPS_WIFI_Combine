package com.starwanmeigo.xu.gps_wifi_combine;

/**
 * Created by xu on 21.07.15.
 */
public class CalcSignalQuality {
    double rssi1;
    double rssi2;
    double rssi3;
    double rssi4;
    double rssi5;
    double p = -14.28;
    double q = 5.72;
    public CalcSignalQuality(double rssi1, double rssi2, double rssi3, double rssi4, double rssi5){
        this.rssi1 = rssi1;
        this.rssi2 = rssi2;
        this.rssi3 = rssi3;
        this.rssi4 = rssi4;
        this.rssi5 = rssi5;
    }

    public double getSignalQuality(){
        double signalQuality1 = calcSG(rssi1);
        double signalQuality2 = calcSG(rssi2);
        double signalQuality3 = calcSG(rssi3);
        double signalQuality4 = calcSG(rssi4);
        double signalQuality5 = calcSG(rssi5);

        return (signalQuality1+signalQuality2+signalQuality3+signalQuality4+signalQuality5)/5;
    }

    private double calcSG(double rssi) {
        double signalQuality = 0;
        if (rssi == 0) {
            return signalQuality;
        }
        else{
            signalQuality = p/(rssi+q);

            return signalQuality;
        }

    }

}
