package com.starwanmeigo.xu.gps_wifi_combine;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by xu on 05.05.15.
 */
public class selectBetterRssi {
    int arraySize = 10;
    int trueorfalse = 0;
    /*double possibility = 0.03;*/
    double[] collectedRssi1 = new double[arraySize];
    double[] collectedRssi2 = new double[arraySize];
    double[] collectedRssi3 = new double[arraySize];
    double[] collectedRssi4 = new double[arraySize];
    double[] collectedRssi5 = new double[arraySize];
    double[][] collectedRssi = new double[5][arraySize];

    double [] bestRssi = new double[5];

    public selectBetterRssi(double[] routerInfoArray1, double[] routerInfoArray2, double[] routerInfoArray3, double[] routerInfoArray4, double[] routerInfoArray5) {
        this.collectedRssi1 = routerInfoArray1;
        this.collectedRssi2 = routerInfoArray2;
        this.collectedRssi3 = routerInfoArray3;
        this.collectedRssi4 = routerInfoArray4;
        this.collectedRssi5 = routerInfoArray5;
    }

    public double [][] getAverage_StandDev() {
        for (int i = 0; i <5; i++) {
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
            else if (i == 2) {
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi3[j];
                }
            }
            else if (i == 3) {
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi4[j];
                }
            }
            else{
                for (int j = 0; j < arraySize; j++) {
                    collectedRssi[i][j] = collectedRssi5[j];
                }
            }
        }
        //get average of collectedRssi_i:u
        double u1;
        double u2;
        double u3;
        double u4;
        double u5;
        double collectedRssi1_sum = 0;
        double collectedRssi2_sum = 0;
        double collectedRssi3_sum = 0;
        double collectedRssi4_sum = 0;
        double collectedRssi5_sum = 0;
        for(int i = 0;i<5;i++){
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
                case 3:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi4_sum += collectedRssi [i][j];
                    }
                    break;
                case 4:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi5_sum += collectedRssi [i][j];
                    }
                    break;
                default:
                    break;
            }
        }
        u1 = collectedRssi1_sum/arraySize;
        u2 = collectedRssi2_sum/arraySize;
        u3 = collectedRssi3_sum/arraySize;
        u4 = collectedRssi4_sum/arraySize;
        u5 = collectedRssi5_sum/arraySize;
        //get Variance
        double variance1;
        double variance2;
        double variance3;
        double variance4;
        double variance5;
        double collectedRssi1_Square_sum = 0;
        double collectedRssi2_Square_sum = 0;
        double collectedRssi3_Square_sum = 0;
        double collectedRssi4_Square_sum = 0;
        double collectedRssi5_Square_sum = 0;
        for(int i = 0;i<5;i++){
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
                case 3:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi4_Square_sum += (collectedRssi [i][j]-u4)*(collectedRssi [i][j]-u4);
                    }
                    break;
                case 4:
                    for(int j=0;j<arraySize;j++){
                        collectedRssi5_Square_sum += (collectedRssi [i][j]-u5)*(collectedRssi [i][j]-u5);
                    }
                    break;
                default:
                    break;
            }
        }
        variance1 = collectedRssi1_Square_sum/(arraySize-1);
        variance2 = collectedRssi2_Square_sum/(arraySize-1);
        variance3 = collectedRssi3_Square_sum/(arraySize-1);
        variance4 = collectedRssi4_Square_sum/(arraySize-1);
        variance5 = collectedRssi5_Square_sum/(arraySize-1);
        double  [][] average_Square = {{u1,variance1},{u2,variance2},{u3,variance3},{u4,variance4},{u5,variance5}};
        return average_Square;
    }

    public int sameOrNot1(){
        int countNum1 = 0;
        for(int count1=0;count1<arraySize-1;count1++){
            if(collectedRssi1[0] == collectedRssi1[count1+1]){
                countNum1++;
            }
        }
        return  countNum1;
    }
    public int sameOrNot2(){
        int countNum2 = 0;
        for(int count2=0;count2<arraySize-1;count2++){
            if(collectedRssi2[0] == collectedRssi2[count2+1]){
                countNum2++;
            }
        }
        return  countNum2;
    }
    public int sameOrNot3(){
        int countNum3 = 0;
        for(int count3=0;count3<arraySize-1;count3++){
            if(collectedRssi3[0] == collectedRssi3[count3+1]){
                countNum3++;
            }
        }
        return  countNum3;
    }
    public int sameOrNot4(){
        int countNum4 = 0;
        for(int count4=0;count4<arraySize-1;count4++){
            if(collectedRssi4[0] == collectedRssi4[count4+1]){
                countNum4++;
            }
        }
        return  countNum4;
    }
    public int sameOrNot5(){
        int countNum5 = 0;
        for(int count5=0;count5<arraySize-1;count5++){
            if(collectedRssi5[0] == collectedRssi5[count5+1]){
                countNum5++;
            }
        }
        return  countNum5;
    }
    public double [] funktionOfX(){

        //select better Rssi from AP1
        if(sameOrNot1()==arraySize-1){
            bestRssi[0] = collectedRssi1[0];
        }
        else{
            double [][] getAverage_StandDev = getAverage_StandDev();
            double u1 = getAverage_StandDev [0][0];
            double var1 = getAverage_StandDev [0][1];
            double [] F1 = new double[arraySize];
            double [] Rssi1_better = new double[arraySize];
            double bestRssi1_sum = 0;
            double f1_max_half = 1/sqrt(2*Math.PI*var1)/2;
            double count1 = 0;
            for (int x=0; x<arraySize;x++){
                double index1 = -pow(collectedRssi1[x]-u1,2)/(2*var1);
                F1[x] = 1/(sqrt(2*Math.PI*var1))*Math.exp(index1);
                if(F1[x]>=f1_max_half){
                    Rssi1_better[x] = collectedRssi1[x];
                    count1++;
                }
            }
            for(int i=0;i<Rssi1_better.length;i++){
                bestRssi1_sum += Rssi1_better [i];
            }
            bestRssi[0] =bestRssi1_sum/count1;
        }

        //select better Rssi from AP2
        if(sameOrNot2()==arraySize-1){
            bestRssi[1] = collectedRssi2[0];
        }
        else{
            double [][] getAverage_StandDev = getAverage_StandDev();
            double u2 = getAverage_StandDev [1][0];
            double var2 = getAverage_StandDev [1][1];
            double [] F2 = new double[arraySize];
            double [] Rssi2_better = new double[arraySize];
            double bestRssi2_sum = 0;
            double f2_max_half = 1/sqrt(2*Math.PI*var2)/2;
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
            bestRssi[1] =bestRssi2_sum/count2;
        }

        //select better Rssi from AP3
        if (sameOrNot3()==arraySize-1){
            bestRssi[2] = collectedRssi3[0];
        }
        else {
            double [][] getAverage_StandDev = getAverage_StandDev();

            double u3 = getAverage_StandDev [2][0];
            double var3 = getAverage_StandDev [2][1];
            double [] F3 = new double[arraySize];
            double [] Rssi3_better = new double[arraySize];
            double bestRssi3_sum = 0;
            double f3_max_half = 1/sqrt(2*Math.PI*var3)/2;
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
            bestRssi[2] =bestRssi3_sum/count3;
        }

        //select better Rssi from AP4
        if (sameOrNot4()==arraySize-1){
            bestRssi[3] = collectedRssi4[0];
        }
        else {
            double [][] getAverage_StandDev = getAverage_StandDev();

            double u4 = getAverage_StandDev [3][0];
            double var4 = getAverage_StandDev [3][1];
            double [] F4 = new double[arraySize];
            double [] Rssi4_better = new double[arraySize];
            double bestRssi4_sum = 0;
            double f4_max_half = 1/sqrt(2*Math.PI*var4)/2;
            double count4 = 0;
            for (int x=0; x<arraySize;x++){
                double index4 = -pow(collectedRssi4[x]-u4,2)/(2*var4);
                F4[x] = 1/(sqrt(2*Math.PI*var4))*Math.exp(index4);
                if(F4[x]>=f4_max_half){
                    Rssi4_better[x] = collectedRssi4[x];
                    count4++;
                }
            }
            for(int i=0;i<Rssi4_better.length;i++){
                bestRssi4_sum += Rssi4_better [i];
            }
            bestRssi[3] =bestRssi4_sum/count4;
        }

        //select better Rssi from AP5
        if (sameOrNot5()==arraySize-1){
            bestRssi[4] = collectedRssi5[0];
        }
        else {
            double [][] getAverage_StandDev = getAverage_StandDev();

            double u5 = getAverage_StandDev [4][0];
            double var5 = getAverage_StandDev [4][1];
            double [] F5 = new double[arraySize];
            double [] Rssi5_better = new double[arraySize];
            double bestRssi5_sum = 0;
            double f5_max_half = 1/sqrt(2*Math.PI*var5)/2;
            double count5 = 0;
            for (int x=0; x<arraySize;x++){
                double index5 = -pow(collectedRssi5[x]-u5,2)/(2*var5);
                F5[x] = 1/(sqrt(2*Math.PI*var5))*Math.exp(index5);
                if(F5[x]>=f5_max_half){
                    Rssi5_better[x] = collectedRssi5[x];
                    count5++;
                }
            }
            for(int i=0;i<Rssi5_better.length;i++){
                bestRssi5_sum += Rssi5_better [i];
            }
            bestRssi[4] =bestRssi5_sum/count5;
        }
        return bestRssi;
    }
}