package com.starwanmeigo.xu.gps_wifi_combine;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by xu on 05.05.15.
 */
public class selectBetterRssi {
    int arraySize = 50;
    /*double possibility = 0.03;*/
    double[] collectedRssi1 = new double[arraySize];
    double[] collectedRssi2 = new double[arraySize];
    double[] collectedRssi3 = new double[arraySize];
    double[][] collectedRssi = new double[3][arraySize];

    double [] bestRssi = new double[3];

    public selectBetterRssi(double[] routerInfoArray1, double[] routerInfoArray2, double[] routerInfoArray3) {
        this.collectedRssi1 = routerInfoArray1;
        this.collectedRssi2 = routerInfoArray2;
        this.collectedRssi3 = routerInfoArray3;
    }

    public double [][] getAverage_StandDev() {
        for (int i = 0; i <3; i++) {
            if (i == 0) {
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi1[j];
                }
            }
            else if (i == 1) {
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi2[j];
                }
            }
            else{
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi3[j];
                }
            }
        }
        //get average of collectedRssi_i:u
        double u1;
        double u2;
        double u3;
        double collectedRssi1_sum = 0;
        double collectedRssi2_sum = 0;
        double collectedRssi3_sum = 0;
        for(int i = 0;i<3;i++){
            switch (i){
                case 0:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi1_sum += collectedRssi [i][j];
                    }
                    break;
                case 1:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi2_sum += collectedRssi [i][j];
                    }
                    break;
                case 2:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi3_sum += collectedRssi [i][j];
                    }
                    break;
                default:
                    break;
            }
        }
        u1 = collectedRssi1_sum/arraySize;
        u2 = collectedRssi2_sum/arraySize;
        u3 = collectedRssi3_sum/arraySize;
        //get Variance
        double variance1;
        double variance2;
        double variance3;
        double collectedRssi1_Square_sum = 0;
        double collectedRssi2_Square_sum = 0;
        double collectedRssi3_Square_sum = 0;
        for(int i = 0;i<3;i++){
            switch (i){
                case 0:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi1_Square_sum += (collectedRssi [i][j]-u1)*(collectedRssi [i][j]-u1);
                    }
                    break;
                case 1:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi2_Square_sum += (collectedRssi [i][j]-u2)*(collectedRssi [i][j]-u2);
                    }
                    break;
                case 2:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi3_Square_sum += (collectedRssi [i][j]-u3)*(collectedRssi [i][j]-u3);
                    }
                    break;
                default:
                    break;
            }
        }
        variance1 = collectedRssi1_Square_sum/(arraySize-1);
        variance2 = collectedRssi2_Square_sum/(arraySize-1);
        variance3 = collectedRssi3_Square_sum/(arraySize-1);
        double  [][] average_Square = {{u1,variance1},{u2,variance2},{u3,variance3}};
        return average_Square;
    }

    public double [] funktionOfX(){
        double [][] getAverage_StandDev = getAverage_StandDev();
        double u1 = getAverage_StandDev [0][0];
        double u2 = getAverage_StandDev [1][0];
        double u3 = getAverage_StandDev [2][0];
        double var1 = getAverage_StandDev [0][1];
        double var2 = getAverage_StandDev [1][1];
        double var3 = getAverage_StandDev [2][1];
        double [] F1 = new double[arraySize];
        double [] F2 = new double[arraySize];
        double [] F3 = new double[arraySize];
        double [] Rssi1_better = new double[arraySize];
        double [] Rssi2_better = new double[arraySize];
        double [] Rssi3_better = new double[arraySize];
        double bestRssi1;
        double bestRssi2;
        double bestRssi3;
        double bestRssi1_sum = 0;
        double bestRssi2_sum = 0;
        double bestRssi3_sum = 0;
        double f1_max_half = 1/sqrt(2*Math.PI*var1)/2;
        double f2_max_half = 1/sqrt(2*Math.PI*var2)/2;
        double f3_max_half = 1/sqrt(2*Math.PI*var3)/2;
        /*//select better Rssi from AP1
        double count1 = 0;
        for (int x=0; x<arraySize;x++){
            double index1 = -pow(collectedRssi1[x]-u1,2)/(2*var1);
            F1[x] = 1/(sqrt(2*Math.PI*var1))*Math.exp(index1);
            if(F1[x]>=possibility){
                Rssi1_better[x] = collectedRssi1[x];
                count1++;
            }
        }
        for(int i=0;i<Rssi1_better.length;i++){
            bestRssi1_sum += Rssi1_better [i];
        }
        bestRssi1 =bestRssi1_sum/count1;*/
        //select better Rssi from AP2
        double count2 = 0;

        for (int x=0; x<arraySize;x++){
            double index2 = -pow(collectedRssi2[x]-u2,2)/(2*var2);
            F2[x] = 1/(sqrt(2*Math.PI*var2))*Math.exp(index2);
            if(F2[x]>=f2_max_half){
                Rssi2_better[x] = collectedRssi2[x];
                count2++;
            }
        }
        for(int i=0;i<Rssi2_better.length;i++){
            bestRssi2_sum += Rssi2_better [i];
        }
        bestRssi2 =bestRssi2_sum/count2;
        //select better Rssi from AP3
        double count3 = 0;
        for (int x=0; x<arraySize;x++){
            double index3 = -pow(collectedRssi3[x]-u3,2)/(2*var3);
            F3[x] = 1/(sqrt(2*Math.PI*var3))*Math.exp(index3);
            if(F3[x]>=f3_max_half){
                Rssi3_better[x] = collectedRssi3[x];
                count3++;
            }
        }
        for(int i=0;i<Rssi3_better.length;i++){
            bestRssi3_sum += Rssi3_better [i];
        }
        bestRssi3 =bestRssi3_sum/count3;
        /*bestRssi[0] = bestRssi1;*/
        bestRssi[1] = bestRssi2;
        bestRssi[2] = bestRssi3;
        return bestRssi;
    }
}